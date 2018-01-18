package com.labelwall.mywall.main.index.usertopic;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.main.index.IndexDataConverter;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerAdapter;
import com.labelwall.mywall.ui.refresh.PagingBean;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-01-17.
 * 用户的帖子的RefreshHandler
 */

public class UserTopicRefreshHandler implements SwipeRefreshLayout.OnRefreshListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final RecyclerView RECYCLER_VIEW;
    private final PagingBean BEAN;
    private final int USER_ID;
    private final IndexDataConverter CONVERTER;
    private MultipleRecyclerAdapter mAdapter = null;

    public UserTopicRefreshHandler(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView,
                                   PagingBean bean, int userId, IndexDataConverter converter) {
        this.REFRESH_LAYOUT = refreshLayout;
        this.RECYCLER_VIEW = recyclerView;
        this.BEAN = bean;
        this.USER_ID = userId;
        this.CONVERTER = converter;
        REFRESH_LAYOUT.setOnRefreshListener(this);
    }

    public static UserTopicRefreshHandler create(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView,
                                                 int userId, IndexDataConverter converter) {
        return new UserTopicRefreshHandler(refreshLayout, recyclerView, new PagingBean(), userId, converter);
    }

    public void onUploadUserTopic() {
        RestClient.builder()
                .url("topic/get_topic_post_list/" + BEAN.getPageIndex() + "/3")
                .params("userId", USER_ID)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter = MultipleRecyclerAdapter.create(CONVERTER.setJsonData(response));
                        RECYCLER_VIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .build()
                .post();
    }

    @Override
    public void onRefresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        uploadRefreshUserTopic();
    }

    private void uploadRefreshUserTopic() {
        RestClient.builder()
                .url("topic/get_topic_post_list/" + BEAN.getPageIndex() + "/2")
                .params("userId", USER_ID)
                .refreshLayout(REFRESH_LAYOUT)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject pageInfo = JSON.parseObject(response).getJSONObject("data");
                        List<MultipleItemEntity> newData = converterData(response);
                        CONVERTER.clearData();
                        mAdapter.addData(newData);
                       /* pagingBean.setPageSize(pageInfo.getInteger("pageSize"));
                        pagingBean.setCurrentCount(mAdapter.getData().size());*/
                        BEAN.addIndex();
                    }
                })
                .build()
                .post();
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
}
