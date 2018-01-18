package com.labelwall.mywall.ui.recycler;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;


/**
 * Created by Administrator on 2018-01-08.
 * 保存数据的通用实体类
 */

public class MultipleItemEntity implements MultiItemEntity {

    private final ReferenceQueue<LinkedHashMap<Object, Object>> ITEM_QUEUE = new ReferenceQueue<>();
    //存储Item数据key = 数据名 value = 数据值
    private final LinkedHashMap<Object, Object> MULTIPLE_FIELDS = new LinkedHashMap<>();
    private final SoftReference<LinkedHashMap<Object, Object>> FIELDS_REFERENCE =
            new SoftReference<>(MULTIPLE_FIELDS, ITEM_QUEUE);

    /**
     * 通过建造者模式创建entity对象
     * 构造方法初始化保存的数据
     *
     * @param fields
     */
    public MultipleItemEntity(LinkedHashMap<Object, Object> fields) {
        FIELDS_REFERENCE.get().putAll(fields);
    }

    public static MultipleItemEntityBuilder builder() {
        return new MultipleItemEntityBuilder();
    }

    /**
     * 控制每一个Item的样式与表现特点
     *
     * @return
     */
    @Override
    public int getItemType() {
        return (int) FIELDS_REFERENCE.get().get(MultipleFields.ITEM_TYPE);
    }

    /**
     * 通过key获取LinkedHashMap其中保存的value
     *
     * @param key
     * @param <T>
     * @return
     */
    public final <T> T getField(Object key) {
        return (T) FIELDS_REFERENCE.get().get(key);
    }

    /**
     * 获取LinkedHashMap中所有的值
     *
     * @return
     */
    public final LinkedHashMap<?, ?> getFields() {
        return FIELDS_REFERENCE.get();
    }

    /**
     * 直接设置数据的key与value
     *
     * @param key
     * @param value
     * @return
     */
    public final MultipleItemEntity setField(Object key, Object value) {
        FIELDS_REFERENCE.get().put(key, value);
        return this;
    }

}
