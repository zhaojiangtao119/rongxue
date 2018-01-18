package com.labelwall.mywall.main.index.detail;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.labelwall.mywall.R;
import com.labelwall.mywall.main.user.UserProfileField;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018-01-09.
 */

public class TopicReplyViewHolder extends BaseViewHolder {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    private Integer mId = 0;
    private String mImage = null;
    private String mContent = null;
    private String mCreateTime = null;
    private Integer mLikeNum = 0;
    private Integer mUserId = 0;
    private String mUsername = null;
    private String mAvatar = null;

    private TopicReplyViewHolder(View view) {
        super(view);
    }

    public static TopicReplyViewHolder create(View view) {
        return new TopicReplyViewHolder(view);
    }

    public void bindData(Context mContext, TopicReplyViewHolder holder, MultipleItemEntity itemEntity) {
        mId = itemEntity.getField(TopicReplyField.TOPIC_REPLY_ID);
        mImage = itemEntity.getField(TopicReplyField.IMAGE);
        mContent = itemEntity.getField(TopicReplyField.CONTENT);
        mCreateTime = itemEntity.getField(TopicReplyField.CREATE_TIME);
        mUserId = itemEntity.getField(UserProfileField.USER_ID);
        mUsername = itemEntity.getField(UserProfileField.USERNAME);
        mAvatar = itemEntity.getField(UserProfileField.AVATAR);
        switch (holder.getItemViewType()) {
            case ItemType.TEXT:
                holder.setText(R.id.user_name, mUsername);
                Glide.with(mContext)
                        .load(mAvatar)
                        .apply(OPTIONS)
                        .into((ImageView) holder.getView(R.id.user_avatar));
                holder.setText(R.id.topic_reply_content, mContent);
                /*holder.setText(R.id.topic_reply_like_num, String.valueOf(mLikeNum));
                holder.setText(R.id.topic_reply_time, mCreateTime);*/
                holder.setText(R.id.topic_reply_create_time, mCreateTime);
                break;
            case ItemType.IMAGE:
                break;
            case ItemType.TEXT_IMAGE:
                break;
            default:
                break;
        }
    }
}
