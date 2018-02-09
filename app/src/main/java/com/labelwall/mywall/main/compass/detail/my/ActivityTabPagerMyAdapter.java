package com.labelwall.mywall.main.compass.detail.my;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.main.compass.detail.ActivityJoinUserDelegate;
import com.labelwall.mywall.main.compass.detail.ActivityJoinUserNullDelegate;
import com.labelwall.mywall.main.compass.detail.comment.ActivityCommentDelegate;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-02-08.
 */

public class ActivityTabPagerMyAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<String> TAB_TITLES = new ArrayList<>();
    private final ArrayList<String> USERS = new ArrayList<>();
    private final Integer ACTIVITY_ID;

    public ActivityTabPagerMyAdapter(FragmentManager fm, JSONObject data, JSONObject commentData, Integer activityId) {
        super(fm);
        this.ACTIVITY_ID = activityId;
        //设置title标题
        TAB_TITLES.add("已加入");
        TAB_TITLES.add("已申请");
        TAB_TITLES.add("评论");
        //获取加入的用户信息
        final JSONArray joinUserArray = data.getJSONArray("joinUser");
        if (joinUserArray != null && joinUserArray.size() > 0) {
            USERS.add(0, joinUserArray.toString());//存储的时候指定list下标
        } else {
            USERS.add(0, null);
        }
        final JSONArray noCheckedJoinUserArray = data.getJSONArray("noCheckedJoinUser");
        if (noCheckedJoinUserArray != null && noCheckedJoinUserArray.size() > 0) {
            USERS.add(1, noCheckedJoinUserArray.toString());
        } else {
            USERS.add(1, null);
        }
        final JSONArray commentArray = commentData.getJSONArray("list");
        if (commentArray != null && commentArray.size() > 0) {
            USERS.add(2, commentArray.toJSONString());
        } else {
            USERS.add(2, null);
        }
    }

    @Override
    public Fragment getItem(int position) {
        String hintMessage = null;
        if (position == 0) {
            if (USERS.get(0) != null) {
                return ActivityJoinUserDelegate.create(USERS.get(0));
            } else {
                hintMessage = "暂无加入的用户";
            }
        } else if (position == 1) {
            if (USERS.get(1) != null) {
                return ActivityApplyJoinUserDelegate.create(USERS.get(1), ACTIVITY_ID);
            } else {
                hintMessage = "暂无申请加入的用户";
            }
        } else if (position == 2) {
            if (USERS.get(2) != null) {
                return ActivityCommentDelegate.create(USERS.get(2));
            } else {
                hintMessage = "暂无评论";
            }
        }
        return ActivityJoinUserNullDelegate.create(hintMessage);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES.get(position);
    }
}
