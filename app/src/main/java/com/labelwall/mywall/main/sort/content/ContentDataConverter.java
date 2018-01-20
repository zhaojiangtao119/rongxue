package com.labelwall.mywall.main.sort.content;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-20.
 * 商品信息数据转化
 */

public class ContentDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {

        final JSONObject pageInfo = JSON.parseObject(getJsonData()).getJSONObject("data");
        if (pageInfo.getJSONArray("list") != null) {
            final JSONArray productArray = pageInfo.getJSONArray("list");
            final int size = productArray.size();
            for (int i = 0; i < size; i++) {
                final JSONObject product = productArray.getJSONObject(i);
                final int id = product.getInteger("id");
                final String name = product.getString("name");
                final String subTitle = product.getString("subtitle");
                final String mainImg = product.getString("mainImage");

                final MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(ItemType.PRODUCT_CONTENT)
                        .setField(MultipleFields.ID, id)
                        .setField(ContentDataField.NAME, name)
                        .setField(ContentDataField.SUBTITLE, subTitle)
                        .setField(ContentDataField.MAINIMAGE, mainImg)
                        .build();
                ENTITIES.add(entity);
            }
        }
        return ENTITIES;
    }
}
