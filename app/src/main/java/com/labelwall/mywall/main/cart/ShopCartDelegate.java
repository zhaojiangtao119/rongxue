package com.labelwall.mywall.main.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-01-04.
 */

public class ShopCartDelegate extends BottomItemDelegate implements ISuccess {

    @BindView(R2.id.rv_shop_cart_item)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.shop_cart_total_price)
    AppCompatTextView mShopCartTotalPrice = null;
    @BindView(R2.id.icon_cart_select_all)
    IconTextView mSelectAll = null;

    private ShopCartAdapter mAdapter = null;
    private boolean isCheckedAll = false;
    private double shopCartTotalPrice = 0.00;

    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_cart;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final long userId = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
        //加载数据
        RestClient.builder()
                .url("shopcart/app_get_cart_list/" + userId)
                .success(this)
                .build()
                .get();
    }

    @Override
    public void onSuccess(String response) {
        final ArrayList<MultipleItemEntity> data =
                new ShopCartDataConverter().setJsonData(response).convert();
        if (data != null && data.size() > 0) {
            MultipleItemEntity entity = data.get(0);
            shopCartTotalPrice = entity.getField(ShopCartDataField.TOTAL_PRICE);
            isCheckedAll = entity.getField(ShopCartDataField.ALLCHECKED);
            mShopCartTotalPrice.setText("￥" + String.valueOf(shopCartTotalPrice));
            if (isCheckedAll) {//判断是否全部选中
                mSelectAll.setTextColor(ContextCompat.getColor(_mActivity, R.color.app_title));
            }
        }
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ShopCartAdapter(data,this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
