package self.androidbase.utils;

import android.content.Context;
import android.os.Handler;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by Janesen on 15/11/22.
 */
public class SingleInstance {

    private static Map<Integer, Object> singles = new HashMap<>();

    public static <T> T getInstance(Class<T> targetClass,Object... params) {
        try {
            if(singles.containsKey(targetClass.hashCode())){
                return (T) singles.get(targetClass.hashCode());
            }else{
                T value=null;
                if (params != null) {
                    Class<?>[] paramsClass=new Class[params.length];
                    for (int i = 0; i <params.length ; i++) {

                        if(params[i] instanceof Context){
                            paramsClass[i]=Context.class;
                        }else if(params[i] instanceof Handler){
                            paramsClass[i]=Handler.class;
                        }
                        else{
                            paramsClass[i]=params[i].getClass();
                        }
                    }
                    Constructor<T> constructor=targetClass.getConstructor(paramsClass);
                    value=constructor.newInstance(params);
                }else{
                    Constructor<T> constructor=targetClass.getConstructor();
                    value=constructor.newInstance(params);
                }
                singles.put(targetClass.hashCode(),value);
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
           throw new RuntimeException(e.getCause());
        }
    }


    /**
     * 释放指定类的单例资源
     * @param targetClass
     */
    public static void release(Class<?> targetClass){
        Object value=singles.get(targetClass.hashCode());
        value=null;
        singles.remove(targetClass.hashCode());
    }

    /**
     * 释放所有单例
     */
    public static void releaseAll(){
        Set<Integer> keys=singles.keySet();
        for (Integer key :
                keys) {
            Object value=singles.get(key);
            value=null;
        }
        singles.clear();
    }
}
