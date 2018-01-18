package com.labelwall.mywall.ui.test;

import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * Created by Administrator on 2018-01-09.
 */

public class ItemEntityBuilder {

    private static final LinkedHashMap<Object, Object> FIELDS = new LinkedHashMap<>();

    public ItemEntityBuilder() {
        FIELDS.clear();
    }

    public ItemEntityBuilder setItemType(int itemType) {
        FIELDS.put(ItemField.ITEM_TYPE, itemType);
        return this;
    }

    public ItemEntityBuilder setField(Object key, Object value) {
        FIELDS.put(key, value);
        return this;
    }

    public ItemEntityBuilder setField(LinkedHashMap<Object, Object> fields) {
        FIELDS.putAll(fields);
        return this;
    }

    public final ItemEntity build() {
        return new ItemEntity(FIELDS);
    }

}
