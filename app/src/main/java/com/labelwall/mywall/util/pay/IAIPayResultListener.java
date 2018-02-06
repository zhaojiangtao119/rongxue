package com.labelwall.mywall.util.pay;

/**
 * Created by Administrator on 2018-01-24.
 * 支付结果的监听
 */

public interface IAIPayResultListener {

    void onPaySuccess();

    void onPaying();

    void onPayFail();

    void onPayCancel();

    void onPayConnectError();

}
