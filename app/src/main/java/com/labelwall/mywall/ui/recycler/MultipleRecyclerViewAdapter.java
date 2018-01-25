package com.labelwall.mywall.ui.recycler;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.labelwall.mywall.R;

import java.util.List;

/**
 * Created by Administrator on 2018-01-25.
 */

public class MultipleRecyclerViewAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, MultipleRecyclerViewHolder>
        implements BaseQuickAdapter.SpanSizeLookup {


    public MultipleRecyclerViewAdapter(List<MultipleItemEntity> data) {
        super(data);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, MultipleItemEntity item) {

    }

    private static List<MultipleItemEntity> mDatas = null;


    @Override
    protected MultipleRecyclerViewHolder createBaseViewHolder(View view) {
        return super.createBaseViewHolder(view);
    }

    @Override
    public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
        return 0;
    }


    public static MultipleRecyclerViewAdapter create(List<MultipleItemEntity> data) {
        return new MultipleRecyclerViewAdapter(data);
    }

    //绑定转换的数据到Item布局中
    public static MultipleRecyclerViewAdapter create(DataConverter data) {
        return new MultipleRecyclerViewAdapter(data.convert());
    }

    public void addItem(List<MultipleItemEntity> newDatas) {
        newDatas.addAll(mDatas);
        mDatas.removeAll(mDatas);
        mDatas.addAll(newDatas);
        notifyDataSetChanged();
    }
}
