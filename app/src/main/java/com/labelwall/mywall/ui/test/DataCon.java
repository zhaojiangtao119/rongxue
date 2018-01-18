package com.labelwall.mywall.ui.test;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-09.
 */

public abstract class DataCon {

    protected final ArrayList<ItemEntity> ENTITYS = new ArrayList<>();
    private String mJsonData = null;

    public abstract ArrayList<ItemEntity> convert();

    public DataCon setJsonData(String json) {
        mJsonData = json;
        return this;
    }

    protected String getJsonData() {
        if (mJsonData == null) {
            throw new NullPointerException("Data is null");
        }
        return mJsonData;
    }
}
