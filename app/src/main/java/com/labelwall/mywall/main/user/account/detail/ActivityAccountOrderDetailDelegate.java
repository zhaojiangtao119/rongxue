package com.labelwall.mywall.main.user.account.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.util.log.WallLogger;

/**
 * Created by Administrator on 2018-02-23.
 */

public class ActivityAccountOrderDetailDelegate extends WallDelegate {


    private final int ORDER_ID;
    private final int ACTIVITY_ID;

    public ActivityAccountOrderDetailDelegate(int orderId, int activityId) {
        this.ORDER_ID = orderId;
        this.ACTIVITY_ID = activityId;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_account_order_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        //TODO 加载订单信息与该活动信息
        Log.e("传递的数据：", ORDER_ID + "---" + ACTIVITY_ID);
    }
}
