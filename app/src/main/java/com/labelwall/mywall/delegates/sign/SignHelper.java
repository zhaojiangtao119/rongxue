package com.labelwall.mywall.delegates.sign;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.app.AccountManager;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.main.live.util.TencentLiveUserAccount;
import com.labelwall.mywall.push.JPushAliasTagSequence;
import com.labelwall.mywall.push.TagAliasOperatorHelper;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018-01-05.
 * 用于解析服务端返回的数据，并将数据插入到数据库
 */

public class SignHelper {

    private static final String TAG = "JPush";
    private static String logs = null;
    private static String username = null;
    private static Message mMessage = null;
    private static TagAliasOperatorHelper mHelper = TagAliasOperatorHelper.getInstance();

    private static final Context mContext = MyWall.getApplicationContext();

    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //设置别名
            String username = (String) msg.obj;
            Set<String> tag = new HashSet<>();
            tag.add("labelwall");
            /**
             * 设置JPush的tag和alias
             * setAlias()，setTag():这个接口是覆盖逻辑，而不是增量逻辑
             * addAlias(),addTags():这个接口是增量逻辑，而不是覆盖逻辑
             */
            JPushInterface.setAlias(mContext, JPushAliasTagSequence.ACTION_ALIAS_SET, username);
            JPushInterface.addTags(mContext, JPushAliasTagSequence.ACTION_TAG_ADD, tag);
            operatorResult();
        }
    };

    private static void operatorResult() {
        mHelper.setOperatorAliasListener(new TagAliasOperatorHelper.IOperatorAliasListener() {
            @Override
            public void onOperatorSuccess() {

            }

            @Override
            public void onOperatorFail(int errorCode) {
                //todo 设置失败，有两种情况要处理
                //1.请求超时，建议重复请求
                //2.Tag数量查过限制，删除部分tag
                if (errorCode == 6002) {
                    mHandler.sendMessageDelayed(mMessage, 2000);//两秒后重新发送
                }
                if (errorCode == 6018) {
                    //删除部分tag
                }
            }
        });
        mHelper.setOperatorTagListener(new TagAliasOperatorHelper.IOperatorTagListener() {
            @Override
            public void onOperatorSuccess() {

            }

            @Override
            public void onOperatorFail(int errorCode) {

            }
        });
    }

    public static void onSignUp(String response, ISignListener signListener) {
        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        final long id = profileJson.getLong("id");
        final String username = profileJson.getString("username");
        final String password = profileJson.getString("password");
        final String head = profileJson.getString("head");
        final String email = profileJson.getString("email");
        final String phone = profileJson.getString("phone");
        final Date schoolDate = profileJson.getDate("schoolDate");
        final String schoolName = profileJson.getString("schoolName");
        final String locationProvince = profileJson.getString("locationProvince");
        final String locationCity = profileJson.getString("locationCity");
        final String locationCounty = profileJson.getString("locationCounty");
        final Integer provinceId = profileJson.getInteger("provinceId");
        final Integer cityId = profileJson.getInteger("cityId");
        final Integer countyId = profileJson.getInteger("countyId");
        final Integer schoolId = profileJson.getInteger("schoolId");
        final String createTimeStr = profileJson.getString("createTimeStr");
        final String updateTimeStr = profileJson.getString("updateTimeStr");
        final Integer gender = profileJson.getInteger("gender");
        final String birthday = profileJson.getString("birthday");

        final UserProfile userProfile = new UserProfile(id, username, password, head,
                email, phone, schoolDate, schoolName,
                locationProvince, locationCity, locationCounty,
                provinceId, cityId, countyId, schoolId,
                createTimeStr, updateTimeStr, gender, birthday);
        //1.首先判断DAO中是否存在该条记录
        List<UserProfile> userProfileList = DataBaseManager
                .getInstance()
                .getDao()
                .queryRaw("where _id=?", new String[]{String.valueOf(id)});
        if (userProfileList == null || userProfileList.size() == 0) {
            //2.将数据插入到数据库中，
            DataBaseManager.getInstance().getDao().insert(userProfile);
            //3.将当前登录的用户id存储到Preference中
            WallPreference.setCurrentUserId(WallTagType.CURRENT_USER_ID.name(), id);
        } else {
            final int size = userProfileList.size();
            for (int i = 0; i < size; i++) {
                //将当前登录的用户id存储到Preference中
                WallPreference.setCurrentUserId(WallTagType.CURRENT_USER_ID.name(),
                        userProfileList.get(i).getId());
            }
        }
        //已经注册并登陆成功，执行注册成功的回调
        AccountManager.setSignState(true);//设置用户登录成功的标志
        signListener.onSignUpSuccess();//执行注册成功的回调

        // 注册极光推送别名alias，使用当前用户的用户名
        setAppAllias(username);
    }

    private static void setAppAllias(String username) {
        if (TextUtils.isEmpty(username)) {
            return;
        }
        mMessage = mHandler.obtainMessage();
        mMessage.obj = username;
        mMessage.sendToTarget();
    }

    public static void onSignIn(String response, ISignListener signListener) {

        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        final long id = profileJson.getLong("id");
        final String username = profileJson.getString("username");
        final String password = profileJson.getString("password");
        final String head = profileJson.getString("head");
        final String email = profileJson.getString("email");
        final String phone = profileJson.getString("phone");
        final Date schoolDate = profileJson.getDate("schoolDate");
        final String schoolName = profileJson.getString("schoolName");
        final String locationProvince = profileJson.getString("locationProvince");
        final String locationCity = profileJson.getString("locationCity");
        final String locationCounty = profileJson.getString("locationCounty");
        final Integer provinceId = profileJson.getInteger("provinceId");
        final Integer cityId = profileJson.getInteger("cityId");
        final Integer countyId = profileJson.getInteger("countyId");
        final Integer schoolId = profileJson.getInteger("schoolId");
        final String createTimeStr = profileJson.getString("createTimeStr");
        final String updateTimeStr = profileJson.getString("updateTimeStr");
        final Integer gender = profileJson.getInteger("gender");
        final String birthday = profileJson.getString("birthday");

        final UserProfile userProfile = new UserProfile(id, username, password, head,
                email, phone, schoolDate, schoolName,
                locationProvince, locationCity, locationCounty,
                provinceId, cityId, countyId, schoolId,
                createTimeStr, updateTimeStr, gender, birthday);

        //1.首先判断DAO中是否存在该条记录
        List<UserProfile> userProfileList = DataBaseManager
                .getInstance()
                .getDao()
                .queryRaw("where _id=?", new String[]{String.valueOf(id)});
        if (userProfileList == null || userProfileList.size() == 0) {
            //2.将数据插入到数据库中，
            DataBaseManager.getInstance().getDao().insert(userProfile);
            //3.将当前登录的用户id存储到Preference中
            WallPreference.setCurrentUserId(WallTagType.CURRENT_USER_ID.name(), id);
        } else {
            final int size = userProfileList.size();
            for (int i = 0; i < size; i++) {
                //将当前登录的用户id存储到Preference中
                WallPreference.setCurrentUserId(WallTagType.CURRENT_USER_ID.name(),
                        userProfileList.get(i).getId());
            }
        }
        //已经登录成功，执行登录成功的回调。
        AccountManager.setSignState(true);//设置用户登录成功的标志
        signListener.onSignInSuccess();//执行登陆成功的回调
        // 登录极光推送别名alias，使用当前用户的用户名
        setAppAllias(username);
        // 登录Tencent直播的sdk
        /*LoginTencentLive(username, TencentLiveUserAccount.getTencentIdentifier(),
                TencentLiveUserAccount.getTencentPassword());*/
    }

    private static void LoginTencentLive(final String username, final String account, final String password) {
        //调用腾讯IM登录
        ILiveLoginManager.getInstance().tlsLogin(account, password, new ILiveCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                //登陆成功。
                loginLive(account, data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //登录失败
                if (errCode == 229) {
                    //用户注册到Tencent直播的sdk上，不对结果进行处理
                    registerTencentLive(username, account, password);
                }
            }
        });
    }

    private static void loginLive(String accountStr, String data) {
        ILiveLoginManager.getInstance().iLiveLogin(accountStr, data, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //最终登录成功
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //登录失败
            }
        });
    }

    //判断用户是否注册了Tencent直播的sdk，如没有则注册，
    private static void registerTencentLive(final String username, String account, String password) {
        ILiveLoginManager.getInstance().tlsRegister(account, password, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //修改Tencent Live上的nickName，将用户名设置为nickname
                setNickName(username);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.e("TENCENT_2", errMsg + "," + errCode);
            }
        });
    }

    private static void setNickName(String nickname) {
        TIMFriendshipManager.getInstance().setNickName(nickname, new TIMCallBack() {
            @Override
            public void onError(int errCode, String errMsg) {
                Log.e("TENCENT_3", errMsg + "," + errCode);
            }

            @Override
            public void onSuccess() {
            }
        });
    }
}
