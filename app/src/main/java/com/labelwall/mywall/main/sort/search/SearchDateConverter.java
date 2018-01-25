package com.labelwall.mywall.main.sort.search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.util.storage.WallPreference;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-25.
 */

public class SearchDateConverter extends DataConverter {

    //保存搜索历史记录
    private static final String TAG_SEARCH_HISTORY = "search_history";

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final String jsonStr = WallPreference.getCustomAppProfile(TAG_SEARCH_HISTORY);
        if (jsonStr.equals("")) {
            final JSONArray array = JSON.parseArray(jsonStr);
            final int size = array.size();
            for (int i = 0; i < size; i++) {
                final String jsonHistoryItemText = array.getString(i);
                final MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(SearchItemType.SEARCH_ITEM)
                        .setField(MultipleFields.TEXT, jsonHistoryItemText)
                        .build();
                ENTITIES.add(entity);
            }
        }
        return ENTITIES;
    }
}
