package com.labelwall.mywall.ui.camera;

import android.net.Uri;

/**
 * Created by Administrator on 2018-01-13.
 * 存储一些中间值 存储Uri
 */

public final class CameraImageBean {

    private Uri mPath = null;
    //饿汉的单例模式
    private static final CameraImageBean INSTRANCE = new CameraImageBean();

    public static CameraImageBean getInstance() {
        return INSTRANCE;
    }

    public Uri getPath() {
        return mPath;
    }

    public void setPath(Uri path) {
        this.mPath = path;
    }
}
