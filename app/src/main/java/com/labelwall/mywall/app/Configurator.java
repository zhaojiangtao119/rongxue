package com.labelwall.mywall.app;

import android.app.Activity;
import android.os.Handler;

import com.blankj.utilcode.util.Utils;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.qiniu.android.collect.Config;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**
 * Created by Administrator on 2018-01-03.
 */

public final class Configurator {
    //存储全局配置项
    private static final HashMap<Object, Object> WALL_CONFIGS = new HashMap<>();
    //存储字体图标
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();
    //存储网络请求拦截器
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();
    //创建Handler
    private static final Handler HANDLER = new Handler();


    private Configurator() {
        WALL_CONFIGS.put(ConfigKeys.CONFIG_READY, false);
        WALL_CONFIGS.put(ConfigKeys.HANDLER, HANDLER);
    }

    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }

    public static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    public HashMap<Object, Object> getWallConfigs() {
        return WALL_CONFIGS;
    }

    public final Configurator withApiHost(String host) {
        WALL_CONFIGS.put(ConfigKeys.API_HOST, host);
        return this;
    }

    public final Configurator withIcon(IconFontDescriptor descriptor) {
        ICONS.add(descriptor);
        return this;
    }

    public final Configurator withInterceptor(Interceptor interceptor) {
        INTERCEPTORS.add(interceptor);
        WALL_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors) {
        INTERCEPTORS.addAll(interceptors);
        WALL_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    public final Configurator withActivity(Activity activity) {
        WALL_CONFIGS.put(ConfigKeys.ACTIVITY, activity);
        return this;
    }

    private void initIcons() {
        if (ICONS.size() > 0) {
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }

    private void checkConfiguration() {
        final boolean isReady = (boolean) WALL_CONFIGS.get(ConfigKeys.CONFIG_READY);
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }

    //获取配置项
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key) {
        checkConfiguration();//检测是否初始化完成配置参数
        Object value = WALL_CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + "is null");
        }
        return (T) WALL_CONFIGS.get(key);
    }

    public final void configure() {
        //初始化字体图标
        initIcons();
        //标记初始化参数完成
        WALL_CONFIGS.put(ConfigKeys.CONFIG_READY, true);
        //初始化工具包
        Utils.init(MyWall.getApplicationContext());
    }
}
