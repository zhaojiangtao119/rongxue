package com.labelwall.mywall.main.index.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.index.detail.reply.TopicReplyHandler;
import com.labelwall.mywall.main.index.usertopic.UserTopicDelegate;
import com.labelwall.mywall.ui.recycler.BaseDecoration;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.refresh.PagingBean;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Administrator on 2018-01-09.
 */

public class TopicDetailDelegate extends WallDelegate {

    private int mTopicId = -1;
    private int mUserId = -1;
    private PagingBean pagingBean = new PagingBean();
    private TopicReplyAdapter mAdapter = null;
    private TopicReplyDataConverter converter = new TopicReplyDataConverter();
    private TopicDetailRefreshHandler mRefreshHandler = null;


    @BindView(R2.id.user_info)
    LinearLayoutCompat mLinearLayoutCompat;
    @BindView(R2.id.user_name)
    AppCompatTextView mUsername;
    @BindView(R2.id.user_avatar)
    AppCompatImageView mUserAvatar;
    @BindView(R2.id.topic_title)
    AppCompatTextView mTopicTitle;
    @BindView(R2.id.topic_image)
    AppCompatImageView mTopicImage;
    @BindView(R2.id.topic_create_time)
    AppCompatTextView mCreateTime;
    @BindView(R2.id.topic_reply_num)
    AppCompatTextView mTopicReplyNum;
    @BindView(R2.id.topic_like_num)
    AppCompatTextView mTopicLikeNum;

    @BindView(R2.id.srl_topic_reply)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R2.id.rv_topic_reply)
    RecyclerView mRecyclerView;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    private TopicReplyHandler mTopicReplyHandler = null;

    @OnClick(R2.id.tv_add_topic_reply)
    void createTopicReply() {
        //跳转到创建回复页面，或者使用dialog对话框
        //创建dialog
        mTopicReplyHandler.beginTopicReplyDialog();
    }

    @OnClick(R.id.user_info)
    void onClick() {
        if (mUserId != -1) {
            start(UserTopicDelegate.create(mUserId));
        }
    }

    private TopicDetailDelegate(int topicId) {
        this.mTopicId = topicId;

    }

    public static TopicDetailDelegate create(int mTopicId) {
        return new TopicDetailDelegate(mTopicId);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_topic_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        RestClient.builder()
                .url("topic/get_topic_post_by_id/" + mTopicId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        bindData(response);
                    }
                })
                .build()
                .get();
        mTopicReplyHandler = new TopicReplyHandler(this);
        mRefreshHandler = TopicDetailRefreshHandler.create(mRecyclerView, mRefreshLayout,
                new TopicReplyDataConverter(), mTopicId, mTopicReplyHandler);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView();
        initRefreshLayout();
        mRefreshHandler.onLoadFirstTopicReply();
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        //设置分割线
        mRecyclerView.addItemDecoration(BaseDecoration.
                create(ContextCompat.getColor(getContext(), R.color.app_background), 5));
        //设置单击监听
        mRecyclerView.addOnItemTouchListener(TopicDetailClickListener.create(this));
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mRefreshLayout.setProgressViewOffset(true, 80, 100);
    }

    //设置进入的动画
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();//水平打开进入
    }

    private void bindData(String data) {
        final JSONObject topic = JSON.parseObject(data).getJSONObject("data");
        final JSONObject user = topic.getJSONObject("userDto");
        Glide.with(_mActivity)
                .load(user.getString("head"))
                .apply(OPTIONS)
                .into(mUserAvatar);
        mUsername.setText(user.getString("username"));
        mTopicTitle.setText(topic.getString("title"));
        Glide.with(_mActivity)
                .load(topic.getString("image"))
                .apply(OPTIONS)
                .into(mTopicImage);
        mTopicReplyNum.setText(String.valueOf(topic.getInteger("replyNum")));
        mTopicLikeNum.setText(String.valueOf(topic.getInteger("likeNum")));
        mCreateTime.setText(topic.getString("createTimeStr"));
        mUserId = user.getInteger("id");
        mLinearLayoutCompat.setTag(mUserId);
    }

}
