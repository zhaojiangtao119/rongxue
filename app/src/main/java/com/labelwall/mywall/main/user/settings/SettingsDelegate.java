package com.labelwall.mywall.main.user.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.UserSettingItem;
import com.labelwall.mywall.main.user.list.ListAdapter;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.main.user.list.ListItemType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Administrator on 2018-01-18.
 */

public class SettingsDelegate extends WallDelegate {

    @BindView(R2.id.rv_settings)
    RecyclerView mRecyclerView = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_settings;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        final ListBean systemPush = new ListBean.builder()
                .setId(1)
                .setText("消息推送")
                .setItemType(ListItemType.ITEM_SWITCH)
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            //打开的状态
                            Toast.makeText(_mActivity, "打开推送", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(_mActivity, "关闭推送", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build();
        final ListBean aboutApp = new ListBean.builder()
                .setId(2)
                .setText("关于")
                .setItemType(ListItemType.ITEM_NORMAL_NO_HINT)
                .build();
        final List<ListBean> data = new ArrayList<>();
        data.add(systemPush);
        data.add(aboutApp);

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new SettingsClickListener());
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
