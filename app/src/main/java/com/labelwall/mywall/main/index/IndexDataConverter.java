package com.labelwall.mywall.main.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.main.user.UserProfileField;

import java.util.ArrayList;


/**
 * Created by Administrator on 2018-01-08.
 * Topic数据转化类
 */

public class IndexDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject pageInfo = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray topicEntityList = pageInfo.getJSONArray("list");
        //获取数据
        final int size = topicEntityList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject topic = topicEntityList.getJSONObject(i);
            //topic
            final Integer topicId = topic.getInteger("id");
            final String title = topic.getString("title");
            final String content = topic.getString("content");
            final String image = topic.getString("image");
            final int replyNum = topic.getInteger("replyNum");
            final int likeNum = topic.getInteger("likeNum");
            final String createTime = topic.getString("createTimeStr");
            //user
            final JSONObject user = topic.getJSONObject("userDto");
            final int userId = user.getInteger("id");
            final String username = user.getString("username");
            final String avatar = user.getString("head");
            final String schoolName = user.getString("schoolName");
            final String province = user.getString("locationProvince");
            final String city = user.getString("locationCity");
            final String county = user.getString("locationCounty");
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
                    .setField(TopicField.CREATE_TIME, createTime)
                    .setField(UserProfileField.USER_ID, userId)
                    .setField(UserProfileField.USERNAME, username)
                    .setField(UserProfileField.AVATAR, avatar)
                    .setField(UserProfileField.SCHOOL_NAME, schoolName)
                    .setField(UserProfileField.PROVINCE, province)
                    .setField(UserProfileField.CITY, city)
                    .setField(UserProfileField.COUNTY, county)
                    .build();
            //添加ArrayList中
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
