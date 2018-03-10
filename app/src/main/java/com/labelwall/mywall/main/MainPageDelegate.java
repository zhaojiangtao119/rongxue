package com.labelwall.mywall.main;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-03-09.
 */

public class MainPageDelegate extends WallDelegate implements View.OnClickListener {

    @BindView(R2.id.rv_main_page)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.tv_main_type)
    AppCompatTextView mMenu = null;

    @OnClick(R2.id.tv_main_type)
    void onClickMainType() {
        initDialog();
    }

    private AlertDialog mDialog = null;
    private MainPageAdapter mAdapter = null;
    private MainPageDataConver mDataconver = new MainPageDataConver();

    @Override
    public Object setLayout() {
        return R.layout.delegate_main_page;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mDialog = new AlertDialog.Builder(this.getContext()).create();
        initRecyclerView();
        initData();
    }

    private void initData() {

    }

    private void initRecyclerView() {
        final StaggeredGridLayoutManager manager =
                new StaggeredGridLayoutManager(8, GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new MainPageAdapter(mDataconver.convert());
        mRecyclerView.setAdapter(mAdapter);
        //设置颜色分割线
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
    }

    private void initDialog() {
        mDialog.show();
        final Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_main_type);
            window.setGravity(Gravity.CENTER);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            window.findViewById(R.id.tv_topic).setOnClickListener(this);
            window.findViewById(R.id.tv_product).setOnClickListener(this);
            window.findViewById(R.id.tv_shopcart).setOnClickListener(this);
            window.findViewById(R.id.tv_activity).setOnClickListener(this);
            window.findViewById(R.id.tv_user_profile).setOnClickListener(this);
            window.findViewById(R.id.tv_6).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.tv_topic:
                Toast.makeText(_mActivity, "点击了1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_product:
                Toast.makeText(_mActivity, "点击了2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_shopcart:
                Toast.makeText(_mActivity, "点击了3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_activity:
                Toast.makeText(_mActivity, "点击了4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_user_profile:
                Toast.makeText(_mActivity, "点击了5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_6:
                Toast.makeText(_mActivity, "点击了6", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
