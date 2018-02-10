package com.labelwall.mywall.ui.recycler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-01-08.
 * 数据转换的约束
 */

public abstract class DataConverter {
    //返回封装的Entity数据
    protected final ArrayList<MultipleItemEntity> ENTITIES = new ArrayList<>();
    private String mJsonData = null;

    public abstract ArrayList<MultipleItemEntity> convert();

    private int mItemType = 0;

    /**
     * 设置json数据
     *
     * @param json
     * @return
     */
    public DataConverter setJsonData(String json) {
        this.mJsonData = json;
        return this;
    }

    /**
     * 获取json数据
     *
     * @return
     */
    protected String getJsonData() {
        if (mJsonData == null) {
            throw new NullPointerException("Data is null");
        }
        return mJsonData;
    }

    public List<MultipleItemEntity> getItemList() {
        return ENTITIES;
    }

    /**
     * 清除数据  ArrayList中的entity
     */
    public void clearData() {
        ENTITIES.clear();
    }

    //设置数据的ItemType
    public int getItemType() {
        return mItemType;
    }

    public DataConverter setItemType(int itemType) {
        this.mItemType = itemType;
        return this;
    }
}