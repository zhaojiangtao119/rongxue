package com.labelwall.mywall.ui.test;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.labelwall.mywall.delegates.bottom.ItemBuilder;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2018-01-09.
 */

public class ItemEntity implements MultiItemEntity {

    private final ReferenceQueue<LinkedHashMap<Object, Object>> ITEM_QUEUE = new ReferenceQueue<>();
    private final LinkedHashMap<Object, Object> MULTIPLE_FIELDS = new LinkedHashMap<>();
    private final SoftReference<LinkedHashMap<Object, Object>> FIELDS_REFERENCE =
            new SoftReference<>(MULTIPLE_FIELDS, ITEM_QUEUE);

    @Override
    public int getItemType() {
        return (int) FIELDS_REFERENCE.get().get(ItemField.ITEM_TYPE);
    }

    public ItemEntity(LinkedHashMap<Object, Object> fields) {
        FIELDS_REFERENCE.get().putAll(fields);
    }

    public final <T> T getField(Object key) {
        return (T) FIELDS_REFERENCE.get().get(key);
    }

    public final LinkedHashMap<Object, Object> getField() {
        return FIELDS_REFERENCE.get();
    }

    public final ItemEntity setField(Object key, Object value) {
        FIELDS_REFERENCE.get().put(key, value);
        return this;
    }

    public static ItemEntityBuilder builder(){
        return new ItemEntityBuilder();
    }
}
