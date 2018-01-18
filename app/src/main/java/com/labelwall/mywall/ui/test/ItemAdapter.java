package com.labelwall.mywall.ui.test;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.labelwall.mywall.R;
import com.labelwall.mywall.ui.recycler.ItemType;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Administrator on 2018-01-09.
 */

public class ItemAdapter extends BaseMultiItemQuickAdapter<ItemEntity, ItemViewHolder> {

    public ItemAdapter(List<ItemEntity> data) {
        super(data);
        init();//初始化布局
    }

    private void init() {
        addItemType(ItemType.TEXT, R.layout.item_multiple_text);
        addItemType(ItemType.TEXT_IMAGE, R.layout.item_multiple_text_image);
        openLoadAnimation();
        isFirstOnly(false);
    }

    @Override
    protected ItemViewHolder createBaseViewHolder(View view) {
        return super.createBaseViewHolder(view);
    }


    @Override
    protected void convert(ItemViewHolder helper, ItemEntity item) {
        helper.bindData(mContext,helper,item);
    }

    public static ItemAdapter create(List<ItemEntity> data) {
        return new ItemAdapter(data);
    }

    public static ItemAdapter create(DataCon dataCon) {
        return new ItemAdapter(dataCon.convert());
    }
}
