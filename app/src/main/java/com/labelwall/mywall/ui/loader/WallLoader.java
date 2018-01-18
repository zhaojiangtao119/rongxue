package com.labelwall.mywall.ui.loader;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.labelwall.mywall.R;
import com.labelwall.mywall.util.DimenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-04.
 * showLoader，将LoaderView与dialog结合起来进行显示
 */

public class WallLoader {
    //存储所有的dialog
    private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();
    //dialog相对于屏幕缩小的尺寸
    private static final int LOADER_SIZE_SCALE = 8;
    //dialog偏移量
    private static final int LOADER_OFFSET_SCALE = 10;
    //默认的Loader
    private static final String DEFAULT_LOADER = LoaderStyle.BallClipRotatePulseIndicator.name();

    public static void showLoading(Context context, String type) {
        //加载对话框样式
        AppCompatDialog dialog = new AppCompatDialog(context, R.style.loaderDialog);
        //获取loader的view对象
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type, context);
        //将loader的view设置到dialog中
        dialog.setContentView(avLoadingIndicatorView);

        //设置dialog的尺寸(以屏幕大小进行缩小)，位置
        int deviceWidth = DimenUtil.getScreenWidth();
        int deviceHeight = DimenUtil.getScreenHeight();
        final Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();//获取dialoag的窗体参数
            lp.width = deviceWidth / LOADER_SIZE_SCALE;
            lp.height = deviceHeight / LOADER_SIZE_SCALE;
            lp.height = lp.height + deviceHeight / LOADER_OFFSET_SCALE;
            lp.gravity = Gravity.CENTER;
        }
        LOADERS.add(dialog);//缓存dialog
        dialog.show();
    }

    public static void showLoading(Context context) {
        showLoading(context, DEFAULT_LOADER);
    }

    public static void showLoading(Context context, Enum<LoaderStyle> type) {
        showLoading(context, type.name());
    }

    public static void stopLoading() {
        for (AppCompatDialog dialog : LOADERS) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        }
    }

}
