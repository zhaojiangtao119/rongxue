package com.labelwall.mywall;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.util.icon.FontEcModule;
import com.labelwall.mywall.util.net.interceptor.DebugInterceptor;
import com.labelwall.mywall.util.qiniu.QnUploadHelper;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018-01-03.
 */

public class MyWallApplication extends Application {

    private static MyWallApplication app;
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appContext = getApplicationContext();

        QnUploadHelper.init("lHd8EiXTJDgq2iWd1acOnfnAlRjyZadlsmJkfpkt",
                "i8O1jGWfwkqQLRc9EAe5xxw5lfNKsvmN8Py_5oMS",
                "http://p0fv1cvkm.bkt.clouddn.com/",
                "wall-bucket");

        MyWall.init(this)
                .withIcon(new FontAwesomeModule())//font awesome Module
                .withIcon(new FontEcModule())//自定义的字体图标
                .withInterceptor(new DebugInterceptor("location", R.raw.test))
                .withApiHost("http://g9cry9.natappfree.cc/Mypro/")
                .configure();
        //初始化数据库
        DataBaseManager.getInstance().init(this);
        //初始化JPush
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static MyWallApplication getApplication() {
        return app;
    }

    public static Context getContext() {
        return appContext;
    }
}
