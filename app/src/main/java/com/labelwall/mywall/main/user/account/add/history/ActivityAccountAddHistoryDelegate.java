package com.labelwall.mywall.main.user.account.add.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.account.ActivityAccountOrderDataConverter;
import com.labelwall.mywall.ui.recycler.BaseDecoration;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-02-22.
 */

public class ActivityAccountAddHistoryDelegate extends WallDelegate {

    @BindView(R2.id.rv_account_add_history)
    RecyclerView mRecyclerView = null;

    private final Integer ACCOUNT_ID;
    private ActivityAccountAddHistoryAdapter mAdapter = null;
    private ActivityAccountAddHistoryDataConverter mConverter =
            new ActivityAccountAddHistoryDataConverter();

    public ActivityAccountAddHistoryDelegate(Integer accountId) {
        this.ACCOUNT_ID = accountId;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_account_add_history;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView();
        uploadAccountAddHistory();
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(BaseDecoration.
                create(ContextCompat.getColor(getContext(), R.color.app_background), 5));
    }

    private void uploadAccountAddHistory() {
        RestClient.builder()
                .url("activity/account/trade/22/2")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter = new
                                ActivityAccountAddHistoryAdapter(mConverter.setJsonData(response).convert());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                })
                .build()
                .get();
    }
}
