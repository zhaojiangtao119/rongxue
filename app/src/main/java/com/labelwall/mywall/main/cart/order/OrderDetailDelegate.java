package com.labelwall.mywall.main.cart.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.main.WallBottomDelegate;
import com.labelwall.mywall.main.cart.ShopCartDataField;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.pay.FastPay;
import com.labelwall.mywall.util.pay.IAIPayResultListener;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Administrator on 2018-01-25.
 * 生成的订单的详情页面
 */

public class OrderDetailDelegate extends WallDelegate implements IAIPayResultListener {

    @BindView(R2.id.tv_order_no_value)
    AppCompatTextView mTVOrderNo = null;
    @BindView(R2.id.tv_order_payment_value)
    AppCompatTextView mOrderPayment = null;
    @BindView(R2.id.rv_order_product_item)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.tv_order_status_value)
    AppCompatTextView mOrderStatus = null;

    private final JSONObject ORDERINFO;
    private OrderProductAdapter mAdapter = null;
    private Long mOrderNo = null;
    private final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    @OnClick(R2.id.btn_cancel_order)
    void onClickCancelOrder() {
        //取消订单，orderNo，userId
        RestClient.builder()
                .url("")
                .params("orderNo", mOrderNo)
                .params("userId", USER_ID)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //取消订单后，跳转到购物车先
                        final int shopCartTab = 3;
                        final WallBottomDelegate wallBottomDelegate = getParentDelegate();
                        final BottomItemDelegate shopCartDelegate =
                                wallBottomDelegate.getItemDelegates().get(shopCartTab);
                        wallBottomDelegate.getSupportDelegate()
                                .showHideFragment(shopCartDelegate, OrderDetailDelegate.this);
                        wallBottomDelegate.changeColor(shopCartTab);
                    }
                })
                .build()
                .put();

    }
    @OnClick(R2.id.btn_pay_order)
    void onClickPayOrder() {
        //支付订单
        //进行具体的订单支付
        FastPay.create(OrderDetailDelegate.this)
                .setPayResultListener(OrderDetailDelegate.this)
                .setOrderId(mOrderNo)
                .beginPayDialog();
    }


    public OrderDetailDelegate(JSONObject orderinfo) {
        this.ORDERINFO = orderinfo;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView();
        bindData();
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
    }

    private void bindData() {
        //获取订单信息：orderNo，payment
        if (ORDERINFO != null) {
            final JSONObject orderVo = ORDERINFO.getJSONObject("data");
            mOrderNo = orderVo.getLong("orderNo");
            final BigDecimal orderPayment = orderVo.getBigDecimal("payment");
            mTVOrderNo.setText(String.valueOf(mOrderNo));
            mOrderPayment.setText("￥" + String.valueOf(orderPayment));
            final String orderStatus = orderVo.getString("statusDesc");
            mOrderStatus.setText(orderStatus);

            final JSONArray orderProductItem = orderVo.getJSONArray("orderItemVoList");
            bindOrderProductData(orderProductItem);
        }
    }

    private void bindOrderProductData(JSONArray orderProductItem) {
        List<MultipleItemEntity> orderProductList = new ArrayList<>();
        final int size = orderProductItem.size();
        for (int i = 0; i < size; i++) {
            final JSONObject orderItemVo = orderProductItem.getJSONObject(i);
            final int productId = orderItemVo.getInteger("productId");
            final String productName = orderItemVo.getString("productName");
            final String productImage = orderItemVo.getString("productImage");
            final BigDecimal productPrice = orderItemVo.getBigDecimal("currentUnitPrice");
            final Integer productQuantity = orderItemVo.getInteger("quantity");
            final BigDecimal productTotalPrice = orderItemVo.getBigDecimal("totalPrice");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(ItemType.ORDER_PRODUCT)
                    .setField(ShopCartDataField.PRODUCT_ID, productId)
                    .setField(ShopCartDataField.NAME, productName)
                    .setField(ShopCartDataField.MAIN_IMAGE, productImage)
                    .setField(ShopCartDataField.PRODUCT_PRICE, productPrice)
                    .setField(ShopCartDataField.QUANTITY, productQuantity)
                    .setField(ShopCartDataField.PRODUCT_TOTAL_PRICE, productTotalPrice)
                    .build();
            orderProductList.add(entity);
        }
        if (orderProductList.size() > 0) {
            //将数据放在RecyclerView中展示
            mAdapter = new OrderProductAdapter(orderProductList, OrderDetailDelegate.this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void onPaySuccess() {

    }

    @Override
    public void onPaying() {

    }

    @Override
    public void onPayFail() {

    }

    @Override
    public void onPayCancel() {

    }

    @Override
    public void onPayConnectError() {

    }
}
