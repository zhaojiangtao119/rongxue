package com.labelwall.mywall.main.compass.detail.comment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.ui.recycler.BaseDecoration;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-02-07.
 */

public class ActivityCommentDelegate extends WallDelegate {

    private static final String ARG_COMMENT_DATA = "ARG_COMMENT_DATA";
    private String mCommentData = null;

    @BindView(R2.id.rv_activity_comment)
    RecyclerView mRecyclerView = null;

    private ActivityCommentDataConverter mConverter = new ActivityCommentDataConverter();
    private ActivityCommentAdapter mAdapter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mCommentData = args.getString(ARG_COMMENT_DATA);
        }
    }

    public static ActivityCommentDelegate create(String commentData) {
        final Bundle args = new Bundle();
        args.putString(ARG_COMMENT_DATA, commentData);
        final ActivityCommentDelegate delegate = new ActivityCommentDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_comment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(
                BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_background), 5));
        mAdapter = new ActivityCommentAdapter(mConverter.setJsonData(mCommentData).convert());
        mRecyclerView.setAdapter(mAdapter);
    }
}
