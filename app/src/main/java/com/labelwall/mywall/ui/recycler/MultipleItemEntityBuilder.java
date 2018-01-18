package com.labelwall.mywall.ui.recycler;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2018-01-08.
 * MultipleItemEntity的建造者，设置FIELDS数据
 */

public class MultipleItemEntityBuilder {

    //存储实体类的属性名与属性值
    private static final LinkedHashMap<Object, Object> FIELDS = new LinkedHashMap<>();

    public MultipleItemEntityBuilder() {
        FIELDS.clear();//清除之前的数据
    }

    public final MultipleItemEntityBuilder setItemType(int itemType) {
        FIELDS.put(MultipleFields.ITEM_TYPE, itemType);
        return this;
    }

    /**
     * 单个保存LinkedHashMap的值
     *
     * @param key
     * @param value
     * @return
     */
    public final MultipleItemEntityBuilder setField(Object key, Object value) {
        FIELDS.put(key, value);
        return this;
    }

    /**
     * 批量保存LinkedHashMap的值
     *
     * @param map
     * @return
     */
    public final MultipleItemEntityBuilder setField(LinkedHashMap<Object, Object> map) {
        FIELDS.putAll(map);
        return this;
    }

    public final MultipleItemEntity build() {
        return new MultipleItemEntity(FIELDS);
    }
}
