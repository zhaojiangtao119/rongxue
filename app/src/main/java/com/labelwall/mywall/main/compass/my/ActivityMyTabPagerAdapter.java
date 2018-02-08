package com.labelwall.mywall.main.compass.my;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.main.compass.detail.ActivityJoinUserNullDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-02-08.
 */

public class ActivityMyTabPagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> PAGE_TITLE = new ArrayList<>();
    private final List<String> PAGE_DATA = new ArrayList<>();
    private ActivityMyDelegate mActivityMyDelegate = null;

    public ActivityMyTabPagerAdapter(FragmentManager fm, String startActivityData,
                                     String joinActivityData, ActivityMyDelegate delegate) {
        super(fm);
        this.mActivityMyDelegate = delegate;
        PAGE_TITLE.add("发起的");
        PAGE_TITLE.add("加入的");
        JSONObject pageInfo = JSON.parseObject(startActivityData).getJSONObject("data");
        JSONArray startArray = pageInfo.getJSONArray("list");
        if (startArray != null && startArray.size() > 0) {
            PAGE_DATA.add(startActivityData);
        } else {
            PAGE_DATA.add(0, null);
        }
        pageInfo = JSON.parseObject(joinActivityData).getJSONObject("data");
        JSONArray joinArray = pageInfo.getJSONArray("list");
        if (joinArray != null && joinArray.size() > 0) {
            PAGE_DATA.add(joinActivityData);
        } else {
            PAGE_DATA.add(1, null);
        }
    }

    @Override
    public Fragment getItem(int position) {
        String hintMessage = null;
        if (position == 0) {
            if (PAGE_DATA.get(0) != null) {
                return ActivityMyStartJoinDelegate.create(PAGE_DATA.get(0),mActivityMyDelegate);
            } else {
                hintMessage = "暂无发起的活动";
            }
        } else if (position == 1) {
            if (PAGE_DATA.get(1) != null) {
                return ActivityMyStartJoinDelegate.create(PAGE_DATA.get(1),mActivityMyDelegate);
            } else {
                hintMessage = "暂无加入的活动";
            }
        }
        return ActivityJoinUserNullDelegate.create(hintMessage);
    }

    @Override
    public int getCount() {
        return PAGE_DATA.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_TITLE.get(position);
    }
}
