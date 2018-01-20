package com.labelwall.mywall.main.sort.list;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-19.
 */

public class VerticalListDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray productCategoryArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = productCategoryArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject produceCategory = productCategoryArray.getJSONObject(i);
            final int id = produceCategory.getInteger("id");
            final String name = produceCategory.getString("name");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ItemType.VERTICAL_MENU_LIST)
                    .setField(VerticalListField.ID, id)
                    .setField(VerticalListField.NAME, name)
                    .setField(MultipleFields.TAG, false)//点击标记
                    .build();
            ENTITIES.add(entity);
            //设置第一个选项被选中
            ENTITIES.get(0).setField(MultipleFields.TAG, true);
        }
        return ENTITIES;
    }
}
