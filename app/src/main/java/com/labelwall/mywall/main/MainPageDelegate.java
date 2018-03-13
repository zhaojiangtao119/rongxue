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
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.main.cart.ShopCartDelegate;
import com.labelwall.mywall.main.compass.ActivityDelegate;
import com.labelwall.mywall.main.index.IndexDelegate;
import com.labelwall.mywall.main.sort.SortDelegate;
import com.labelwall.mywall.main.user.UserDelegate;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-03-09.
 */

public class MainPageDelegate extends BottomItemDelegate {

    @BindView(R2.id.rv_main_page)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.tv_main_type)
    AppCompatTextView mMenu = null;

    @OnClick(R2.id.tv_main_type)
    void onClickMainType() {
        mDialog.initDialog();
    }

    private MainPageDialog mDialog = null;
    private MainPageAdapter mAdapter = null;
    private MainPageDataConver mDataconver = new MainPageDataConver();

    @Override
    public Object setLayout() {
        return R.layout.delegate_main_page;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mDialog = new MainPageDialog(this);
        initRecyclerView();
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
}
