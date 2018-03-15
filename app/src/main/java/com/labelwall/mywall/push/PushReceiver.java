package com.labelwall.mywall.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.labelwall.mywall.WallActivity;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.main.compass.ActivityJPushTag;
import com.labelwall.mywall.util.log.WallLogger;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018-01-18.
 * 极光推送，广播接收器
 */

public class PushReceiver extends BroadcastReceiver {

    private final Context mContext = MyWall.getApplicationContext();
    private TagAliasOperatorHelper mTagAliasHelper = TagAliasOperatorHelper.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        //获取极光推送的基本消息
        final Bundle bundle = intent.getExtras();
        final Set<String> keys = bundle.keySet();
        final JSONObject json = new JSONObject();
        for (String key : keys) {
            final Object val = bundle.get(key);
            json.put(key, val);
        }

        WallLogger.json("PushReceiver", json.toJSONString());

        final String pushAction = intent.getAction();
        if (pushAction.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
            //处理接收到的信息
            onReceivedMessage(bundle);
        } else if (pushAction.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            //打开相应的Notification
            onOpenNotification(context, bundle);
        }
    }

    private void onReceivedMessage(Bundle bundle) {
        final String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        final String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        final int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        final String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        final String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        final String alert = bundle.getString(JPushInterface.EXTRA_ALERT);

        //判断接收的结果，如果是活动的提示就删除该APP中该活动的Tag，该活动的tag存储在extra中;
        if (!StringUtils.isEmpty(extra)) {
            final JSONObject jsonObject = JSON.parseObject(extra);
            final String tag = jsonObject.getString("ActivityTag");
            //删除该tag
            ActivityJPushTag.getInstance()
                    .checkTagState(mContext, JPushAliasTagSequence.ACTION_TAG_CHECK, tag);
            mTagAliasHelper.setOperatorTagListener(new TagAliasOperatorHelper.IOperatorTagListener() {
                @Override
                public void onOperatorSuccess() {
                    ActivityJPushTag.getInstance()
                            .delJPushTag(mContext, JPushAliasTagSequence.ACTION_TAG_DELETE, tag);
                }

                @Override
                public void onOperatorFail(int errorCode) {

                }
            });
        }
    }

    private void onOpenNotification(Context context, Bundle bundle) {
        //推送的通知，手机中点击通知后出发的方法
        final String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        final String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        final int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        final String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        final String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        final String alert = bundle.getString(JPushInterface.EXTRA_ALERT);

        final Bundle openActivityBundle = new Bundle();
        //点击通知后进入App的那个页面
        final Intent intent = new Intent(context, WallActivity.class);
        intent.putExtras(openActivityBundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ContextCompat.startActivity(context, intent, null);
    }
}
