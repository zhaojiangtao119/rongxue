package com.labelwall.mywall.main.user.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.account.add.ActivityAccountAddDelegate;
import com.labelwall.mywall.main.user.account.add.history.ActivityAccountAddHistoryDelegate;
import com.labelwall.mywall.ui.recycler.BaseDecoration;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-02-22.
 */

public class ActivityAccountDelegate extends WallDelegate {

    @BindView(R2.id.tv_account_num)
    AppCompatTextView mAccountNum = null;
    @BindView(R2.id.rv_activity_trade)
    RecyclerView mRecyclerView = null;

    private final long USER_ID =
            WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private Integer mAccountId = null;
    private ActivityAccountOrderAdapter mAdapter = null;
    private ActivityAccountOrderDataConverter mConverter = new ActivityAccountOrderDataConverter();


    @OnClick(R2.id.tv_account_add)
    void onClickAccountAdd() {
        getSupportDelegate().start(new ActivityAccountAddDelegate(mAccountId));
    }

    @OnClick(R2.id.tv_account_add_history)
    void onClickAccountAddHistory() {
        getSupportDelegate().start(new ActivityAccountAddHistoryDelegate(mAccountId));
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_account;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //加载账户详细信息
        uploadUserActivityAccount();
        initRecyclerView();
        uploadActivityOrder();
    }

    private void uploadUserActivityAccount() {
        RestClient.builder()
                .url("activity/account/" + USER_ID)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject activityAccount =
                                JSON.parseObject(response).getJSONObject("data");
                        final Integer id = activityAccount.getInteger("id");
                        final Integer accountNum = activityAccount.getInteger("balance");
                        mAccountId = id;
                        mAccountNum.setText(String.valueOf(accountNum));
                    }
                })
                .build()
                .get();
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(BaseDecoration.
                create(ContextCompat.getColor(getContext(), R.color.app_background), 5));
        mRecyclerView.addOnItemTouchListener(new ActivityAccountOrderClickListener(this));
    }

    private void uploadActivityOrder() {
        RestClient.builder()
                //TODO
                .url("activity/account/trade/" + USER_ID)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter = new ActivityAccountOrderAdapter(
                                mConverter.setJsonData(response).convert());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                })
                .build()
                .get();
    }
}
