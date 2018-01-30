package com.labelwall.mywall.main.index;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.main.WallBottomDelegate;
import com.labelwall.mywall.main.index.topic.CreateTopicDelegate;
import com.labelwall.mywall.ui.recycler.BaseDecoration;
import com.labelwall.mywall.ui.refresh.RefreshHandler;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-01-04.
 * 默认主页
 */

public class IndexDelegate extends BottomItemDelegate {

    @BindView(R.id.index_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.index_viewpager)
    ViewPager mViewPager;
    private List<String> mTitles = new ArrayList<>();
    private LinkedHashMap<Integer, String> mCategory = new LinkedHashMap<>();
    @BindView(R2.id.tv_add_topic)
    TextView mTextView;

    @OnClick(R2.id.tv_add_topic)
    void onClickAddTopic() {
        WallDelegate parentDelegate = getParentDelegate();
        parentDelegate.getSupportDelegate().start(new CreateTopicDelegate());
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        converterData();
    }

    private void converterData() {
        RestClient.builder()
                .url("topic/get_category")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONArray classifyArray = JSON.parseObject(response).getJSONArray("data");
                        final int size = classifyArray.size();
                        for (int i = 0; i < size; i++) {
                            JSONObject classify = classifyArray.getJSONObject(i);
                            final Integer id = classify.getInteger("id");
                            final String name = classify.getString("name");
                            mCategory.put(id, name);
                            mTitles.add(name);
                        }
                        createTabControl();
                    }
                })
                .build()
                .get();
    }

    private void createTabControl() {
        initTabLayout();
        final int size = mTitles.size();
        for (int i = 0; i < 3; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles.get(i)));
        }
        final List<WallDelegate> delegates = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            delegates.add(new IndexListDelegate());
        }
        IndexListDelegateAdapter delegateAdapter =
                IndexListDelegateAdapter.create(getFragmentManager(), delegates, mTitles);
        mViewPager.setAdapter(delegateAdapter);
    }

    private void initTabLayout() {
        //tab title 平均分开
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //设置选择线的颜色
        mTabLayout.setSelectedTabIndicatorColor(Color.BLACK);
        //设置 tabTitle的字体颜色
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        //设置tab的背景色
        //mTabLayout.setBackgroundColor(ContextCompat.getColor(_mActivity,R.color.app_title));
        //关联viewPage
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
