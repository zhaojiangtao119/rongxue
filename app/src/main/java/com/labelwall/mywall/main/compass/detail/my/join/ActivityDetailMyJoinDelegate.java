package com.labelwall.mywall.main.compass.detail.my.join;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.compass.detail.ActivityTabPagerAdapter;
import com.labelwall.mywall.main.compass.detail.my.ActivityDetailInfoMyDelegate;
import com.labelwall.mywall.main.compass.detail.my.ActivityMyTabPagerAdapter;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import butterknife.BindView;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Administrator on 2018-02-06.
 */

public class ActivityDetailMyJoinDelegate extends WallDelegate implements AppBarLayout.OnOffsetChangedListener {

    @BindView(R2.id.activity_detail_toolbar)
    Toolbar mToobar = null;
    @BindView(R2.id.tab_layout)
    TabLayout mTabLayout = null;
    @BindView(R2.id.view_pager)
    ViewPager mViewPage = null;
    @BindView(R2.id.detail_activity_poster)
    AppCompatImageView mActivityPoster = null;
    @BindView(R2.id.collapsing_toolbar_detail)
    CollapsingToolbarLayout mCollapsingToolbarLayout = null;
    @BindView(R2.id.app_bar_detail)
    AppBarLayout mAppBar = null;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .centerCrop();

    private Integer mActivityId = -1;
    private static final String ARG_ACTIVITY_ID = "ARG_ACTIVITY_ID";

    private JSONObject mCommentData = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mActivityId = args.getInt(ARG_ACTIVITY_ID);
        }
    }

    public static ActivityDetailMyJoinDelegate create(@NonNull Integer activityId) {
        final Bundle args = new Bundle();
        args.putInt(ARG_ACTIVITY_ID, activityId);
        final ActivityDetailMyJoinDelegate delegate = new ActivityDetailMyJoinDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        //折叠toolbar设置变换的颜色
        mCollapsingToolbarLayout.setContentScrimColor(Color.WHITE);
        //设置滚动的事件监听
        mAppBar.addOnOffsetChangedListener(this);
        //加载服务端activity数据
        initActivityComment();
        initTabLayout();
    }

    private void initActivityComment() {
        RestClient.builder()//加载评论
                .url("activity/comment/" + mActivityId + "/1/20")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject pageInfo = JSON.parseObject(response).getJSONObject("data");
                        mCommentData = pageInfo;
                        initData();
                    }
                })
                .build()
                .get();
    }

    private void initData() {
        RestClient.builder()
                .url("activity/" + mActivityId)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject activityDto =
                                JSON.parseObject(response).getJSONObject("data");
                        //初始化数据
                        initActivityPoster(activityDto);
                        initActivityInfo(activityDto);
                        initViewPager(activityDto);

                    }
                })
                .build()
                .get();
    }

    private void initActivityInfo(JSONObject activityDto) {
        final String activityInfo = activityDto.toString();
        getSupportDelegate().loadRootFragment(R.id.frame_activity_info,
                ActivityDetailInfoMyJoinDelegate.create(activityInfo, mActivityId));
    }

    private void initActivityPoster(JSONObject activityDto) {
        //初始化活动的poster
        final String posterUrl = activityDto.getString("posterURL");
        /*Glide.with(_mActivity)
                        .load(posterUrl)
                        .apply(OPTIONS)
                        .into(mActivityPoster);*/
    }

    private void initTabLayout() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//tab平均分开
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.app_title));
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setupWithViewPager(mViewPage);
    }

    private void initViewPager(JSONObject activityDto) {
        final ActivityTabPagerAdapter adapter =
                new ActivityTabPagerAdapter(getFragmentManager(), mActivityId, activityDto, mCommentData);
        mViewPage.setAdapter(adapter);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }
}
