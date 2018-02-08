package com.labelwall.mywall.main.compass.detail.comment;

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
 * Created by Administrator on 2018-02-08.
 */

public class ActivityCommentDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray commentArray = JSON.parseArray(getJsonData());
        final int size = commentArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject comment = commentArray.getJSONObject(i);
            final String username = comment.getString("username");
            final String content = comment.getString("content");
            final String createTime = comment.getString("createTimeStr");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ItemType.ACTIVITY_COMMENT_LIST)
                    .setField(UserProfileField.USERNAME, username)
                    .setField(TopicField.CREATE_TIME, createTime)
                    .setField(TopicField.CONTENT, content)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
