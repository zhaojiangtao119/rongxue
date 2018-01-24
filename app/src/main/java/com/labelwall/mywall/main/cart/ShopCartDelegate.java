package com.labelwall.mywall.main.cart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewStubCompat;
import android.view.View;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-01-04.
 */

public class ShopCartDelegate extends BottomItemDelegate
        implements ISuccess {
    private int mCurrentItemCount = 0;
    private int mTotalCount = 0;

    @BindView(R2.id.rv_shop_cart_item)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.shop_cart_total_price)
    AppCompatTextView mTVShopCartTotalPrice = null;
    @BindView(R2.id.icon_cart_select_all)
    IconTextView mSelectAll = null;
    @BindView(R2.id.tv_top_shop_cart_clear)
    AppCompatTextView mClearShopCart = null;
    @BindView(R2.id.tv_top_shop_cart_clear_selected)
    AppCompatTextView mClearSelectShopCart = null;
    @BindView(R2.id.stub_no_item)
    ViewStubCompat mStubNoItem = null;

    private ShopCartAdapter mAdapter = null;
    private boolean mIsCheckedAll = false;
    private double mShopCartTotalPrice = 0.00;
    private long mUserId = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private boolean mStubIsInflate = false;

    @OnClick
    void onClickSelectAll() {
        if (mIsCheckedAll) {//判断是否全部选中
            //若是全部选中，则取消全部选中
            mSelectAll.setTextColor(Color.GRAY);
            clickSelectAll("app_un_select_all");
        } else {
            mSelectAll.setTextColor(ContextCompat.getColor(_mActivity, R.color.app_title));
            clickSelectAll("app_select_all");
        }
    }

    private void clickSelectAll(String url) {
        RestClient.builder()
                .url("app/shopcart/" + url)
                .loader(getContext())
                .params("userId", mUserId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final ArrayList<MultipleItemEntity> data =
                                new ShopCartDataConverter().setJsonData(response).convert();
                        uploadShopCartProduct(data);
                    }
                })
                .build()
                .put();
    }

    @OnClick(R2.id.tv_top_shop_cart_clear_selected)
    void onClickRemoveSelectedItem() {
        //获取adapter中的数据
        final List<MultipleItemEntity> data = mAdapter.getData();
        //获取要删除的数据
        List<MultipleItemEntity> deleteEntities = new ArrayList<>();
        for (MultipleItemEntity entity : data) {
            final Integer isChecked = entity.getField(ShopCartDataField.IS_CHECKED);
            if (isChecked == 1) {
                //item是选中状态
                deleteEntities.add(entity);
            }
        }
        for (MultipleItemEntity entity : deleteEntities) {
            int removePosition;
            final int entityPosition = entity.getField(ShopCartDataField.POSITION);
            if (entityPosition > mCurrentItemCount - 1) {
                removePosition = entityPosition - (mTotalCount - mCurrentItemCount);
            } else {
                removePosition = entityPosition;
            }
            if (removePosition <= mAdapter.getItemCount()) {
                mAdapter.remove(removePosition);
                mCurrentItemCount = mAdapter.getItemCount();
                //更新数据
                mAdapter.notifyItemRangeChanged(removePosition, mAdapter.getItemCount());
            }
        }
        checkItemCount();
    }

    @OnClick(R2.id.tv_top_shop_cart_clear)
    void onClickClearAll() {
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        checkItemCount();
    }

    private void checkItemCount() {//判断购物车是否为null
        final int itemCount = mAdapter.getItemCount();
        if (itemCount == 0) {
            mStubNoItem.setOnInflateListener(new ViewStubCompat.OnInflateListener() {
                @Override
                public void onInflate(ViewStubCompat stub, View inflated) {
                    mStubIsInflate = true;
                }
            });
            if (!mStubIsInflate) {
                //只能初始化一次mStubNoItem.inflate();
                final View stubView = mStubNoItem.inflate();
                final AppCompatTextView tvToBye =
                        (AppCompatTextView) stubView.findViewById(R.id.tv_stub_to_buy);
                tvToBye.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                        Toast.makeText(getContext(), "要去购物", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            mStubNoItem.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mStubNoItem.setVisibility(View.GONE);
        }
    }

    @OnClick(R2.id.tv_shop_cart_pay)
    void onClickPay() {//结算购物车，创建订单
        final String orderUrl = "";
        final WeakHashMap<String, Object> orderParams = new WeakHashMap<>();
        orderParams.put("userId", 123);
        orderParams.put("amount", 0.01);
        orderParams.put("comment", "测试支付");

    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_cart;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        final CallbackManager manager = CallbackManager.getInstance()
                .addCallback(CallbackType.SHOP_CART_PRODUCTS, new IGlobalCallback<ArrayList<MultipleItemEntity>>() {
                    @Override
                    public void executeCallback(ArrayList<MultipleItemEntity> data) {
                        uploadShopCartProduct(data);
                    }
                });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //加载数据
        RestClient.builder()
                .url("app/shopcart/app_get_cart_list/" + mUserId)
                .success(this)
                .build()
                .get();
    }

    @Override
    public void onSuccess(String response) {
        final ArrayList<MultipleItemEntity> data =
                new ShopCartDataConverter().setJsonData(response).convert();
        uploadShopCartProduct(data);
    }

    private void uploadShopCartProduct(ArrayList<MultipleItemEntity> data) {
        if (data != null && data.size() > 0) {
            MultipleItemEntity entity = data.get(0);
            mShopCartTotalPrice = entity.getField(ShopCartDataField.TOTAL_PRICE);
            mIsCheckedAll = entity.getField(ShopCartDataField.ALLCHECKED);
            mTVShopCartTotalPrice.setText("￥" + String.valueOf(mShopCartTotalPrice));
            if (mIsCheckedAll) {//判断是否全部选中
                mSelectAll.setTextColor(ContextCompat.getColor(_mActivity, R.color.app_title));
            } else {
                mSelectAll.setTextColor(Color.GRAY);
            }
        }
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ShopCartAdapter(data, this);
        mRecyclerView.setAdapter(mAdapter);
        checkItemCount();
    }

   /* @Override
    public void onStart() {
        super.onStart();
        //加载数据
        RestClient.builder()
                .url("app/shopcart/app_get_cart_list/" + mUserId)
                .success(this)
                .build()
                .get();
    }*/

   /* @Override
    public void onResume() {
        super.onResume();
        RestClient.builder()
                .url("app/shopcart/app_get_cart_list/" + mUserId)
                .success(this)
                .build()
                .get();
    }*/
}
