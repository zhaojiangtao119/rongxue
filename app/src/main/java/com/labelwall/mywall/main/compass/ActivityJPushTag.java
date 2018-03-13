package com.labelwall.mywall.main.compass;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018-03-13.
 * 用户创建活动和加入活动时创建该App的Tag
 */

public class ActivityJPushTag {

    private Set<String> tags = new HashSet<>();

    private static class Holder {
        private static final ActivityJPushTag INSTANCE = new ActivityJPushTag();
    }

    public static ActivityJPushTag getInstance() {
        return Holder.INSTANCE;
    }

    //添加活动的Tag到App
    public void addJPushTag(Context context, int sequence, String tag) {
        if (tags.size() > 0) {
            tags.clear();
        }
        tags.add(tag);
        JPushInterface.addTags(context, sequence, tags);
    }
}
