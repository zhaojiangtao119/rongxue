package com.labelwall.mywall.main.sort.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.sort.SortDelegate;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-01-19.
 */

public class VerticalListDelegate extends WallDelegate {

    @BindView(R2.id.rv_vertical_list)
    RecyclerView mRecyclerView = null;

    private CategoryAdapter mAdapter = null;


    @Override
    public Object setLayout() {
        return R.layout.delegate_vertical_list;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(null);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //加载verticalList数据
        initRecyclerView();
        uploadCategory();
    }

    private void uploadCategory() {
        RestClient.builder()
                .url("product/get_category_all")
                //.loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final List<MultipleItemEntity> entityList =
                                new CategoryDataConverter().setJsonData(response).convert();
                        final SortDelegate delegate = getParentDelegate();
                        mAdapter = new CategoryAdapter(entityList, delegate);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                })
                .build()
                .get();
    }
}
