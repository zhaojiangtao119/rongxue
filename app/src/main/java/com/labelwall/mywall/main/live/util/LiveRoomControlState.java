package com.labelwall.mywall.main.live.util;

import com.tencent.ilivesdk.ILiveConstants;

/**
 * Created by Administrator on 2018-03-17.
 */

public class LiveRoomControlState {
    private boolean isBeautyOn = false;//美颜，默认关闭
    private boolean isFlashOn = false;//闪光灯，默认关闭
    private boolean isVoiceOn = true;//麦克风，默认打开
    private int cameraid = ILiveConstants.BACK_CAMERA;//摄像头ID，默认是后置摄像头

    public boolean isBeautyOn() {
        return isBeautyOn;
    }

    public void setBeautyOn(boolean beautyOn) {
        isBeautyOn = beautyOn;
    }

    public boolean isFlashOn() {
        return isFlashOn;
    }

    public void setFlashOn(boolean flashOn) {
        isFlashOn = flashOn;
    }

    public boolean isVoiceOn() {
        return isVoiceOn;
    }

    public void setVoiceOn(boolean voiceOn) {
        isVoiceOn = voiceOn;
    }

    public int getCameraid() {
        return cameraid;
    }

    public void setCameraid(int cameraid) {
        this.cameraid = cameraid;
    }
}
