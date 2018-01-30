package com.labelwall.mywall.main.user.address;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.profile.location.JsonBean;
import com.labelwall.mywall.main.user.profile.location.LocationJsonReader;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Administrator on 2018-01-29.
 * 配送地址的添加与修改
 */

public class AddressAddDelegate extends WallDelegate {

    @BindView(R2.id.tv_address_manager_title)
    AppCompatTextView mAddressTitle = null;
    @BindView(R2.id.et_receiver_name)
    TextInputEditText mReceiverName = null;
    @BindView(R2.id.et_receiver_mobile)
    TextInputEditText mReceiverMobile = null;
    @BindView(R2.id.et_receiver_zip)
    TextInputEditText mReceiverZip = null;
    @BindView(R2.id.et_receiver_address)
    TextInputEditText mReceiverAddress = null;
    @BindView(R2.id.tv_arrow_value)
    AppCompatTextView mLocationValue = null;

    private String mName = null;
    private String mMobile = null;
    private String mZip = null;
    private String mAddress = null;
    private String mProvince = null;
    private String mCity = null;
    private String mCounty = null;

    private final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private final Integer SHOPPING_ID;

    private ArrayList<JsonBean> options1Item = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Item = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Item = new ArrayList<>();

    @OnClick(R2.id.rl_address_location)
    void onClickAddressLocation() {//选择省市区县
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //确定选择的回调监听
                //返回的分别是三个级别的选中位置
                mProvince = options1Item.get(options1).getPickerViewText();
                mCity = options2Item.get(options1).get(options2);
                mCounty = options3Item.get(options1).get(options2).get(options3);
                String location;
                if (mProvince.equals(mCity)) {
                    location = mProvince + "-" + mCounty;
                } else {
                    location = mProvince + "-" + mCity + "-" + mCounty;
                }
                mLocationValue.setText(location);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();
        //noinspection unchecked
        pvOptions.setPicker(options1Item, options2Item, options3Item);
        pvOptions.show();
    }


    @OnClick(R2.id.btn_submit_add)
    void onClickAddAddress() {
        if (SHOPPING_ID == null) {
            //1.添加地址
            if (checkForm()) {
                manageReceiverInfo("app/shopping/add");
            }
        } else {
            //2.修改地址
            if (checkForm()) {
                manageReceiverInfo("app/shopping/modify/" + SHOPPING_ID);
            }
        }
    }

    private void manageReceiverInfo(String url) {
        RestClient.builder()
                .url(url)
                .loader(getContext())
                .params("userId", USER_ID)
                .params("receiverName", mName)
                .params("receiverMobile", mMobile)
                .params("receiverAddress", mAddress)
                .params("receiverZip", mZip)
                .params("receiverProvince", mProvince)
                .params("receiverCity", mCity)
                .params("receiverCounty", mCounty)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //添加成功之后跳转到地址列表
                        final JSONObject data = JSON.parseObject(response);
                        final int status = data.getInteger("status");
                        if (status == 0) {
                            getSupportDelegate().startWithPop(new AdressDelegate(null));
                        }
                    }
                })
                .build()
                .post();
    }

    private boolean checkForm() {
        mName = mReceiverName.getText().toString();
        mMobile = mReceiverMobile.getText().toString();
        mZip = mReceiverZip.getText().toString();
        mAddress = mReceiverAddress.getText().toString();
        boolean isChenckPass = true;

        if (mName.isEmpty()) {
            mReceiverName.setError("请输入收件人姓名");
            isChenckPass = false;
        } else if (mMobile.isEmpty()) {
            mReceiverMobile.setError("请输入手机号");
            isChenckPass = false;
        } else if (mZip.isEmpty()) {
            mReceiverZip.setError("请输入邮编");
            isChenckPass = false;
        } else if (mAddress.isEmpty()) {
            mReceiverAddress.setError("请输入详细收货地址");
            isChenckPass = false;
        }
        return isChenckPass;
    }

    public AddressAddDelegate(Integer shoppingId) {
        this.SHOPPING_ID = shoppingId;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_address_add;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initJsonData();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (SHOPPING_ID != null) {//修改配送地址
            uploadModifyAddressInfo();
        }
    }

    private void uploadModifyAddressInfo() {
        RestClient.builder()
                .url("app/shopping/" + USER_ID + "/" + SHOPPING_ID)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //绑定数据
                        bindModifyInfo(response);
                    }
                })
                .build()
                .get();
    }

    private void bindModifyInfo(String response) {
        final JSONObject shoppingVo = JSON.parseObject(response).getJSONObject("data");
        final String name = shoppingVo.getString("receiverName");
        final String mobile = shoppingVo.getString("receiverMobile");
        final String province = shoppingVo.getString("receiverProvince");
        final String city = shoppingVo.getString("receiverCity");
        final String county = shoppingVo.getString("receiverCounty");
        final String address = shoppingVo.getString("receiverAddress");
        final String zip = shoppingVo.getString("receiverZip");

        mAddressTitle.setText("修改地址");
        mReceiverName.setText(name);
        mReceiverMobile.setText(mobile);
        mReceiverZip.setText(zip);
        mReceiverAddress.setText(address);
        if (province != null && city != null && county != null) {
            String location;
            if (province.equals(city)) {
                location = province + "-" + county;
            } else {
                location = province + "-" + city + "-" + county;
            }
            mLocationValue.setText(location);
        }
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
