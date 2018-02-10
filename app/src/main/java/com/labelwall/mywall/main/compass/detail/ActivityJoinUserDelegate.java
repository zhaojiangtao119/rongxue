package com.labelwall.mywall.main.compass.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-02-07.
 */

public class ActivityJoinUserDelegate extends WallDelegate {

    private static final String ARG_USERS_DATA = "ARG_USERS_DATA";
    private String mData = null;
    private static final String ARG_ACTIVITY_ID = "ARF_ACTIVITY_ID";
    private Integer mActivityId = null;
    private static final String ARG_ITEM_TYPE = "ARG_ITEM_TYPE";
    private int mItemType = 0;

    @BindView(R2.id.rv_activity_join_user)
    RecyclerView mRecyclerView = null;
    private ActivityJoinUserAdapter mAdapter = null;
    private ActivityJoinUserDataConverter mConverter = new ActivityJoinUserDataConverter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mData = args.getString(ARG_USERS_DATA);
            mActivityId = args.getInt(ARG_ACTIVITY_ID);
            mItemType = args.getInt(ARG_ITEM_TYPE);
        }
    }

    public static ActivityJoinUserDelegate create(String data, Integer activityId, int itemType) {
        final Bundle args = new Bundle();
        args.putString(ARG_USERS_DATA, data);
        args.putInt(ARG_ACTIVITY_ID, activityId);
        args.putInt(ARG_ITEM_TYPE, itemType);
        final ActivityJoinUserDelegate activityJoinUserDelegate = new ActivityJoinUserDelegate();
        activityJoinUserDelegate.setArguments(args);
        return activityJoinUserDelegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_join_user;
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ActivityJoinUserAdapter(
                mConverter.setItemType(mItemType).setJsonData(mData).convert(), mActivityId);
        mRecyclerView.setAdapter(mAdapter);
    }
}
