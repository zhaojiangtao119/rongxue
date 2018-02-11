package com.labelwall.mywall.main.user.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;

import com.google.gson.Gson;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.list.ListAdapter;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.main.user.list.ListItemType;
import com.labelwall.mywall.main.user.profile.location.JsonBean;
import com.labelwall.mywall.main.user.profile.location.LocationJsonReader;
import com.labelwall.mywall.main.user.profile.location.RecyclerViewDelegate;
import com.labelwall.mywall.util.location.LocationUtil;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Administrator on 2018-01-17.
 * 用户信息更新的delegate
 */

public class UserProfileDelegate extends WallDelegate {

    @BindView(R2.id.rv_user_profile)
    RecyclerView mRecyclerView = null;
    private UserProfile mUserprofile = null;
    private long mUserId = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private String[] mGender = new String[]{"男", "女", "保密"};

    private ArrayList<JsonBean> options1Item = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Item = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Item = new ArrayList<>();

    @Override
    public Object setLayout() {
        return R.layout.delegate_user_profile;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        List<UserProfile> userProfileList = DataBaseManager
                .getInstance()
                .getDao()
                .queryRaw("where _id = ?", new String[]{String.valueOf(mUserId)});
        mUserprofile = userProfileList.get(0);
        uploadUserProfileItem();
        initLocationData();
    }

    private void initLocationData() {
        Map<String, Object> locationData = LocationUtil.create(this)
                .initJsonData()
                .getLocationData();
        options1Item.addAll((ArrayList<JsonBean>) locationData.get(LocationUtil.ITEM1));
        options2Item.addAll((ArrayList<ArrayList<String>>) locationData.get(LocationUtil.ITEM2));
        options3Item.addAll((ArrayList<ArrayList<ArrayList<String>>>) locationData.get(LocationUtil.ITEM3));
    }

    private void uploadUserProfileItem() {
        final ListBean username = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(UserProfileSettingItem.USERNAME)
                .setText(UserProfileSettingItem.USERNAME_VALUE)
                .setValue(mUserprofile.getUsername())
                .build();
        final ListBean gender = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(UserProfileSettingItem.GENDER)
                .setText(UserProfileSettingItem.GENDER_VALUE)
                .setValue(mGender[mUserprofile.getGender()])
                .build();
        final ListBean birthday = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(UserProfileSettingItem.BIRTHDAY)
                .setText(UserProfileSettingItem.BIRTHDAY_VALUE)
                .setValue(mUserprofile.getBirthday())
                .build();
        final ListBean phone = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(UserProfileSettingItem.PHONE)
                .setText(UserProfileSettingItem.PHONE_VALUE)
                .setValue(mUserprofile.getPhone())
                .build();
        final ListBean email = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(UserProfileSettingItem.EMAIL)
                .setText(UserProfileSettingItem.EMAIL_VALUE)
                .setValue(mUserprofile.getEmail())
                .build();
        final ListBean location = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(UserProfileSettingItem.LOCATION)
                .setText(UserProfileSettingItem.LOCATION_VALUE)
                .setValue(mUserprofile.getLocationProvince() + "-"
                        + mUserprofile.getLocationCity() + "-"
                        + mUserprofile.getLocationCounty())
                .build();
        final ListBean location2 = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(7)
                .setDelegate(new RecyclerViewDelegate())
                .setText("ItemDecoration")
                .build();
        final List<ListBean> data = new ArrayList<>();
        data.add(username);
        data.add(gender);
        data.add(birthday);
        data.add(phone);
        data.add(email);
        data.add(location);
        data.add(location2);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(
                new UserProfileClickListener(this, options1Item, options2Item, options3Item));
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
