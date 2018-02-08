package com.labelwall.mywall.main.compass.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-02-07.
 */

public class ActivityJoinUserNullDelegate extends WallDelegate {

    @BindView(R2.id.tv_activity_no_user)
    AppCompatTextView mTextView = null;
    private String hintMessage = null;

    private static final String HINT_MSG = "HINT_MSG";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            hintMessage = args.getString(HINT_MSG);
        }
    }

    public static ActivityJoinUserNullDelegate create(String hintMessge) {
        final Bundle args = new Bundle();
        args.putString(HINT_MSG, hintMessge);
        final ActivityJoinUserNullDelegate delegate = new ActivityJoinUserNullDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_join_user_null;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTextView.setText(hintMessage);
    }
}
