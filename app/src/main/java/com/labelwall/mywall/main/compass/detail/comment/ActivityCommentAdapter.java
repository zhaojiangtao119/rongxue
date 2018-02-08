package com.labelwall.mywall.main.compass.detail.comment;

import android.support.v7.widget.AppCompatTextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.main.index.TopicField;
import com.labelwall.mywall.main.user.UserProfileField;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018-02-08.
 */

public class ActivityCommentAdapter extends MultipleRecyclerViewAdapter {
    public ActivityCommentAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(ItemType.ACTIVITY_COMMENT_LIST, R.layout.item_activity_comment);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()) {
            case ItemType.ACTIVITY_COMMENT_LIST:
                final String username = item.getField(UserProfileField.USERNAME);
                final String content = item.getField(TopicField.CONTENT);
                final String createTime = item.getField(TopicField.CREATE_TIME);

                final AppCompatTextView usernameView =
                        helper.getView(R.id.tv_activity_comment_username);
                final AppCompatTextView contentView =
                        helper.getView(R.id.tv_activity_comment_content);
                final AppCompatTextView createTimeView =
                        helper.getView(R.id.tv_activity_comment_create_time);

                usernameView.setText(username);
                contentView.setText(content);
                createTimeView.setText(createTime);
                break;
            default:
                break;
        }
    }
}
