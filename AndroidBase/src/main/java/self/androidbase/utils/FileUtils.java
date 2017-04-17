package self.androidbase.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtils {


    private static final int BUFF_SIZE = 1024 * 500; // 500kb


    /**
     * 获得程序默认保存的图片路径
     *
     * @return
     */
    public static File getAppDefauleSaveImagePath() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/SelfTalk/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }


    /**
     * 压缩文件
     *
     * @param files 文件
     */
    public static File zipData(Context context, List<File> files) {
        try {
            //context.getFilesDir()+
            File zipFile = new File(Environment.getExternalStorageDirectory().getPath() + "/SelfTalk/" + System.currentTimeMillis());
            ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile), BUFF_SIZE));

            if (files != null) {
                for (File file : files) {
                    FileInputStream fis = null;
                    BufferedInputStream bis = null;
                    try {
                        byte[] buffer = new byte[BUFF_SIZE];
                        fis = new FileInputStream(file);
                        bis = new BufferedInputStream(fis, buffer.length);
                        int read = 0;
                        zipout.putNextEntry(new ZipEntry(file.getName()));
                        while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                            zipout.write(buffer, 0, read);
                        }
                        zipout.closeEntry();
                    } finally {
                        bis.close();
                        fis.close();
                    }
                }
            }
            zipout.flush();
            zipout.finish();
            zipout.close();
            return zipFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解压文件
     *
     * @param rootPath
     * @param file
     * @return
     */
    public static Map<String, List<File>> unzipData(String rootPath, File file) {
        try {
            Map<String, List<File>> result = new HashMap<String, List<File>>();

            File rootFile = new File(rootPath);
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }

            String content = "";
            List<File> listFiles = new ArrayList<File>();
            ZipFile zipFile = new ZipFile(file);
            Enumeration<?> entries = zipFile.entries();
            BufferedOutputStream bos = null;
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                if (entry.getName().equals("0")) {
                    content = new String(entry.getExtra());
                } else {
                    InputStream in = zipFile.getInputStream(entry);
                    File targetFile = new File(rootPath + entry.getName());

                    bos = new BufferedOutputStream(new FileOutputStream(targetFile));
                    int read = 0;
                    byte[] buffer = new byte[BUFF_SIZE];
                    while ((read = in.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.flush();
                    in.close();
                    listFiles.add(targetFile);
                }
            }
            if (bos != null) {
                bos.close();
            }
            result.put(content, listFiles);
            zipFile.close();
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 保存文件
     *
     * @param bitmap
     * @param path
     */
    public static void saveBitmapToPath(Bitmap bitmap, String path, boolean isRecycle) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        try {
            if (path.toLowerCase().endsWith(".png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            }

            fos = new FileOutputStream(path);
            fos.write(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null && isRecycle) {
                bitmap.recycle();
            }
        }

    }


    /**
     * 递归彻底删除文件
     *
     * @param file
     */
    public static long deleteFiles(File file) {
        long currLength = file.length();
        if (file.isDirectory()) {
            for (File fl : file.listFiles()) {
                currLength += deleteFiles(fl);
            }
            file.delete();
        } else {
            boolean isDelete = file.delete();
        }
        return currLength;
    }

}
