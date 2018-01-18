package com.labelwall.mywall.app;

/**
 * Created by Administrator on 2018-01-05.
 * 用户是否登录的回调
 */

public interface IUserChecker {

    void onSignIn();//用户已经登录

    void onNotSignIn();//用户没有登录

}
