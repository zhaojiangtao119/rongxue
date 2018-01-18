package com.labelwall.mywall.delegates.bottom;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2018-01-04.
 * 将导航栏的bean与每个页面结合起来，传入导航bean与子页面(fragment)
 */

public class ItemBuilder {

    //用来存储导航bean与页面delegate
    private final LinkedHashMap<BottomTabBean,BottomItemDelegate> ITEMS = new LinkedHashMap<>();

    static ItemBuilder builder(){
        return new ItemBuilder();
    }

    public final ItemBuilder addItem(BottomTabBean bean,BottomItemDelegate delegate){
        ITEMS.put(bean,delegate);
        return this;
    }
    public final ItemBuilder addItem(LinkedHashMap<BottomTabBean,BottomItemDelegate> items){
        ITEMS.putAll(items);
        return this;
    }

    public final LinkedHashMap<BottomTabBean,BottomItemDelegate> build(){
        return ITEMS;
    }
}
