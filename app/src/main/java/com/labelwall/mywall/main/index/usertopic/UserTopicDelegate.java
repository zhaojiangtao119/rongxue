package com.labelwall.mywall.main.index.usertopic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.index.IndexDataConverter;
import com.labelwall.mywall.main.index.IndexItemClickListener;
import com.labelwall.mywall.ui.recycler.BaseDecoration;
import com.labelwall.mywall.ui.recycler.DividerLookupImpl;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerAdapter;
import com.labelwall.mywall.ui.refresh.PagingBean;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import butterknife.BindView;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import retrofit2.http.OPTIONS;

/**
 * Created by Administrator on 2018-01-10.
 * 指定用户所发表的帖子
 */

public class UserTopicDelegate extends WallDelegate {

    private int mUserId = -1;

    @BindView(R2.id.user_avatar)
    AppCompatImageView mUserAvatar;
    @BindView(R2.id.user_name)
    AppCompatTextView mUsername;
    @BindView(R2.id.user_school)
    AppCompatTextView mUserSchool;
    @BindView(R2.id.user_location)
    AppCompatTextView mUserLocation;
    @BindView(R2.id.user_join_time)
    AppCompatTextView mUserJoinTime;

    @BindView(R2.id.srl_user_topic)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R2.id.rv_user_topic_item)
    RecyclerView mRecyclerView;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    private UserTopicRefreshHandler mRefreshHandler = null;

    public UserTopicDelegate(int userId) {
        this.mUserId = userId;
    }

    public static UserTopicDelegate create(int userId) {
        return new UserTopicDelegate(userId);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView();
        initRefreshLayout();
        mRefreshHandler.onUploadUserTopic();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright
        );
        mRefreshLayout.setProgressViewOffset(true, 80, 100);
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(
                BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_background), 5));
        //设置Item的点击监听
        mRecyclerView.addOnItemTouchListener(IndexItemClickListener.create(this));
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_user_topic1;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        RestClient.builder()
                .url("user/get_user_info/" + mUserId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        bindData(response);
                    }
                })
                .build()
                .get();
        mRefreshHandler = UserTopicRefreshHandler.create(mRefreshLayout,mRecyclerView,
                mUserId,new IndexDataConverter());

    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }


    private void bindData(String response) {
        final JSONObject user = JSON.parseObject(response).getJSONObject("data");
        if (user != null) {
            Glide.with(_mActivity)
                    .load(user.getString("head"))
                    .apply(OPTIONS)
                    .into(mUserAvatar);
            mUsername.setText(user.getString("username"));
            mUserSchool.setText(user.getString("schoolName"));
            mUserJoinTime.setText(user.getString("createTimeStr"));
            String location = user.getString("locationProvince") + " "
                    + user.getString("locationCity") + " " + user.getString("locationCounty");
            mUserLocation.setText(location);
        }
    }
}
