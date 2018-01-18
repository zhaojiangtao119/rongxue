package com.labelwall.mywall.delegates.sign;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.app.AccountManager;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.delegates.launcher.ScrollLauncherTag;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018-01-05.
 * 用于解析服务端返回的数据，并将数据插入到数据库
 */

public class SignHelper {

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
        //将数据插入到数据库中，
        DataBaseManager.getInstance().getDao().insert(userProfile);
        List<UserProfile> userProfileList = DataBaseManager
                .getInstance()
                .getDao()
                .queryRaw("where _id=?", new String[]{String.valueOf(id)});
        final int size = userProfileList.size();
        for (int i = 0; i < size; i++) {
            //将当前登录的用户id存储到Preference中
            WallPreference.setCurrentUserId(WallTagType.CURRENT_USER_ID.name(),
                    userProfileList.get(i).getId());
        }
        //已经注册并登陆成功，执行注册成功的回调
        AccountManager.setSignState(true);//设置用户登录成功的标志
        signListener.onSignUpSuccess();//执行注册成功的回调
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
        //数据不允许重复插入
        DataBaseManager.getInstance().getDao().insert(userProfile);
        List<UserProfile> userProfileList = DataBaseManager
                .getInstance()
                .getDao()
                .queryRaw("where _id=?", new String[]{String.valueOf(id)});
        final int size = userProfileList.size();
        for (int i = 0; i < size; i++) {
            //将当前登录的用户id存储到Preference中
            WallPreference.setCurrentUserId(WallTagType.CURRENT_USER_ID.name(),
                    userProfileList.get(i).getId());
        }
        //已经登录成功，执行注册成功的回调。
        AccountManager.setSignState(true);//设置用户登录成功的标志
        signListener.onSignInSuccess();//执行登陆成功的回调
    }
}
