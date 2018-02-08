package com.labelwall.mywall.main.compass;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.main.user.UserProfileField;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018-02-06.
 */

public class ActivityDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject pageInfo = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray activityList = pageInfo.getJSONArray("list");
        final int size = activityList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject activityInfo = activityList.getJSONObject(i);
            final Integer id = activityInfo.getInteger("id");
            final Integer userId = activityInfo.getInteger("userId");
            final String theme = activityInfo.getString("theme");
            final String location = activityInfo.getString("location");
            final String city = activityInfo.getString("city");
            final String county = activityInfo.getString("county");
            final String school = activityInfo.getString("school");
            final String type = activityInfo.getString("type");
            final String style = activityInfo.getString("style");
            final String poster = activityInfo.getString("posterURL");
            final BigDecimal amount = activityInfo.getBigDecimal("amount");
            final Integer limitNum = activityInfo.getInteger("num_limt");
            final String content = activityInfo.getString("content");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ItemType.ACTIVITY_LIST)
                    .setField(MultipleFields.ID, id)
                    .setField(UserProfileField.USER_ID, userId)
                    .setField(ActivityFeilds.THEME, theme)
                    .setField(ActivityFeilds.LOCATION, location)
                    .setField(ActivityFeilds.CITY, city)
                    .setField(ActivityFeilds.COUNTY, county)
                    .setField(ActivityFeilds.SCHOOL, school)
                    .setField(ActivityFeilds.TYPE, type)
                    .setField(ActivityFeilds.STYLE, style)
                    .setField(ActivityFeilds.POSTER, poster)
                    .setField(ActivityFeilds.AMOUNT, amount)
                    .setField(ActivityFeilds.LIMIT_NUM, limitNum)
                    .setField(ActivityFeilds.CONTENT, content)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
