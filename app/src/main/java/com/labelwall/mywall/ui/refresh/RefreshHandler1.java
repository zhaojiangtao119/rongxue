package com.labelwall.mywall.ui.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerAdapter;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

/**
 * Created by Administrator on 2018-01-09.
 */

public class RefreshHandler1 implements SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final RecyclerView RECYCLER_VIEW;
    private final PagingBean BEAN;
    private final DataConverter CONVERTER;
    private MultipleRecyclerAdapter mAdapter = null;

    private RefreshHandler1(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView, PagingBean bean, DataConverter conventer) {
        this.REFRESH_LAYOUT = refreshLayout;
        this.RECYCLER_VIEW = recyclerView;
        this.BEAN = bean;
        this.CONVERTER = conventer;
        REFRESH_LAYOUT.setOnRefreshListener(this);
    }

    public static RefreshHandler1 create(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView, DataConverter conventer) {
        return new RefreshHandler1(refreshLayout, recyclerView, new PagingBean(), conventer);
    }

    //下拉刷新加载
    @Override
    public void onRefresh() {

    }

    //上拉刷新加载
    @Override
    public void onLoadMoreRequested() {

    }

    public void firstPage() {
        RestClient.builder()
                .url("")
                .refreshLayout(REFRESH_LAYOUT)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject pageInfo = JSON.parseObject(response).getJSONObject("data");
                        BEAN.setPageIndex(pageInfo.getInteger("pageNum"))
                                .setTotal(pageInfo.getInteger("total"));
                        mAdapter = MultipleRecyclerAdapter.create(CONVERTER.setJsonData(response));
                        mAdapter.setOnLoadMoreListener(RefreshHandler1.this, RECYCLER_VIEW);
                        RECYCLER_VIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .build()
                .post();
    }
}
