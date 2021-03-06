package com.labelwall.mywall.main.cart.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.labelwall.mywall.main.user.address.AdressDelegate;
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

    @BindView(R2.id.tv_order_payment_value)
    AppCompatTextView mOrderPayment = null;
    @BindView(R2.id.rv_order_product_item)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.tv_receiver_name)
    AppCompatTextView mReceiverUsername = null;
    @BindView(R2.id.tv_receiver_mobile)
    AppCompatTextView mReceiverMobile = null;
    @BindView(R2.id.tv_receiver_address)
    AppCompatTextView mReceiverAddress = null;

    @OnClick(R2.id.icon_select_address)
    void onClickAddressSelect() {//选择配送地址
        getSupportDelegate().start(new AdressDelegate(mOrderNo));
    }

    private final Long ORDER_NO;

    private OrderProductAdapter mAdapter = null;
    private Long mOrderNo = null;
    private final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    //    @OnClick(R2.id.btn_cancel_order)
//    void onClickCancelOrder() {
//        //取消订单，orderNo，userId
//        RestClient.builder()
//                .url("")
//                .params("orderNo", mOrderNo)
//                .params("userId", USER_ID)
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        //取消订单后，跳转到购物车先
//                        final int shopCartTab = 3;
//                        final WallBottomDelegate wallBottomDelegate = getParentDelegate();
//                        final BottomItemDelegate shopCartDelegate =
//                                wallBottomDelegate.getItemDelegates().get(shopCartTab);
//                        wallBottomDelegate.getSupportDelegate()
//                                .showHideFragment(shopCartDelegate, OrderDetailDelegate.this);
//                        wallBottomDelegate.changeColor(shopCartTab);
//                    }
//                })
//                .build()
//                .put();
//
//    }
    @OnClick(R2.id.btn_pay_order)
    void onClickPayOrder() {
        //支付订单
        //进行具体的订单支付
        FastPay.create(OrderDetailDelegate.this)
                .setPayResultListener(OrderDetailDelegate.this)
                .setOrderId(mOrderNo)
                .beginPayDialog();
    }


    public OrderDetailDelegate(Long orderNo) {
        this.ORDER_NO = orderNo;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        RestClient.builder()
                .url("app/order/detail")
                .loader(getContext())
                .params("userId", USER_ID)
                .params("orderNo", ORDER_NO)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject data = JSON.parseObject(response);
                        bindData(data);
                    }
                })
                .build().get();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
    }

    private void bindData(JSONObject data) {
        //获取订单信息：orderNo，payment
        if (data != null) {
            final JSONObject orderVo = data.getJSONObject("data");
            final BigDecimal orderPayment = orderVo.getBigDecimal("payment");
            mOrderNo = orderVo.getLong("orderNo");
            mOrderPayment.setText("￥" + String.valueOf(orderPayment));

            //绑定收货人信息
            final JSONObject shopping = orderVo.getJSONObject("shoppingVo");
            final String name = shopping.getString("receiverName");
            final String mobile = shopping.getString("receiverMobile");
            final String province = shopping.getString("receiverProvince");
            final String city = shopping.getString("receiverCity");
            final String county = shopping.getString("receiverCounty");
            final String address = shopping.getString("address");
            mReceiverUsername.setText(name);
            mReceiverMobile.setText(mobile);
            mReceiverAddress.setText(province + city + county + address);
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
        //这里监听的是支付宝同步返回的支付结果，
        // 最终的支付结果的需要服务端的验签结果
        Log.e("支付结果：", "支付成功");
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}
