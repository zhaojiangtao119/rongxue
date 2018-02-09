package com.labelwall.mywall.main.compass.detail.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.compass.detail.ActivityJoinUserAdapter;
import com.labelwall.mywall.main.compass.detail.ActivityJoinUserDataConverter;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-02-08.
 */

public class ActivityApplyJoinUserDelegate extends WallDelegate {

    @BindView(R2.id.rv_activity_join_user)
    RecyclerView mRecyclerView = null;
    private ActivityApplyJoinUserAdapter mAdapter = null;
    private ActivityApplyJoinUserDataConverter mConverter = new ActivityApplyJoinUserDataConverter();

    private static final String ARG_APPLY_JOIN = "ARG_APPLY_JOIN";
    private String mApplyJoinUserData = null;
    private static final String ARF_ACTIVITY_ID = "ARF_ACTIVITY_ID";
    private Integer mActivityId = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mApplyJoinUserData = args.getString(ARG_APPLY_JOIN);
            mActivityId = args.getInt(ARF_ACTIVITY_ID);
        }
    }

    public static ActivityApplyJoinUserDelegate create(String data, Integer activityId) {
        final Bundle args = new Bundle();
        args.putString(ARG_APPLY_JOIN, data);
        args.putInt(ARF_ACTIVITY_ID, activityId);
        final ActivityApplyJoinUserDelegate delegate = new ActivityApplyJoinUserDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_join_user;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ActivityApplyJoinUserAdapter(mConverter.setJsonData(mApplyJoinUserData).convert(),mActivityId);
        mRecyclerView.setAdapter(mAdapter);
    }
}
