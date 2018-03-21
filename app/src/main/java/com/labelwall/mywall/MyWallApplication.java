package com.labelwall.mywall;

import android.app.Application;
import android.content.Context;

import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.util.icon.FontEcModule;
import com.labelwall.mywall.util.net.interceptor.DebugInterceptor;
import com.labelwall.mywall.util.qiniu.QnUploadHelper;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018-01-03.
 */

public class MyWallApplication extends Application {

    private static MyWallApplication app;
    private static Context appContext;
    private static ILVLiveConfig mLVLiveConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appContext = getApplicationContext();


        MyWall.init(this)
                .withIcon(new FontAwesomeModule())//font awesome Module
                .withIcon(new FontEcModule())//自定义的字体图标
                .withInterceptor(new DebugInterceptor("location", R.raw.test))
                .withApiHost("http://vnp38f.natappfree.cc/zhaopin/")
                //.withApiHost("http://www.labelwall.com:8080/zhaopin_war/")
                .configure();
        //初始化数据库
        DataBaseManager.getInstance().init(this);
        //初始化JPush
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //初始化七牛云存储
        QnUploadHelper.init("lHd8EiXTJDgq2iWd1acOnfnAlRjyZadlsmJkfpkt",
                "i8O1jGWfwkqQLRc9EAe5xxw5lfNKsvmN8Py_5oMS",
                "http://p0fv1cvkm.bkt.clouddn.com/",
                "wall-bucket");
        //初始化腾讯直播SDK
        ILiveLog.setLogLevel(ILiveLog.TILVBLogLevel.DEBUG);
        ILiveSDK.getInstance().initSdk(this, 1400049396, 19148);
        mLVLiveConfig = new ILVLiveConfig();
        ILVLiveManager.getInstance().init(mLVLiveConfig);
    }

    public static MyWallApplication getApplication() {
        return app;
    }

    public static Context getContext() {
        return appContext;
    }

    public static ILVLiveConfig getILVLiveConfig() {
        return mLVLiveConfig;
    }
}
