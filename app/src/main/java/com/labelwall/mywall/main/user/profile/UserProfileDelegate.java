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
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;
import java.util.List;

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
        initJsonData();
        uploadUserProfileItem();

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

    private void initJsonData() {   //解析数据
        //  获取json数据
        String JsonData = LocationJsonReader.getJson(getContext(), "province_data.json");
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Item = jsonBean;
        final int provinceJsonListSize = jsonBean.size();
        for (int i = 0; i < provinceJsonListSize; i++) {//遍历省份
            ArrayList<String> cityProvinceList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> countyProvinceList = new ArrayList<>();//该省的所有地区列表（第三极）
            List<JsonBean.CityBean> cityJsonList = jsonBean.get(i).getCityList();
            final int cityJsonListSize = cityJsonList.size();
            for (int j = 0; j < cityJsonListSize; j++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(j).getName();
                cityProvinceList.add(cityName);//添加城市
                ArrayList<String> countyCityList = new ArrayList<>();//该城市的所有地区列表
                List<String> countyJsonList = cityJsonList.get(j).getArea();
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (countyJsonList == null || countyJsonList.size() == 0) {
                    countyCityList.add("");
                } else {
                    for (int k = 0; k < countyJsonList.size(); k++) {//该城市对应地区所有数据
                        String AreaName = countyJsonList.get(k);
                        countyCityList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                countyProvinceList.add(countyCityList);//添加该省所有地区数据
            }
            options2Item.add(cityProvinceList);
            options3Item.add(countyProvinceList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            org.json.JSONArray data = new org.json.JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
