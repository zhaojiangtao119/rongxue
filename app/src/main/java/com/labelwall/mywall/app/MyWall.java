package com.labelwall.mywall.app;

import android.content.Context;
import android.os.Handler;

/**
 * Created by Administrator on 2018-01-03.
 * 项目整体配置类，将全局信息存储在Map集合中
 */

public final class MyWall {

    public static Configurator init(Context context){
        Configurator.getInstance()
                .getWallConfigs()
                .put(ConfigKeys.APPLICATION_CONTEXT,context.getApplicationContext());
        return Configurator.getInstance();
    }

    public static Configurator getConfigurator(){
        return Configurator.getInstance();
    }

    public static <T> T getConfiguration(Object key){
        return getConfigurator().getConfiguration(key);
    }

    public static Context getApplicationContext(){
        return getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
    }

    public static Handler getHandler(){
        return getConfiguration(ConfigKeys.HANDLER);
    }


}
