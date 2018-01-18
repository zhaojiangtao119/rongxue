package com.labelwall.mywall.main;

import android.graphics.Color;

import com.labelwall.mywall.delegates.bottom.BaseBottomDelegate;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.delegates.bottom.BottomTabBean;
import com.labelwall.mywall.delegates.bottom.ItemBuilder;
import com.labelwall.mywall.main.user.UserDelegate;
import com.labelwall.mywall.main.compass.CompassDelegate;
import com.labelwall.mywall.main.index.IndexDelegate;
import com.labelwall.mywall.main.search.SearchDelegate;
import com.labelwall.mywall.main.sort.SortDelegate;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2018-01-04.
 * 基本容器
 */

public class WallBottomDelegate extends BaseBottomDelegate {

    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();
        items.put(new BottomTabBean("{fa-home}", ""), new IndexDelegate());
        items.put(new BottomTabBean("{fa-sort}", ""), new SortDelegate());
        items.put(new BottomTabBean("{fa-compass}", ""), new CompassDelegate());
        items.put(new BottomTabBean("{fa-search}", ""), new SearchDelegate());
        items.put(new BottomTabBean("{fa-user}", ""), new UserDelegate());
        return builder.addItem(items).build();
    }

    @Override
    public int setIndexDelegate() {
        return 0;
    }

    @Override
    public int setClickedColor() {
        return Color.parseColor("#ffff8800");
    }
}
