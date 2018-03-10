package com.labelwall.mywall.ui.camera;

import android.net.Uri;

/**
 * Created by Administrator on 2018-01-13.
 * 存储一些中间值 存储照片的Uri
 */

public final class CameraImageBean {

    private Uri mPath = null;
    //饿汉的单例模式
    /*private static final CameraImageBean INSTRANCE = new CameraImageBean();*/

   /* public static CameraImageBean getInstance() {
        return INSTRANCE;
    }
        懒汉的单例模式
    private static CameraImageBean IN = null;
    private static CameraImageBean getInstrance() {
        if (IN == null) {
            IN = new CameraImageBean();
        }
        return IN;
    }*/

    private static class Hodler {
        private static final CameraImageBean INSTANCE = new CameraImageBean();
    }

    public static CameraImageBean getInstance() {
        return Hodler.INSTANCE;
    }

    public Uri getPath() {
        return mPath;
    }

    public void setPath(Uri path) {
        this.mPath = path;
    }
}
