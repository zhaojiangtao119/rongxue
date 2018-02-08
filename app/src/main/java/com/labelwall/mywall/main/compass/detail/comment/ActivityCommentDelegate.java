package com.labelwall.mywall.main.compass.detail.comment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.labelwall.mywall.delegates.base.WallDelegate;

/**
 * Created by Administrator on 2018-02-07.
 */

public class ActivityCommentDelegate extends WallDelegate {

    private static final String ARG_COMMENT_DATA = "ARG_COMMENT_DATA";
    private String mCommentData = null;

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
        return null;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }
}
