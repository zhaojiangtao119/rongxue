package com.labelwall.mywall.delegates.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.labelwall.mywall.R;
import com.labelwall.mywall.app.AccountManager;
import com.labelwall.mywall.app.IUserChecker;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.ui.launcher.LauncherHolderCreator;
import com.labelwall.mywall.util.storage.WallPreference;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-04.
 * 第一次启动轮播图delegate,使用ConvenientBanner
 */

public class LauncherScrollDelegate extends WallDelegate implements OnItemClickListener {

    private ConvenientBanner<Integer> mConvenientBanner = null;

    private static final ArrayList<Integer> INTEGERS = new ArrayList<>();

    private ILauncherListener mILauncherListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener) {
            mILauncherListener = (ILauncherListener) activity;
        }
    }

    private void initBanner() {
        INTEGERS.add(R.mipmap.launcher_01);
        INTEGERS.add(R.mipmap.launcher_02);
        INTEGERS.add(R.mipmap.launcher_03);
        INTEGERS.add(R.mipmap.launcher_04);
        INTEGERS.add(R.mipmap.launcher_05);
        mConvenientBanner.setPages(new LauncherHolderCreator(), INTEGERS)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(this)
                .setCanLoop(false);
    }

    @Override
    public Object setLayout() {
        mConvenientBanner = new ConvenientBanner<>(getContext());
        return mConvenientBanner;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initBanner();
    }

    @Override
    public void onItemClick(int position) {
        if (position == INTEGERS.size() - 1) {//如果点击的是最后一张图片
            //在SharedPreference中保存用户已经第一次启动了APP
            WallPreference.setAppFlag(ScrollLauncherTag.HAS_FRIST_LAUNCHER_APP.name(), true);
            //TODO 检查用户是否已经登录
            AccountManager.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {
                    if (mILauncherListener != null) {
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);
                    }
                }

                @Override
                public void onNotSignIn() {
                    if (mILauncherListener != null) {
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED);
                    }
                }
            });
        }
    }
}
