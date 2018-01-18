package com.labelwall.mywall.main.index;

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
        parentDelegate.start(new CreateTopicDelegate());
        /*CallbackManager.getInstance().addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
            @Override
            public void executeCallback(Uri args) {
                Log.e("剪裁的文件路径：", args.toString());
            }
        });
        //照片选择
        startCameraWithCheck();*/
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        converterData();
        onPageChange();
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
        final int size = mTitles.size();
        for (int i = 0; i < size; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles.get(i)));
        }
        final List<WallDelegate> delegates = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            delegates.add(new IndexListDelegate());
        }
        IndexListDelegateAdapter delegateAdapter =
                IndexListDelegateAdapter.create(getFragmentManager(), delegates, mTitles);
        mViewPager.setAdapter(delegateAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(delegateAdapter);
    }

    private void onPageChange() {
        /*滑动监听器OnPageChangeListener
            onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
                当页面在活动的时候会调用此方法，在滑动被停止之前，此方法一直被调用
                position：当前页面
                positionOffest:当前页面偏移的百分比
                positionOffsetPixels：当前页面偏移的像素位置
            onPageSelected(int position)
                页面跳转完成后被调用
                position:当前选中的页面编号
            onPageScrollStateChange(int state)
                此方法实在状态改变的时候调用
                state:
                    0：表示什么也没做
                    1：表示正在滑动
                    2：表示滑动完毕
        */
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("onPageScrolled: ", "postion:" + position + ",positionOffset:" +
                        positionOffset + ",positionOffsetPixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("onPageSelected:", "position:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
                    Log.e("StateChanged", "state-->0");
                } else if (state == 1) {
                    Log.e("StateChanged", "state-->1");
                } else if (state == 2) {
                    Log.e("StateChanged", "state--->2");
                }
            }
        });
    }
}
