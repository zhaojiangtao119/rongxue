package com.labelwall.mywall.main.user.account.add;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;

/**
 * Created by Administrator on 2018-02-23.
 */

public class ActivityAccountAddOrderDetailDelegate extends WallDelegate {

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
        Log.e("数据", ORDER_INFO.toString());
    }
}
