package com.labelwall.mywall.main;

import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-03-09.
 */

public class MainPageDataConver extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        for (int i = 0; i < 100; i++) {
            String text = "测试" + i;

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(100)
                    .setField(MultipleFields.TEXT, text)
                    .build();

            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
