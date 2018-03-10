package com.labelwall.mywall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.labelwall.mywall.activities.ProxyActivity;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.delegates.launcher.ILauncherListener;
import com.labelwall.mywall.delegates.launcher.LauncherDelegate;
import com.labelwall.mywall.delegates.launcher.OnLauncherFinishTag;
import com.labelwall.mywall.delegates.sign.ISignListener;
import com.labelwall.mywall.delegates.sign.SignInDelegate;
import com.labelwall.mywall.main.MainPageDelegate;
import com.labelwall.mywall.main.WallBottomDelegate;

import cn.jpush.android.api.JPushInterface;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Administrator on 2018-01-03.
 */

public class WallActivity extends ProxyActivity
        implements ISignListener, ILauncherListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();//隐藏actionBar
        }
        MyWall.getConfigurator().withActivity(this);
        StatusBarCompat.translucentStatusBar(this, true);
    }

   @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public WallDelegate setRootDelegate() {
        return new LauncherDelegate();
    }

    @Override
    public void onSignInSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        getSupportDelegate().start(new WallBottomDelegate());
    }

    @Override
    public void onSignUpSuccess() {
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 当launcher倒计时走完，用户登录了的回调，没有登录的回调
     *
     * @param tag
     */
    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        switch (tag) {
            case SIGNED:
                Toast.makeText(this, "用户登录了", Toast.LENGTH_SHORT).show();
                getSupportDelegate().start(new MainPageDelegate());
                break;
            case NOT_SIGNED:
                Toast.makeText(this, "用户未登录", Toast.LENGTH_SHORT).show();
                //startWithPop(): start的同时清除掉栈中上一个元素
                getSupportDelegate().startWithPop(new SignInDelegate());
                break;
            default:
                break;
        }
    }
}
