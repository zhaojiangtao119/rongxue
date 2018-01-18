package com.labelwall.mywall.delegates.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.app.AccountManager;
import com.labelwall.mywall.app.IUserChecker;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.timer.BaseTimerTask;
import com.labelwall.mywall.util.timer.ITimerLisenter;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-01-04.
 * APP启动图delegate（包含倒计时使用Timer,TimerTask）
 */

public class LauncherDelegate extends WallDelegate implements ITimerLisenter {

    @BindView(R2.id.tv_launcher_timer)
    AppCompatTextView mTvTimer = null;

    private Timer mTimer = null;

    private int mCount = 5;//倒计时的数字

    private ILauncherListener mILauncherListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener) {
            mILauncherListener = (ILauncherListener) activity;
        }
    }

    @OnClick(R2.id.tv_launcher_timer)
    void onClickTimerView() {
        if (mTimer != null) {
            mTimer.cancel();//取消定时器
            mTimer = null;
            checkIsShowScroll();
        }
    }

    /**
     * 初始化Timer和TimerTask
     */
    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask timerTask = new BaseTimerTask(this);
        //timerTask：执行的任务（会去执行run方法），0：延迟的时间，1000：间隔的时间
        mTimer.schedule(timerTask, 0, 1000);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_launcher;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initTimer();
    }

    /**
     * 判断是否展示滑动轮播图，用户是否第一次启动APP
     */
    private void checkIsShowScroll() {
        if (!WallPreference.getAppFlag(ScrollLauncherTag.HAS_FRIST_LAUNCHER_APP.name())) {
            start(new LauncherScrollDelegate(), SINGLETASK);//启动轮播图
        } else {
            //todo 检查用户是否已经登陆
            AccountManager.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {//用户已经登录，LauncherFinish  Signed
                    if (mILauncherListener != null) {
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);
                    }
                }

                @Override
                public void onNotSignIn() {//用户没有登录，LauncherFinish  Not_Signed
                    if (mILauncherListener != null) {
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED);
                    }
                }
            });
        }
    }

    @Override
    public void onTimer() {
        //主线程执行
        getProxyActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTvTimer != null) {
                    mTvTimer.setText(MessageFormat.format("跳过\n{0}s", mCount));
                    mCount--;
                    if (mCount < 0) {
                        if (mTimer != null) {
                            mTimer.cancel();//取消定时器
                            mTimer = null;
                            checkIsShowScroll();
                        }
                    }
                }
            }
        });
    }
}
