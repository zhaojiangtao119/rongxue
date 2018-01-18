package com.labelwall.mywall.app;

import com.labelwall.mywall.util.storage.WallPreference;

/**
 * Created by Administrator on 2018-01-05.
 * 管理用户信息
 */

public class AccountManager {

    private enum SignTag {
        SIGN_TAG//登陆与否的一个标识
    }

    //保存用户的登录状态，登录后调用，保存用户的登录状态（success，fail）
    public static void setSignState(boolean state) {
        WallPreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }

    //判断用户是否登录成功
    private static boolean isSignIn() {
        return WallPreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    public static void checkAccount(IUserChecker checker) {
        if (isSignIn()) {//已经登录的回调
            checker.onSignIn();//执行已经登陆成功的方法
        } else {//未登录的回调
            checker.onNotSignIn();//执行未登录的方法
        }
    }
}
