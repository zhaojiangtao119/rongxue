package com.labelwall.mywall.util.location;

import com.google.gson.Gson;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.profile.location.JsonBean;
import com.labelwall.mywall.main.user.profile.location.LocationJsonReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-02-11.
 */

public class LocationUtil {

    public static final String ITEM1 = "options1Item";
    public static final String ITEM2 = "options2Item";
    public static final String ITEM3 = "options3Item";

    private ArrayList<JsonBean> options1Item = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Item = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Item = new ArrayList<>();
    private final WallDelegate DELEGATE;

    private LocationUtil(WallDelegate delegate) {
        this.DELEGATE = delegate;
    }

    public static LocationUtil create(WallDelegate delegate) {
        return new LocationUtil(delegate);
    }

    public LocationUtil initJsonData() {   //解析数据
        //  获取json数据
        String JsonData = LocationJsonReader.getJson(DELEGATE.getContext(), "province_data.json");
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
        return this;
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

    public Map<String, Object> getLocationData() {
        Map<String, Object> locationData = new HashMap<>();
        locationData.put(ITEM1, options1Item);
        locationData.put(ITEM2, options2Item);
        locationData.put(ITEM3, options3Item);
        return locationData;
    }
}
