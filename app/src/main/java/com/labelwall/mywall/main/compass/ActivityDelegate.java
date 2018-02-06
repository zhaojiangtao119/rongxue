package com.labelwall.mywall.main.compass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-01-04.
 */

public class ActivityDelegate extends BottomItemDelegate {

    @BindView(R2.id.srl_activity_list)
    SwipeRefreshLayout mRefreshLayout = null;
    @BindView(R2.id.rv_activity_list)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.tv_activity_my)
    AppCompatTextView mMyActivityList;
    @BindView(R2.id.icon_activity_add)
    IconTextView mAddActivity;

    private ActivityRefreshHandler mRefreshHandler = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        WallDelegate wallDelegate = getParentDelegate();
        mRefreshHandler = ActivityRefreshHandler.create(mRefreshLayout, mRecyclerView, new ActivityDataConverter(),wallDelegate);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        initRecyclerView();
        mRefreshHandler.firshActivityPage();
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright
        );
        mRefreshLayout.setProgressViewOffset(true, 80, 100);
    }
}
