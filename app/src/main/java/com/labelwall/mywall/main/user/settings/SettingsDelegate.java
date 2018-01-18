package com.labelwall.mywall.main.user.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-01-18.
 */

public class SettingsDelegate extends WallDelegate {

    @BindView(R2.id.rv_settings)
    RecyclerView mRecyclerView;

    @Override
    public Object setLayout() {
        return R.layout.delegate_settings;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
