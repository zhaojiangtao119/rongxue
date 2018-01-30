package com.labelwall.mywall.main.user.address;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Administrator on 2018-01-27.
 */

public class AdressDelegate extends WallDelegate {

    private final Long ORDER_NO;

    public AdressDelegate(Long orderNo) {
        this.ORDER_NO = orderNo;
    }

    @BindView(R2.id.rv_address_list)
    RecyclerView mRecyclerView = null;
    private final long mUserId =
            WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    @OnClick(R2.id.icon_address_add)
    void onClickAddAddress() {
        getSupportDelegate().start(new AddressAddDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_address_list;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initRecyclerView();
        initData();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
    }

    private void initData() {
        RestClient.builder()
                .url("app/shopping/list")
                .params("userId", mUserId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final List<MultipleItemEntity> data =
                                new AddressDataConverter().setJsonData(response).convert();
                        final AddressAdapter adapter = new AddressAdapter(data, AdressDelegate.this, ORDER_NO);
                        mRecyclerView.setAdapter(adapter);
                    }
                })
                .build()
                .get();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
