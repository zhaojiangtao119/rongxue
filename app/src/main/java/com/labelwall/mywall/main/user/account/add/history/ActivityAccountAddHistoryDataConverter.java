package com.labelwall.mywall.main.user.account.add.history;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.main.user.account.ActivityAccountOrderField;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-02-22.
 */

public class ActivityAccountAddHistoryDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray accountOrderList = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = accountOrderList.size();
        for (int i = 0; i < size; i++) {
            final JSONObject accountOrder = accountOrderList.getJSONObject(i);
            final int id = accountOrder.getInteger("id");
            final int activityId = accountOrder.getInteger("accountId");
            final String orderNo = accountOrder.getString("orderNo");
            final String typeDesc = accountOrder.getString("typeDesc");
            final double orderPrice = accountOrder.getDouble("orderPrice");
            final String orderInfo = accountOrder.getString("orderInfo");
            final String createTime = accountOrder.getString("createTime");
            final String statusDesc = accountOrder.getString("statusDesc");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ItemType.ACCOUNT_ORDER_LIST)
                    .setField(MultipleFields.ID, id)
                    .setField(ActivityAccountOrderField.ACCOUNT_ID, activityId)
                    .setField(ActivityAccountOrderField.ORDER_NO, orderNo)
                    .setField(ActivityAccountOrderField.TYPE_DESC, typeDesc)
                    .setField(ActivityAccountOrderField.ORDER_PRICE, orderPrice)
                    .setField(ActivityAccountOrderField.ORDER_INFO, orderInfo)
                    .setField(ActivityAccountOrderField.ORDER_CREATE_TIME, createTime)
                    .setField(ActivityAccountOrderField.STATUS_DESC, statusDesc)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }

}
