package com.labelwall.mywall.main.user.account.add;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.cart.order.OrderDetailDelegate;
import com.labelwall.mywall.main.user.account.add.history.ActivityAccountAddHistoryDelegate;
import com.labelwall.mywall.util.pay.FastPay;
import com.labelwall.mywall.util.pay.IAIPayResultListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-02-23.
 */

public class ActivityAccountAddOrderDetailDelegate extends WallDelegate implements IAIPayResultListener {

    @BindView(R2.id.tv_account_order_no)
    AppCompatTextView mOrderNoView = null;
    @BindView(R2.id.tv_account_order_info)
    AppCompatTextView mOrderInfo = null;
    @BindView(R2.id.tv_account_order_price)
    AppCompatTextView mOrderPrice = null;
    @BindView(R2.id.tv_account_order_jindou_num)
    AppCompatTextView mJindouNum = null;
    @BindView(R2.id.tv_account_order_status)
    AppCompatTextView mOrderStatus = null;

    private String mOrderNo = null;
    private Integer mAccountId = null;

    @OnClick(R2.id.btn_submit_add_order)
    void onClickPayOrder() {
        Long orderNo = Long.valueOf(mOrderNo);
        AccountAddPay.create(ActivityAccountAddOrderDetailDelegate.this)
                .setPayResultListener(ActivityAccountAddOrderDetailDelegate.this)
                .setOrderNo(orderNo)
                .beginPayDialog();
    }

    private final JSONObject ORDER_INFO;

    public ActivityAccountAddOrderDetailDelegate(JSONObject orderInfo) {
        this.ORDER_INFO = orderInfo;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_account_add_order_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        bindData();
    }

    private void bindData() {
        mOrderNo = ORDER_INFO.getString("orderNo");
        mAccountId = ORDER_INFO.getInteger("accountId");
        final String orderInfo = ORDER_INFO.getString("orderInfo");
        final double orderPrice = ORDER_INFO.getDouble("orderPrice");
        final Integer jindouNum = ORDER_INFO.getInteger("jindouCount");
        final String orderStatus = ORDER_INFO.getString("statusDesc");
        mOrderNoView.setText(mOrderNo);
        mOrderInfo.setText(orderInfo);
        mOrderPrice.setText(String.valueOf(orderPrice));
        mJindouNum.setText(String.valueOf(jindouNum));
        mOrderStatus.setText(orderStatus);
    }

    @Override
    public void onPaySuccess() {
        //TODO 支付成功后跳转到用户的充值页面
        getSupportDelegate().startWithPop(new ActivityAccountAddHistoryDelegate(mAccountId));
    }

    @Override
    public void onPaying() {

    }

    @Override
    public void onPayFail() {

    }

    @Override
    public void onPayCancel() {

    }

    @Override
    public void onPayConnectError() {

    }
}
