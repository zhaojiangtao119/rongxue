package com.labelwall.mywall.push;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushMessage;

/**
 * Created by Administrator on 2018-03-13.
 * Tag Alias操作的帮助类
 */

public class TagAliasOperatorHelper {

    private static TagAliasOperatorHelper mInstance;
    private Context mContext;

    private final String TAG_TAG = "JPush Tag";
    private final String TAG_ALIAS = "JPush Alias";

    public static TagAliasOperatorHelper getInstance() {
        if (mInstance == null) {
            synchronized (TagAliasOperatorHelper.class) {
                if (mInstance == null) {
                    mInstance = new TagAliasOperatorHelper();
                }
            }
        }
        return mInstance;
    }

    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        final int sequence = jPushMessage.getSequence();
        Log.e(TAG_TAG, "action - onTagOperatorResult, sequence:" + sequence + ",tags:" + jPushMessage.getTags());
        Log.e(TAG_TAG, "tags size:" + jPushMessage.getTags().size());
        final int errorCode = jPushMessage.getErrorCode();
        if (errorCode == 0) {
            Log.i(TAG_TAG, "JPush set Tag success");
            if (mIOperatorTagListener != null) {
                mIOperatorTagListener.onOperatorSuccess();
            }
        } else {
            String logs = "JPush set Tag fail ";
            if (errorCode == 6018) {
                //tag数量超过限制，需要先清除一部分
                logs += ",Tags is exceed limit need to clean";
            }
            logs += ", errorCode:" + jPushMessage.getErrorCode();
            Log.e(TAG_TAG, logs);
            if (mIOperatorTagListener != null) {
                mIOperatorTagListener.onOperatorFail(errorCode);
            }
        }
    }

    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG_ALIAS, "action - onAliasOperatorResult, sequence:" + sequence + ",alias:" + jPushMessage.getAlias());
        final int errorCode = jPushMessage.getErrorCode();
        if (errorCode == 0) {
            if (mIOperatorAliasListener != null) {
                mIOperatorAliasListener.onOperatorSuccess();
            }
        } else {
            if (mIOperatorAliasListener != null) {
                mIOperatorAliasListener.onOperatorFail(errorCode);
            }
        }
    }

    public void onCheckTagStateResult(Context context, JPushMessage jPushMessage) {
        final int errorCode = jPushMessage.getErrorCode();
        if (errorCode == 0) {
            if (mIOperatorTagListener != null) {
                mIOperatorTagListener.onOperatorSuccess();
            }
        } else {
            if (mIOperatorTagListener != null) {
                mIOperatorTagListener.onOperatorFail(errorCode);
            }
        }
    }

    //添加一个alias和tag操作成功，失败的回调

    private IOperatorTagListener mIOperatorTagListener;
    private IOperatorAliasListener mIOperatorAliasListener;

    public interface IOperatorTagListener {
        void onOperatorSuccess();

        void onOperatorFail(int errorCode);
    }

    public interface IOperatorAliasListener {
        void onOperatorSuccess();

        void onOperatorFail(int errorCode);
    }

    public void setOperatorTagListener(IOperatorTagListener listener) {
        this.mIOperatorTagListener = listener;
    }

    public void setOperatorAliasListener(IOperatorAliasListener listener) {
        this.mIOperatorAliasListener = listener;
    }
}
