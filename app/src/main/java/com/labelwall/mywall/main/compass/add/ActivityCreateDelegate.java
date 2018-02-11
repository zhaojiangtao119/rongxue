package com.labelwall.mywall.main.compass.add;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.UserClickListener;
import com.labelwall.mywall.main.user.UserSettingItem;
import com.labelwall.mywall.main.user.address.AdressDelegate;
import com.labelwall.mywall.main.user.list.ListAdapter;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.main.user.list.ListItemType;
import com.labelwall.mywall.main.user.profile.UserProfileDelegate;
import com.labelwall.mywall.main.user.settings.SettingsDelegate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-02-10.
 * 创建活动
 */

public class ActivityCreateDelegate extends WallDelegate {

    @BindView(R2.id.rv_create_activity_info)
    RecyclerView mRecyclerView = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_create_activity;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initSetList();//设置条目信息
    }

    private void initSetList() {
        final ListBean applyStartTime = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.APPLY_START_TIME)
                .setText(ActivityCreateInfoItem.APPLY_START_TIME_VALUE)
                .build();
        final ListBean applyEntTime = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.APPLY_END_TIME)
                .setText(ActivityCreateInfoItem.APPLY_END_TIME_VALUE)
                .build();
        final ListBean activityStartTime = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.ACTIVITY_START_TIME)
                .setText(ActivityCreateInfoItem.ACTIVITY_START_TIME_VALUE)
                .setDelegate(new SettingsDelegate())
                .build();
        final ListBean activityEndTime = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.ACTIVITY_END_TIME)
                .setText(ActivityCreateInfoItem.ACTIVITY_END_TIME_VALUE)
                .setDelegate(new SettingsDelegate())
                .build();
        final List<ListBean> data = new ArrayList<>();
        data.add(applyStartTime);
        data.add(applyEntTime);
        data.add(activityStartTime);
        data.add(activityEndTime);

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRecyclerView.setAdapter(adapter);
        //设置监听事件
        mRecyclerView.addOnItemTouchListener(new ActivityCreateClickListener(this));
    }
}
