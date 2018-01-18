package com.labelwall.mywall.ui.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.main.index.TopicField;
import com.labelwall.mywall.main.user.UserProfileField;
import com.labelwall.mywall.ui.recycler.ItemType;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-09.
 */

public class TopicDataConverter extends DataCon {

    @Override
    public ArrayList<ItemEntity> convert() {
        final JSONObject pageInfo = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray topicList = pageInfo.getJSONArray("list");
        final int size = topicList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject topic = topicList.getJSONObject(i);
            //topic
            final Integer topicId = topic.getInteger("id");
            final String title = topic.getString("title");
            final String content = topic.getString("content");
            final String image = topic.getString("image");
            final int replyNum = topic.getInteger("replyNum");
            final int likeNum = topic.getInteger("likeNum");
            //user
            final JSONObject user = topic.getJSONObject("userDto");
            final int userId = user.getInteger("id");
            final String username = user.getString("username");
            final String avatar = user.getString("head");
            final String schoolName = user.getString("schoolName");
            final String province = user.getString("locationProvince");
            final String city = user.getString("locationCity");
            final String county = user.getString("locationCounty");
            int type = 0;
            if (image == null && content != null) {
                type = ItemType.TEXT;
            } else if (image != null && content == null) {
                type = ItemType.IMAGE;
            } else if (image != null && content != null) {
                type = ItemType.TEXT_IMAGE;
            }

            final ItemEntity entity = ItemEntity.builder()
                    .setItemType(type)
                    .setField(TopicField.TOPIC_ID, topicId)
                    .setField(TopicField.TITLE, title)
                    .setField(TopicField.CONTENT, content)
                    .setField(TopicField.IMAGE, image)
                    .setField(TopicField.REPLY_NUM, replyNum)
                    .setField(TopicField.LIKE_NUM, likeNum)
                    .setField(UserProfileField.USER_ID, userId)
                    .setField(UserProfileField.USERNAME, username)
                    .setField(UserProfileField.AVATAR, avatar)
                    .setField(UserProfileField.SCHOOL_NAME, schoolName)
                    .setField(UserProfileField.PROVINCE, province)
                    .setField(UserProfileField.CITY, city)
                    .setField(UserProfileField.COUNTY, county)
                    .build();
            ENTITYS.add(entity);
        }
        return ENTITYS;
    }
}
