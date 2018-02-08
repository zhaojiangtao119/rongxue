package com.labelwall.mywall.main.compass.my;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-02-08.
 */

public class ActivityMyDelegate extends WallDelegate {

    @BindView(R2.id.my_activity_tabs)
    TabLayout mTabLayout = null;
    @BindView(R2.id.my_activity_viewpager)
    ViewPager mViewPager = null;

    private final static long USER_ID =
            WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private String mStartActivityData = null;
    private String mJoinActivityData = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_my;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initTabLayout();
        //加载数据
        uploadStartActivityData();
    }

    private void uploadStartActivityData() {
        RestClient.builder()
                .url("activity/user/start/" + USER_ID + "/1/10")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mStartActivityData = response;
                        uploadJoinActivityData();
                    }
                })
                .build()
                .get();
    }

    private void uploadJoinActivityData() {
        RestClient.builder()
                .url("activity/user/join/" + USER_ID + "/1/10")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mJoinActivityData = response;
                        initViewPager();
                    }
                })
                .build()
                .get();
    }

    private void initViewPager() {
        final ActivityMyTabPagerAdapter adapter =
                new ActivityMyTabPagerAdapter(getFragmentManager(),
                        mStartActivityData, mJoinActivityData,this);
        mViewPager.setAdapter(adapter);
    }

    private void initTabLayout() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
