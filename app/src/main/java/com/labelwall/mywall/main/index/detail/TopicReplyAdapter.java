package com.labelwall.mywall.main.index.detail;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.labelwall.mywall.R;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2018-01-09.
 */

public class TopicReplyAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, TopicReplyViewHolder> {

    protected TopicReplyAdapter(List<MultipleItemEntity> data) {
        super(data);
        init();//初始化布局
    }

    private void init() {
        addItemType(ItemType.TEXT, R.layout.item_topic_reply_text);
        openLoadAnimation();
        isFirstOnly(false);
    }

    public static TopicReplyAdapter create(List<MultipleItemEntity> data) {
        return new TopicReplyAdapter(data);
    }

    public static TopicReplyAdapter create(DataConverter converter) {
        return new TopicReplyAdapter(converter.convert());
    }


    @Override
    protected TopicReplyViewHolder createBaseViewHolder(View view) {
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(TopicReplyViewHolder holder, MultipleItemEntity itemEntity) {
        holder.bindData(mContext, holder, itemEntity);
    }

}
