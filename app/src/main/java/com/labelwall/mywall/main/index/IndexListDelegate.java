package com.labelwall.mywall.main.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.WallBottomDelegate;
import com.labelwall.mywall.ui.recycler.BaseDecoration;
import com.labelwall.mywall.ui.refresh.RefreshHandler;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-01-11.
 */

public class IndexListDelegate extends WallDelegate {

    @BindView(R2.id.rv_index)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.srl_index)
    SwipeRefreshLayout mRefreshLayout = null;
    private RefreshHandler mRefreshHandler = null;

    //初始化refresh
    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
    }

    //初始化RecyclerView
    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        //1.设置布局管理器
        mRecyclerView.setLayoutManager(manager);
        //2.设置分割线
        mRecyclerView.addItemDecoration(BaseDecoration.
                create(ContextCompat.getColor(getContext(), R.color.app_background), 5));
        //3.设置Item增加删除时的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //4.设置Item的点击事件 获取父级delegate
        final WallBottomDelegate wallBottomDelegate = getParentDelegate();
        mRecyclerView.addOnItemTouchListener(IndexItemClickListener.create(wallBottomDelegate));
        //5.设置尺寸？？
        mRecyclerView.setHasFixedSize(true);
    }

    //懒加载
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
//        initRefreshLayout();
//        initRecyclerView();
//        mRefreshHandler.firstPage();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index_topic_list;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mRefreshHandler = RefreshHandler.create(mRefreshLayout, mRecyclerView, new IndexDataConverter());
        initRefreshLayout();
        initRecyclerView();
        mRefreshHandler.firstPage();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        initRefreshLayout();
        initRecyclerView();
        mRefreshHandler.firstPage();
        //mRefreshHandler = RefreshHandler.create(mRefreshLayout, mRecyclerView, new IndexDataConverter());
    }
}
