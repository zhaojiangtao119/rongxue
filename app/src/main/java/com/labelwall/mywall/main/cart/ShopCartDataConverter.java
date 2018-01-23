package com.labelwall.mywall.main.cart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-01-23.
 */

public class ShopCartDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject data = JSON.parseObject(getJsonData()).getJSONObject("data");
        final double totalPrice = data.getDouble("cartTotalPrice");
        final boolean allChecked = data.getBoolean("allChecked");
        final JSONArray cartProductArray = data.getJSONArray("cartProductVoList");
        final int size = cartProductArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject cartProduct = cartProductArray.getJSONObject(i);
            final int quantity = cartProduct.getInteger("quantity");
            final double productPrice = cartProduct.getDouble("productPrice");
            final String name = cartProduct.getString("productName");
            final String productSubtitle = cartProduct.getString("productSubtitle");
            final String productMainImage = cartProduct.getString("productMainImage");
            //1-选中了该商品，0-未选中该商品
            final int isChecked = cartProduct.getInteger("productChecked");
            final int cartProductId = cartProduct.getInteger("id");
            final int productId = cartProduct.getInteger("productId");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ItemType.SHOP_CART)
                    .setField(MultipleFields.ID, cartProductId)
                    .setField(ShopCartDataField.TOTAL_PRICE, totalPrice)
                    .setField(ShopCartDataField.ALLCHECKED, allChecked)
                    .setField(ShopCartDataField.QUANTITY, quantity)
                    .setField(ShopCartDataField.PRODUCT_PRICE, productPrice)
                    .setField(ShopCartDataField.NAME, name)
                    .setField(ShopCartDataField.SUBTITLE, productSubtitle)
                    .setField(ShopCartDataField.MAIN_IMAGE, productMainImage)
                    .setField(ShopCartDataField.IS_CHECKED, isChecked)
                    .setField(ShopCartDataField.PRODUCT_ID, productId)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
