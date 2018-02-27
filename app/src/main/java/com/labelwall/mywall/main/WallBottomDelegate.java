package com.labelwall.mywall.main;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.bottom.BaseBottomDelegate;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.delegates.bottom.BottomTabBean;
import com.labelwall.mywall.delegates.bottom.ItemBuilder;
import com.labelwall.mywall.main.cart.ShopCartDelegate;
import com.labelwall.mywall.main.compass.ActivityDelegate;
import com.labelwall.mywall.main.user.UserDelegate;
import com.labelwall.mywall.main.index.IndexDelegate;
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
        items.put(new BottomTabBean("{fa-compass}", ""), new ActivityDelegate());
        items.put(new BottomTabBean("{fa-shopping-cart}", ""), new ShopCartDelegate());
        items.put(new BottomTabBean("{fa-user}", ""), new UserDelegate());
        return builder.addItem(items).build();
    }

    @Override
    public int setIndexDelegate(Integer index) {
        return BaseBottomDelegate.setIndex(index);
    }

    @Override
    public int setClickedColor() {
        return ContextCompat.getColor(_mActivity, R.color.app_title);
        //return Color.parseColor("#ffff8800");
    }
}
