package com.labelwall.mywall.main.user.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-26.
 * 订单列表的UI需要的数据
 * orderNo
 * payment
 * statusDesc
 * paymentTypeDesc
 * createTime
 */

public class OrderListDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject pageInfo = JSON.parseObject(getJsonData()).getJSONObject("data");
        final JSONArray orderArray = pageInfo.getJSONArray("list");
        final int size = orderArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject order = orderArray.getJSONObject(i);
            final Long orderNo = order.getLong("orderNo");
            final BigDecimal totalPrice = order.getBigDecimal("payment");
            final String paymentTypeDesc = order.getString("paymentTypeDesc");
            final String statusDesc = order.getString("statusDesc");
            final String createTime = order.getString("createTime");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ItemType.ORDER_LIST)
                    .setField(OrderListField.ORDER_NO, orderNo)
                    .setField(OrderListField.TOTAL_PRICE, totalPrice)
                    .setField(OrderListField.PAYMENT_TYPE, paymentTypeDesc)
                    .setField(OrderListField.STATUS, statusDesc)
                    .setField(OrderListField.CREATE_TIME, createTime)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
