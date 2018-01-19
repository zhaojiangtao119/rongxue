package com.labelwall.mywall;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.labelwall.mywall.main.user.profile.location.JsonBean;
import com.labelwall.mywall.main.user.profile.location.LocationJsonReader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView mTvAddress;

    private ArrayList<JsonBean> options1Item = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Item = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Item = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setClickListener();
        initJsonData();
    }

    private void initView() {
        mTvAddress = (TextView) findViewById(R.id.tv_address);
    }

    private void setClickListener() {
        mTvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerView();
            }
        });
    }

    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //确定选择的回调监听
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();
        pvOptions.setPicker(options1Item, options2Item, options3Item);
        pvOptions.show();
    }

    private void initJsonData() {   //解析数据
        //  获取json数据
        String JsonData = LocationJsonReader.getJson(this, "province_data.json");
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
}
