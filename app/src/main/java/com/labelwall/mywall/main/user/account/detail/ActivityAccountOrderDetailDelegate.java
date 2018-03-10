package com.labelwall.mywall.main.user.account.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.util.log.WallLogger;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

/**
 * Created by Administrator on 2018-02-23.
 */

public class ActivityAccountOrderDetailDelegate extends WallDelegate {


    private final int ORDER_ID;
    private final int ACTIVITY_ID;
    private final long USER_ID =
            WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

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
        uploadActivtyOrderDate();

    }

    private void uploadActivtyOrderDate() {
        RestClient.builder()
                .url("activity/account/trade/detail")
                .params("userId", USER_ID)
                .params("orderId", ORDER_ID)
                .params("activityId", ACTIVITY_ID)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject jsonResponse = JSON.parseObject(response);
                        final int status = jsonResponse.getInteger("status");
                        final String message = jsonResponse.getString("msg");
                        if (status == 0) {
                            Toast.makeText(_mActivity, response, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build()
                .get();
    }
}
