package com.labelwall.mywall.main.compass.detail.my;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.main.user.UserProfileField;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-02-08.
 */

public class ActivityApplyJoinUserDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray joinUserArray = JSON.parseArray(getJsonData());
        final int size = joinUserArray.size();
        for (int i = 0; i < size; i++) {
            JSONObject joinUser = joinUserArray.getJSONObject(i);
            final Integer userId = joinUser.getInteger("id");
            final String username = joinUser.getString("username");
            final String avatar = joinUser.getString("head");
            final String schoolName = joinUser.getString("schoolName");
            final String city = joinUser.getString("locationCity");
            final String county = joinUser.getString("locationCounty");
            final String province = joinUser.getString("locationProvince");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ItemType.APPLY_JOIN_USERS)
                    .setField(UserProfileField.USER_ID, userId)
                    .setField(UserProfileField.USERNAME, username)
                    .setField(UserProfileField.AVATAR, avatar)
                    .setField(UserProfileField.SCHOOL_NAME, schoolName)
                    .setField(UserProfileField.CITY, city)
                    .setField(UserProfileField.COUNTY, county)
                    .setField(UserProfileField.PROVINCE, province)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
