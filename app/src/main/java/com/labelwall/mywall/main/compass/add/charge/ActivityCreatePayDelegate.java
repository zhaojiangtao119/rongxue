package com.labelwall.mywall.main.compass.add.charge;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.compass.add.ActivityCreateInfoItem;
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

public class ActivityCreatePayDelegate extends WallDelegate {

    @BindView(R2.id.tv_account_num)
    AppCompatTextView mAccountJindouNumView = null;
    @BindView(R2.id.lv_jindou_count)
    ListView mListView = null;

    private final Map<String, Object> ACTIVITY_PARAMS;
    private Long mAccountId = null;
    private Integer mAccountJindouNum = null;
    private Integer mSelectJindouNum = null;
    private final long USER_ID =
            WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private List<String> mJindouNum = new ArrayList<>();
    private ActivityAccountJindouAdapter mAdapter = null;
    private Integer mSelectJindou = -1;

    public ActivityCreatePayDelegate(Map<String, Object> activityParams) {
        this.ACTIVITY_PARAMS = activityParams;
    }

    @OnClick(R2.id.tv_account_add)
    void onClickAccountJindouAdd() {
        //TODO 充值金豆
    }

    @OnClick(R2.id.btn_submit_activity_order)
    void onClickSubmitActivityOrder() {
        //提交活动的订单
        if (mSelectJindou == -1) {
            Toast.makeText(_mActivity, "请选择金豆数量！", Toast.LENGTH_SHORT).show();
        } else {
            mSelectJindouNum = Integer.valueOf(mJindouNum.get(mSelectJindou));
            if (mAccountJindouNum < mSelectJindouNum) {
                Toast.makeText(_mActivity, "账户余额不足，请充值！", Toast.LENGTH_SHORT).show();
            } else {
                //提交订单，将金豆数存储，创建订单
                ACTIVITY_PARAMS.put(ActivityCreateInfoItem.ACTIVITY_AMOUNT_PARAM, mSelectJindouNum);
                uploadActivityOrder();
            }
        }
    }

    private void uploadActivityOrder() {
        //需要的参数有:userId，jindouNum，订单描述，type:0
        String activityTitle = (String) ACTIVITY_PARAMS.get(ActivityCreateInfoItem.ACTIVITY_TITLE_PRAMS);
        if (activityTitle == null) {
            activityTitle = "参加某一个活动";
        }
        RestClient.builder()
                .url("activity/account/trade/add/a")
                .params("userId", USER_ID)
                .params("orderPrice", mSelectJindouNum)
                .params("orderInfo", activityTitle)
                .params("type", 0)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject jsonResponse = JSON.parseObject(response);
                        final int status = jsonResponse.getInteger("status");
                        final String message = jsonResponse.getString("msg");
                        final JSONObject data = jsonResponse.getJSONObject("data");
                        //跳转到订单详情页面
                        getSupportDelegate().startWithPop(new ActivityCreateOrderDetailDelegate(data, ACTIVITY_PARAMS));
                    }
                })
                .build()
                .post();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_pay;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        uploadUserActivityAccount();
        initListView();
    }

    private void initListView() {
        mJindouNum.add("5");
        mJindouNum.add("10");
        mJindouNum.add("20");
        mJindouNum.add("50");
        mJindouNum.add("100");
        mAdapter = new ActivityAccountJindouAdapter(this, mJindouNum);
        mListView.setAdapter(mAdapter);
        mAdapter.setCheckedListener(new ActivityAccountJindouAdapter.CheckedListener() {
            @Override
            public void selected(int postion) {
                //监听哪一个被选中了
                mSelectJindou = postion;
            }
        });
    }

    private void uploadUserActivityAccount() {
        RestClient.builder()
                .url("activity/account/" + USER_ID)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject activityAccount =
                                JSON.parseObject(response).getJSONObject("data");
                        mAccountId = activityAccount.getLong("id");
                        mAccountJindouNum = activityAccount.getInteger("balance");
                        mAccountJindouNumView.setText(String.valueOf(mAccountJindouNum));
                    }
                })
                .build()
                .get();
    }
}
