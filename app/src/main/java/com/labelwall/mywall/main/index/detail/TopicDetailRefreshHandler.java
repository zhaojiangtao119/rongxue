package com.labelwall.mywall.main.index.detail;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.labelwall.mywall.delegates.launcher.ScrollLauncherTag;
import com.labelwall.mywall.main.index.detail.reply.TopicReplyHandler;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.refresh.PagingBean;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-01-16.
 * TopicDetailRefresh的处理类
 */

public class TopicDetailRefreshHandler implements
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private final RecyclerView RECYCLER_VIEW;
    private final PagingBean BEAN;
    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final TopicReplyDataConverter CONVERTER;
    private final int TOPIC_ID;
    private TopicReplyAdapter mAdapter = null;
    private final TopicReplyHandler REPLY_HANDLER;

    private TopicDetailRefreshHandler(RecyclerView recyclerView, SwipeRefreshLayout refreshLayout,
                                      TopicReplyDataConverter converter, PagingBean bean, int topicId,
                                      TopicReplyHandler replyHandler) {
        this.RECYCLER_VIEW = recyclerView;
        this.REFRESH_LAYOUT = refreshLayout;
        this.CONVERTER = converter;
        this.BEAN = bean;
        this.TOPIC_ID = topicId;
        this.REPLY_HANDLER = replyHandler;
        REFRESH_LAYOUT.setOnRefreshListener(this);
        onSubmitTopicReplyListener();
    }

    private void onSubmitTopicReplyListener() {
        REPLY_HANDLER.setSubmitTopicReplyListener(new TopicReplyHandler.ISubmitTopicReplyListener() {
            @Override
            public void submitTopicReply(String content) {
                onSubmitTopicReply(content);
            }
        });
    }

    private void onSubmitTopicReply(String content) {
        final long userId = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
        RestClient.builder()
                .url("app/topic/app_publish_post_reply")
                .params("userId", userId)
                .params("topicPostId", TOPIC_ID)
                .params("content", content)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //TODO 返回的信息处理
                        Log.e("返回的信息：", response.toString());
                    }
                })
                .build()
                .post();
    }

    public static TopicDetailRefreshHandler create(RecyclerView recyclerView, SwipeRefreshLayout refreshLauout,
                                                   TopicReplyDataConverter converter, int topicId,
                                                   TopicReplyHandler replyHandler) {
        return new TopicDetailRefreshHandler(recyclerView, refreshLauout, converter,
                new PagingBean(), topicId, replyHandler);
    }

    public void onLoadFirstTopicReply() {
        RestClient.builder()
                .url("topic/get_topic_reply_by_post_id/" + TOPIC_ID + "/" + BEAN.getPageIndex() + "/1")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter = TopicReplyAdapter.create(CONVERTER.setJsonData(response));
                        //mAdapter.setOnLoadMoreListener(TopicDetailDelegate.this, mRecyclerView);
                        RECYCLER_VIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .build()
                .get();
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onRefresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        uploadRefreshTopicReply();
    }

    private void uploadRefreshTopicReply() {
        RestClient.builder()
                .url("topic/get_topic_reply_by_post_id/" + TOPIC_ID + "/" + BEAN.getPageIndex() + "/1")
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
                .get();
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
