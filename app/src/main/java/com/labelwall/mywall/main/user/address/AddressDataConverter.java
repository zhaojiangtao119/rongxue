package com.labelwall.mywall.main.user.address;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-27.
 */

public class AddressDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray shoppingArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = shoppingArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject shopping = shoppingArray.getJSONObject(i);
            final int id = shopping.getInteger("id");
            final String receiverName = shopping.getString("receiverName");
            final String phone = shopping.getString("receiverPhone");
            final String mobile = shopping.getString("receiverMobile");
            final String province = shopping.getString("receiverProvince");
            final String city = shopping.getString("receiverCity");
            final String county = shopping.getString("receiverCounty");
            final String address = shopping.getString("receiverAddress");
            final String zip = shopping.getString("receiverZip");
            final Integer selected = shopping.getInteger("selected");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ItemType.ADDRESS_LIST)
                    .setField(MultipleFields.ID, id)
                    .setField(AddressField.RECEIVER_NAME, receiverName)
                    .setField(AddressField.RECEIVER_PHONE, phone)
                    .setField(AddressField.RECEIVER_MOBILE, mobile)
                    .setField(AddressField.RECEIVER_PROVINCE, province)
                    .setField(AddressField.RECEIVER_CITY, city)
                    .setField(AddressField.RECEIVER_COUNTY, county)
                    .setField(AddressField.RECEIVER_ADDRESS, address)
                    .setField(AddressField.RECEIVER_ZIP, zip)
                    .setField(AddressField.SELECTED, selected)
                    .build();

            ENTITIES.add(entity);
        }
        return ENTITIES;
    }

}
