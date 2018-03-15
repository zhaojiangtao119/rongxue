package com.labelwall.mywall.push;

import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Created by Administrator on 2018-03-13.
 * JPush新的alias和tag的操作结果回调
 */

public class JPushAliasTagReceiver extends JPushMessageReceiver {

    /**
     * tag增删查改的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.e("JPush Tag", "action - onTagOperatorResult, sequence:" + sequence + ",tags:" + jPushMessage.getTags());
        Log.e("JPush Tag", "tags size:" + jPushMessage.getTags().size());
        Log.e("JPush Tag", "ErrorCode:" + jPushMessage.getErrorCode());
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    /**
     * 查询某个tag与当前用户的绑定状态的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagStateResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    /**
     * alias相关的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.e("JPush Alias", "action - onTagOperatorResult, sequence:" + sequence + ",tags:" + jPushMessage.getAlias());
        Log.e("JPush Alias", "tags size:" + jPushMessage.getAlias().length());
        Log.e("JPush Alias", "ErrorCode:" + jPushMessage.getErrorCode());
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    /**
     * 设置手机号码会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }
}
