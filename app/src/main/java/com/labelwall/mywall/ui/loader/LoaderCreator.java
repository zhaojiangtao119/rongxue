package com.labelwall.mywall.ui.loader;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

/**
 * Created by Administrator on 2018-01-03.
 * 加载过程中的loaderUI,创建一个AVLoadingIndicatorView对象
 */

public final class LoaderCreator {

    // 保存loader View 起一个缓存的作用
    // 这样就不用每次都去反射生成type对象
    private static final WeakHashMap<String, Indicator> LOADER_MAP = new WeakHashMap<>();

    /**
     * 创建AVLoadingIndicatorView对象
     * 设置content,indicator对象
     *
     * @param type
     * @param context
     * @return
     */
    static AVLoadingIndicatorView create(String type, Context context) {
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        if (LOADER_MAP.get(type) == null) {
            final Indicator indicator = getIndicator(type);
            LOADER_MAP.put(type, indicator);
        }
        avLoadingIndicatorView.setIndicator(LOADER_MAP.get(type));
        return avLoadingIndicatorView;
    }

    /**
     * 利用反射获取Indicator对象
     * 拼接该类的全限定名进行反射创建该类对象
     *
     * @param name 类名
     * @return
     */
    private static Indicator getIndicator(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        final StringBuilder drawableClassName = new StringBuilder();
        if (!name.contains(".")) {//如果name不包含“.”说明是一个类名
            final String defaultPackageName = AVLoadingIndicatorView.class.getPackage().getName();
            drawableClassName.append(defaultPackageName)
                    .append(".indicators")
                    .append(".");
        }
        drawableClassName.append(name);
        try {
            //通过类的全限定名反射获取loaderView的类对象
            final Class<?> drawableClass = Class.forName(drawableClassName.toString());
            return (Indicator) drawableClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
