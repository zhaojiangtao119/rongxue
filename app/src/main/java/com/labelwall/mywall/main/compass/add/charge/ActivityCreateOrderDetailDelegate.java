package com.labelwall.mywall.main.compass.add.charge;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.compass.add.ActivityCreateInfoItem;
import com.labelwall.mywall.main.compass.my.ActivityMyDelegate;
import com.labelwall.mywall.main.user.account.add.ActivityAccountJindouAdapter;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-02-24.
 */

public class ActivityCreateOrderDetailDelegate extends WallDelegate {

    @BindView(R2.id.tv_account_order_no)
    AppCompatTextView mOrderNoView = null;
    @BindView(R2.id.tv_account_order_info)
    AppCompatTextView mOrderInfo = null;

    @BindView(R2.id.tv_activity_order_desc)
    AppCompatTextView mOrderPriceDesc = null;
    @BindView(R2.id.tv_account_order_price)
    AppCompatTextView mOrderPrice = null;

    @BindView(R2.id.tv_account_order_jindou_num)
    AppCompatTextView mJindouNum = null;
    @BindView(R2.id.tv_account_order_status)
    AppCompatTextView mOrderStatus = null;

    @BindView(R2.id.btn_submit_add_order)
    Button mPayOrder = null;

    @OnClick(R2.id.btn_submit_add_order)
    void onClickPayActivityOrder() {
        //支付并创建活动
        RestClient.builder()
                .url("activity/account/trade/pay/" + mOrderNo)
                .params(ACTIVITY_PARAMS)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject jsonResponse = JSON.parseObject(response);
                        final int status = jsonResponse.getInteger("status");
                        final String message = jsonResponse.getString("msg");
                        if (status == 0) {
                            //TODO 页面跳转，问题：在请求过程中跳转页面，该页面涉及到了网络请求，则无法发送该请求
                            //getSupportDelegate().startWithPop(new ActivityMyDelegate());
                        } else {
                            Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build()
                .post();
    }

    private final Map<String, Object> ACTIVITY_PARAMS;
    private final JSONObject ORDER_PARAMS;
    private String mOrderNo = null;

    public ActivityCreateOrderDetailDelegate(JSONObject orderParams, Map<String, Object> activityParams) {
        this.ORDER_PARAMS = orderParams;
        this.ACTIVITY_PARAMS = activityParams;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_account_add_order_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        bindData();
    }

    private void bindData() {
        mOrderNo = ORDER_PARAMS.getString("orderNo");
        final String typeDesc = ORDER_PARAMS.getString("typeDesc");
        final Double orderPrice = ORDER_PARAMS.getDouble("orderPrice");
        final String orderInfo = ORDER_PARAMS.getString("orderInfo");
        final String orderStatus = ORDER_PARAMS.getString("statusDesc");
        mPayOrder.setText("支付并创建活动");
        mOrderNoView.setText(mOrderNo);
        mOrderInfo.setText(typeDesc);
        mOrderPriceDesc.setText("订单描述");
        mOrderPrice.setText(orderInfo);
        mJindouNum.setText(String.valueOf(orderPrice));
        mOrderStatus.setText(orderStatus);
    }
}
