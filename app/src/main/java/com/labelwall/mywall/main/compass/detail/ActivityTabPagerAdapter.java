package com.labelwall.mywall.main.compass.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.labelwall.mywall.main.compass.detail.comment.ActivityCommentDelegate;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-02-07.
 */

public class ActivityTabPagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<String> TAB_TITLES = new ArrayList<>();
    private final ArrayList<String> USERS = new ArrayList<>();
    private final Integer ACTIVITY_ID;
    private String mComment = null;

    public ActivityTabPagerAdapter(FragmentManager fm, JSONObject data, Integer activityId) {
        super(fm);
        this.ACTIVITY_ID = activityId;
        //设置title标题
        TAB_TITLES.add("已加入");
        TAB_TITLES.add("已申请");
        TAB_TITLES.add("评论");
        //获取加入的用户信息
        final JSONArray joinUserArray = data.getJSONArray("joinUser");
        if (joinUserArray != null && joinUserArray.size() >= 0) {
            USERS.add(0, joinUserArray.toString());//存储的时候指定list下标
        }
        final JSONArray noCheckedJoinUserArray = data.getJSONArray("noCheckedJoinUser");
        if (noCheckedJoinUserArray != null && noCheckedJoinUserArray.size() >= 0) {
            USERS.add(1, noCheckedJoinUserArray.toString());
        }
        //加载评论
        uploadComment();
        //
        USERS.add(2, mComment);
    }

    private void uploadComment() {
        RestClient.builder()//加载评论
                .url("activity/comment/" + ACTIVITY_ID + "/1/20")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mComment = response;
                    }
                })
                .build()
                .get();
    }

    @Override
    public Fragment getItem(int position) {
        String hintMessage = null;
        if (position == 0) {
            if (USERS.size() > 0) {
                if (!StringUtils.isEmpty(USERS.get(0))) {
                    return ActivityJoinUserDelegate.create(USERS.get(0));
                }
            } else {
                hintMessage = "暂无加入的用户";
            }
        } else if (position == 1) {
            if (USERS.size() > 1) {
                if (!StringUtils.isEmpty(USERS.get(1))) {
                    return ActivityJoinUserDelegate.create(USERS.get(1));
                }
            } else {
                hintMessage = "暂无申请加入的用户";
            }
        } else if (position == 2) {
            if (USERS.size() > 2) {
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
