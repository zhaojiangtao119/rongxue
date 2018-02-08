package com.labelwall.mywall.main.compass.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.compass.ActivityAdapter;
import com.labelwall.mywall.main.compass.ActivityDataConverter;
import com.labelwall.mywall.main.compass.detail.ActivityDetailDelegate;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-02-08.
 */

public class ActivityMyStartJoinDelegate extends WallDelegate {


    @BindView(R2.id.rv_activity_my_start_join)
    RecyclerView mRecyclerView = null;
    private ActivityAdapter mAdapter = null;
    private ActivityDataConverter mConverter = new ActivityDataConverter();

    private static final String ARG_MY_DATA = "ARG_MY_DATA";
    private String mPagerData = null;
    private static ActivityMyDelegate mActivityMyDelegate;

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_start_join;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mPagerData = args.getString(ARG_MY_DATA);
        }
    }

    public static ActivityMyStartJoinDelegate create(String data, ActivityMyDelegate activityMyDelegate) {
        mActivityMyDelegate = activityMyDelegate;
        final Bundle args = new Bundle();
        args.putString(ARG_MY_DATA, data);
        final ActivityMyStartJoinDelegate delegate = new ActivityMyStartJoinDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ActivityAdapter(mConverter.setJsonData(mPagerData).convert(),
                mActivityMyDelegate);
        mRecyclerView.setAdapter(mAdapter);
    }
}
