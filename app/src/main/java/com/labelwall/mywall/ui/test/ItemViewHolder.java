package com.labelwall.mywall.ui.test;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseViewHolder;
import com.labelwall.mywall.R;
import com.labelwall.mywall.main.index.TopicField;
import com.labelwall.mywall.ui.recycler.ItemType;

/**
 * Created by Administrator on 2018-01-09.
 */

public class ItemViewHolder extends BaseViewHolder {

    private Integer topicId = 0;
    private String topicImage = null;
    private String title = null;
    private String content = null;
    private int replyNum = 0;
    private int likeNum = 0;
    private int userId = 0;
    private String avatar = null;
    private String username = null;

    private ItemViewHolder(View view) {
        super(view);
    }

    public static ItemViewHolder create(View view) {
        return new ItemViewHolder(view);
    }

    public void bindData(Context mContext, ItemViewHolder helper, ItemEntity item) {

    }
}
