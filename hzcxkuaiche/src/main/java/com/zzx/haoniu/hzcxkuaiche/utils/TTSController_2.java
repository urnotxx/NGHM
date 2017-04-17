package com.zzx.haoniu.hzcxkuaiche.utils;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.util.LinkedList;

/**
 * 当前DEMO的播报方式是队列模式。其原理就是依次将需要播报的语音放入链表中，播报过程是从头开始依次往后播报。
 * <p>
 * 导航SDK原则上是不提供语音播报模块的，如果您觉得此种播报方式不能满足你的需求，请自行优化或改进。
 */
public class TTSController_2 implements SynthesizerListener {

    /**
     * 请替换您自己申请的ID。
     */
    private final String appId = "58eeeab5";

    public static TTSController_2 ttsManager;
    private Context mContext;
    private SpeechSynthesizer mTts;
    private boolean isPlaying = false;
    private LinkedList<String> wordList = new LinkedList();

    public void startSpeck(String text) {
        if (mTts != null) {
            mTts.startSpeaking(text, this);
        }
    }

    private TTSController_2(Context context) {
        mContext = context.getApplicationContext();
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID + "=" + appId);
        mTts = SpeechSynthesizer.createSynthesizer(mContext, initListener);
        init();
    }

    private void createSynthesizer() {

    }

    private InitListener initListener = new InitListener() {
        @Override
        public void onInit(int errorcode) {
            if (ErrorCode.SUCCESS == errorcode) {
            } else {
                Toast.makeText(mContext, "语音合成初始化失败!", Toast.LENGTH_SHORT);
            }
        }
    };

    public void init() {
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        //设置语速,值范围：[0, 100],默认值：50
        mTts.setParameter(SpeechConstant.SPEED, "45");
        //设置音量
        mTts.setParameter(SpeechConstant.VOLUME, "tts_volume");
        //设置语调
        mTts.setParameter(SpeechConstant.PITCH, "tts_pitch");
    }

    public static TTSController_2 getInstance(Context context) {
        if (ttsManager == null) {
            ttsManager = new TTSController_2(context);
        }
        return ttsManager;
    }

    public void stopSpeaking() {
        if (wordList != null) {
            wordList.clear();
        }
        if (mTts != null) {
            mTts.stopSpeaking();
        }
        isPlaying = false;
    }

    public void destroy() {
        if (mTts != null) {
            mTts.stopSpeaking();
            mTts.destroy();
            ttsManager = null;
        }
    }


    @Override
    public void onSpeakBegin() {

    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {

    }

    @Override
    public void onSpeakPaused() {

    }

    @Override
    public void onSpeakResumed() {

    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {

    }

    @Override
    public void onCompleted(SpeechError speechError) {

    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }
}
