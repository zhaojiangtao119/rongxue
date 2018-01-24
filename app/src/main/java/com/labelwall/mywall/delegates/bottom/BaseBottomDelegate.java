package com.labelwall.mywall.delegates.bottom;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Administrator on 2018-01-04.
 * 主界面底部栏
 */

public abstract class BaseBottomDelegate extends WallDelegate implements View.OnClickListener {

    private final ArrayList<BottomTabBean> TAB_BEANS = new ArrayList<>();
    private final ArrayList<BottomItemDelegate> ITEM_DELEGATES = new ArrayList<>();
    private final LinkedHashMap<BottomTabBean, BottomItemDelegate> ITEMS = new LinkedHashMap<>();
    //记录当前的fragment
    private int mCurrentDelegate = 0;
    private int mIndexDelegate = 0;
    //点击bean字体变色
    private int mClickedColor = Color.RED;

    @BindView(R2.id.bottom_bar)
    LinearLayoutCompat mBottomBar = null;

    //设置bean与delegate的值
    public abstract LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder);

    public abstract int setIndexDelegate();

    @ColorInt
    public abstract int setClickedColor();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentDelegate = setIndexDelegate();
        if (setClickedColor() != 0) {
            mClickedColor = setClickedColor();
        }
        //初始化数据bean/delegate
        final ItemBuilder builder = new ItemBuilder();
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = setItems(builder);
        ITEMS.putAll(items);
        for (Map.Entry<BottomTabBean, BottomItemDelegate> entry : ITEMS.entrySet()) {
            final BottomTabBean key = entry.getKey();
            final BottomItemDelegate value = entry.getValue();
            TAB_BEANS.add(key);
            ITEM_DELEGATES.add(value);
        }
    }

    @Override
    public Object setLayout() {//设置bottom的bar
        return R.layout.delegate_bottom;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        final int size = ITEMS.size();

        //初始化delegate（fragment）使用fragmentation的方式，
        // 将delegate初始化到bottom_bar_delegate_container
        final ISupportFragment[] delegateArray = ITEM_DELEGATES.toArray(new ISupportFragment[size]);
        getSupportDelegate().loadMultipleRootFragment(R.id.bottom_bar_delegate_container, mIndexDelegate, delegateArray);

        //初始化BottomBar
        for (int i = 0; i < size; i++) {
            //将TabBean的布局添加在LinearLayout中
            LayoutInflater.from(getContext()).inflate(R.layout.bottom_ite_icon_text_layout, mBottomBar);
            //获取每一个TabBean的RelativeLayout
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            //设置每一个TebBean的点击事件
            item.setTag(i);//设置Tag标识，用户点击事件的绑定
            item.setOnClickListener(this);
            //获取TabBean的子组件（IconTextView/AppCompatTextView）
            final IconTextView itemIcon = (IconTextView) item.getChildAt(0);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
            //初始化子组件数据
            final BottomTabBean bean = TAB_BEANS.get(i);
            itemIcon.setText(bean.getICON());
            itemTitle.setText(bean.getTITLE());
            if (i == mCurrentDelegate) {//循环的item正好当前展示的fragment
                itemIcon.setTextColor(mClickedColor);
                itemTitle.setTextColor(mClickedColor);

                getSupportDelegate().showHideFragment(ITEM_DELEGATES.get(mCurrentDelegate));
                //当前的页面标识改变为tag
                mCurrentDelegate = i;
            }
        }
    }

    /**
     * 重置所有的的TabBean的颜色，到未选中状态
     */
    private void resetColor() {
        final int count = mBottomBar.getChildCount();
        for (int i = 0; i < count; i++) {
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            final IconTextView itemIcon = (IconTextView) item.getChildAt(0);
            itemIcon.setTextColor(Color.GRAY);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
            itemTitle.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onClick(View v) {
        final int tag = (int) v.getTag();//获取当前被点击view的tag标识
        resetColor();
        final RelativeLayout item = (RelativeLayout) v;//当前被点击的TabBean
        final IconTextView itemIcon = (IconTextView) item.getChildAt(0);
        itemIcon.setTextColor(mClickedColor);
        final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
        itemTitle.setTextColor(mClickedColor);
        //fragmentation的showHideFragment方法，显示参数一的fragment，隐藏参数二的fragment
        getSupportDelegate().showHideFragment(ITEM_DELEGATES.get(tag), ITEM_DELEGATES.get(mCurrentDelegate));
        //当前的页面标识改变为tag
        mCurrentDelegate = tag;
    }
}
