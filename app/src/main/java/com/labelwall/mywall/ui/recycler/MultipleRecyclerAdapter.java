package com.labelwall.mywall.ui.recycler;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.labelwall.mywall.R;

import java.util.List;

/**
 * Created by Administrator on 2018-01-08.
 * recycler的Adapter
 * BaseMultiItemQuickAdapter<数据结构，ViewHolder>
 */

public class MultipleRecyclerAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, MultipleViewHolder>
        implements BaseQuickAdapter.SpanSizeLookup {

    private static List<MultipleItemEntity> mDatas = null;

    protected MultipleRecyclerAdapter(List<MultipleItemEntity> data) {
        super(data);
        init();
    }

    //初始化多布局
    private void init() {
        //设置不同的布局，添加到一个数组中
        addItemType(ItemType.TEXT, R.layout.item_multiple_text);
        //addItemType(ItemType.IMAGE, R.layout.item_multiple_image);
        addItemType(ItemType.TEXT_IMAGE, R.layout.item_multiple_text_image);
        openLoadAnimation();//打开动画效果
        isFirstOnly(false);//多次执行动画
    }

    @Override
    protected MultipleViewHolder createBaseViewHolder(View view) {
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity itemEntity) {
        holder.bindData(mContext, holder, itemEntity);
    }

    @Override
    public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
        return 0;
    }

    public static MultipleRecyclerAdapter create(List<MultipleItemEntity> data) {
        return new MultipleRecyclerAdapter(data);
    }

    //绑定转换的数据到Item布局中
    public static MultipleRecyclerAdapter create(DataConverter data) {
        return new MultipleRecyclerAdapter(data.convert());
    }

    public void addItem(List<MultipleItemEntity> newDatas) {
        newDatas.addAll(mDatas);
        mDatas.removeAll(mDatas);
        mDatas.addAll(newDatas);
        notifyDataSetChanged();
    }
}

