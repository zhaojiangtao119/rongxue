package com.labelwall.mywall.ui.recycler;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.labelwall.mywall.R;
import com.labelwall.mywall.main.index.TopicField;
import com.labelwall.mywall.main.user.UserProfileField;

/**
 * Created by Administrator on 2018-01-08.
 */

public class MultipleViewHolder extends BaseViewHolder {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    private Integer topicId = 0;
    private String topicImage = null;
    private String title = null;
    private String content = null;
    private int replyNum = 0;
    private int likeNum = 0;
    private int userId = 0;
    private String avatar = null;
    private String username = null;
    private String createTime = null;

    private MultipleViewHolder(View view) {
        super(view);
    }

    public static MultipleViewHolder create(View view) {
        return new MultipleViewHolder(view);
    }

    public void bindData(Context context, MultipleViewHolder holder, MultipleItemEntity itemEntity) {
        //转换数据
        topicId = itemEntity.getField(TopicField.TOPIC_ID);
        title = itemEntity.getField(TopicField.TITLE);
        topicImage = itemEntity.getField(TopicField.IMAGE);
        content = itemEntity.getField(TopicField.CONTENT);
        replyNum = itemEntity.getField(TopicField.REPLY_NUM);
        likeNum = itemEntity.getField(TopicField.LIKE_NUM);
        createTime = itemEntity.getField(TopicField.CREATE_TIME);
        userId = itemEntity.getField(UserProfileField.USER_ID);
        avatar = itemEntity.getField(UserProfileField.AVATAR);
        username = itemEntity.getField(UserProfileField.USERNAME);

        switch (holder.getItemViewType()) {
            case ItemType.TEXT:
                holder.setText(R.id.topic_content, content);
                addOnClickListener(R.id.topic_content);
                holder.setTag(R.id.topic_content, topicId);
                break;
            case ItemType.TEXT_IMAGE:
                holder.setText(R.id.topic_title, title);
                Glide.with(context)
                        .load(topicImage)
                        .apply(OPTIONS)
                        .into((ImageView) holder.getView(R.id.topic_image));
                addOnClickListener(R.id.topic_title);
                addOnClickListener(R.id.topic_image);
                //holder.setTag(R.id.topic_image, topicId);
                holder.setTag(R.id.topic_title, topicId);
                break;
            default:
                break;
        }
        //图片加载
        Glide.with(context)
                .load(avatar)
                .apply(OPTIONS)
                .into((ImageView) holder.getView(R.id.user_avatar));
        holder.setText(R.id.user_name, username);
        holder.setText(R.id.topic_reply_num, String.valueOf(replyNum));
        holder.setText(R.id.topic_like_num, String.valueOf(likeNum));
        holder.setText(R.id.topic_create_time, createTime);

        holder.setTag(R.id.user_info, userId);
        holder.setTag(R.id.topic_comment, topicId);
        holder.setTag(R.id.topic_info, topicId);
        holder.setTag(R.id.topic_create_time, topicId);

        addOnClickListener(R.id.user_info);
        addOnClickListener(R.id.topic_comment);
        addOnClickListener(R.id.topic_like);
    }
}
