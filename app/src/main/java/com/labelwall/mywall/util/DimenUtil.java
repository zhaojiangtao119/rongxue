package com.labelwall.mywall.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.labelwall.mywall.app.MyWall;

/**
 * Created by Administrator on 2018-01-04.
 * 计算dialog尺寸工具类
 */

public class DimenUtil {

    /**
     * 获取屏幕的宽width
     * @return
     */
    public static int getScreenWidth(){
        final Resources resources = MyWall.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }
    /**
     * 获取屏幕的高height
     * @return
     */
    public static int getScreenHeight(){
        final Resources resources = MyWall.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

}
