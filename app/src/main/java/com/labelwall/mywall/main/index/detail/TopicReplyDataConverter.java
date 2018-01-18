package com.labelwall.mywall.main.index.detail;

import android.graphics.Camera;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.main.user.UserProfileField;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-09.
 */

public class TopicReplyDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject pageInfo = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray topicReplyArray = pageInfo.getJSONArray("list");
        int size = topicReplyArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject topicReply = topicReplyArray.getJSONObject(i);
            //topicReply
            final Integer id = topicReply.getInteger("id");
            final String image = topicReply.getString("image");
            final String content = topicReply.getString("content");
            final String createTime = topicReply.getString("createTimeStr");
            final Integer likeNum = topicReply.getInteger("likeNum");
            //user
            final JSONObject user = topicReply.getJSONObject("userDto");
            final Integer userId = user.getInteger("id");
            final String username = user.getString("username");
            final String avatar = user.getString("head");
            int type = 0;
            if (content == null && image != null) {
                type = ItemType.IMAGE;
            } else if (content != null && image == null) {
                type = ItemType.TEXT;
            } else if (content != null && image != null) {
                type = ItemType.TEXT_IMAGE;
            }

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(type)
                    .setField(TopicReplyField.TOPIC_REPLY_ID, id)
                    .setField(TopicReplyField.CONTENT, content)
                    .setField(TopicReplyField.CREATE_TIME, createTime)
                    .setField(TopicReplyField.LIKE_NUM,likeNum)
                    .setField(TopicReplyField.IMAGE, image)
                    .setField(UserProfileField.USERNAME, username)
                    .setField(UserProfileField.AVATAR, avatar)
                    .setField(UserProfileField.USER_ID, userId)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
