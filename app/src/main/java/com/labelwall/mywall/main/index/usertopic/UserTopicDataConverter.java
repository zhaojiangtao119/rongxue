package com.labelwall.mywall.main.index.usertopic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.main.index.TopicField;
import com.labelwall.mywall.main.user.UserProfileField;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-10.
 */

public class UserTopicDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject pageInfo = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray userTopicArray = pageInfo.getJSONArray("list");
        final int size = userTopicArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject topic = userTopicArray.getJSONObject(i);
            //topic
            final Integer topicId = topic.getInteger("id");
            final String title = topic.getString("title");
            final String content = topic.getString("content");
            final String image = topic.getString("image");
            final int replyNum = topic.getInteger("replyNum");
            final int likeNum = topic.getInteger("likeNum");
            //设置topic的显示的样式标识
            int type = 0;
            if (image == null && content != null) {
                type = ItemType.TEXT;
            } else if (image != null && content == null) {
                type = ItemType.IMAGE;
            } else if (image != null && content != null) {
                type = ItemType.TEXT_IMAGE;
            }
            //组装数据
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(type)
                    .setField(TopicField.TOPIC_ID, topicId)
                    .setField(TopicField.TITLE, title)
                    .setField(TopicField.CONTENT, content)
                    .setField(TopicField.IMAGE, image)
                    .setField(TopicField.REPLY_NUM, replyNum)
                    .setField(TopicField.LIKE_NUM, likeNum)
                    .build();
            //添加ArrayList中
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
