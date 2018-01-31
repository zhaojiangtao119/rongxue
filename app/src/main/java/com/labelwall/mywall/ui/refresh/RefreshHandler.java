package com.labelwall.mywall.ui.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.index.IndexDataConverter;
import com.labelwall.mywall.main.index.TopicField;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerAdapter;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-01-06.
 * 下拉刷新的handler
 * <p>
 * RecyclerView
 * Adapter
 * DataConverter
 * PageBean
 */

public class RefreshHandler implements SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final PagingBean BEAN;
    private final RecyclerView RECYCLERVIEW;
    private MultipleRecyclerAdapter mAdapter = null;
    private final DataConverter CONVERTER;

    private int mLastVisibleItem = -1;

    private RefreshHandler(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView,
                           DataConverter dataConverter, PagingBean pageBean) {
        this.REFRESH_LAYOUT = refreshLayout;
        this.BEAN = pageBean;
        this.RECYCLERVIEW = recyclerView;
        this.CONVERTER = dataConverter;
        //监听refresh操作
        refreshLayout.setOnRefreshListener(this);
    }

    public static RefreshHandler create(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView,
                                        DataConverter dataConverter) {
        return new RefreshHandler(refreshLayout, recyclerView, dataConverter, new PagingBean());
    }

    //下拉加载
    @Override
    public void onRefresh() {
        REFRESH_LAYOUT.setRefreshing(true);//开始加载
        uploadRefreshTopic();
    }

    //上拉加载
    @Override
    public void onLoadMoreRequested() {
        //TODO uploadMoreTopic();
        //uploadMoreTopic();
    }

    public void firstPage() {
        RestClient.builder()
                .url("topic/get_topic_post_list/" + BEAN.getPageIndex() + "/6")
                .refreshLayout(REFRESH_LAYOUT)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject pageInfo = JSON.parseObject(response).getJSONObject("data");
                        BEAN.setTotal(pageInfo.getInteger("total"))
                                .setPageSize(pageInfo.getInteger("pageSize"));
                        mAdapter = MultipleRecyclerAdapter.create(CONVERTER.setJsonData(response));
                        //上拉加载更多的监听
                        mAdapter.setOnLoadMoreListener(RefreshHandler.this, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .build()
                .post();
    }

    private void uploadRefreshTopic() {
        final int pageSize = BEAN.getPageSize();
        final int currentCount = BEAN.getCurrentCount();
        final int total = BEAN.getTotal();
        final int index = BEAN.getPageIndex();

        if (mAdapter.getItemCount() < pageSize || currentCount >= total) {
            mAdapter.loadMoreEnd(true);
        } else {
            RestClient.builder()
                    .url("topic/get_topic_post_list/" + BEAN.getPageIndex() + "/2")
                    .refreshLayout(REFRESH_LAYOUT)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            final JSONObject pageInfo = JSON.parseObject(response).getJSONObject("data");
                            List<MultipleItemEntity> newData = converterData(response);
                            CONVERTER.clearData();
                            mAdapter.addData(newData);
                            BEAN.setPageSize(pageInfo.getInteger("pageSize"));
                            BEAN.setCurrentCount(mAdapter.getData().size());
                            BEAN.addIndex();
                            Log.e("数量：", mAdapter.getItemCount() + "");
                        }
                    })
                    .build()
                    .post();
        }
    }

    private List<MultipleItemEntity> converterData(String response) {
        final JSONObject pageInfo = JSON.parseObject(response).getJSONObject("data");
        List<MultipleItemEntity> oldData = new ArrayList<>();
        oldData.addAll(CONVERTER.getItemList());
        CONVERTER.clearData();
        List<MultipleItemEntity> newData = new ArrayList<>();
        newData.addAll(CONVERTER.setJsonData(response).convert());
        //将下拉刷新的新数据添加到与adapter关联的List集合的最前边
        oldData.addAll(0, newData);
        return oldData;
    }


    private void uploadMoreTopic() {
        final int pageSize = BEAN.getPageSize();
        final int currentCount = BEAN.getCurrentCount();
        final int total = BEAN.getTotal();
        final int index = BEAN.getPageIndex();

        if (mAdapter.getItemCount() < pageSize || currentCount >= total) {
            mAdapter.loadMoreEnd(true);
        } else {
            RestClient.builder()
                    .url("topic/get_topic_post_list/" + index + "/2")
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            final JSONObject pageInfo = JSON.parseObject(response).getJSONObject("data");
                            List<MultipleItemEntity> newData = converterData(response);
                            CONVERTER.clearData();
                            mAdapter.addData(newData);
                            //累加数量
                            BEAN.setCurrentCount(mAdapter.getData().size());
                            mAdapter.loadMoreComplete();
                            BEAN.addIndex();
                        }
                    })
                    .build()
                    .post();
        }
    }
}
