package com.labelwall.mywall.main.user.profile.location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.profile.location.decoration.TitleItemDecoration;
import com.labelwall.mywall.main.user.profile.location.decoration.TitleItemDecoration2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Administrator on 2018-01-19.
 */

public class RecyclerViewDelegate extends WallDelegate {

    @BindView(R2.id.rv_location)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.indexBar)
    IndexBar mIndexBar = null;//右侧导航栏
    @BindView(R2.id.tvSideBarHint)
    AppCompatTextView mTextView = null;//显示指示器

    private List<ProvinceBean> mProvinceList = null;
    private ProvinceAdapter mAdapter = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_item_dectoration_loaction;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        initData(getResources().getStringArray(R.array.provinces));
        mAdapter = ProvinceAdapter.create(mProvinceList, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new TitleItemDecoration(getContext(), mProvinceList));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mIndexBar.setmPressedShowTextView(mTextView)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(manager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(mProvinceList);//设置数据源
    }

    private void initData(String[] stringArray) {
        mProvinceList = new ArrayList<>();
        final int provinceSize = stringArray.length;
        for (int i = 0; i < provinceSize; i++) {
            ProvinceBean provinceBean = new ProvinceBean(stringArray[i]);
            mProvinceList.add(provinceBean);
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
