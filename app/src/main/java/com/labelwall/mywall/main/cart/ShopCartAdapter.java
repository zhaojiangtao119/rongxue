package com.labelwall.mywall.main.cart;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.List;

/**
 * Created by Administrator on 2018-01-23.
 */

public class ShopCartAdapter extends MultipleRecyclerViewAdapter{

    private final ShopCartDelegate DELEGATE;
    final long userId = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    public ShopCartAdapter(List<MultipleItemEntity> data, ShopCartDelegate delegate) {
        super(data);
        this.DELEGATE = delegate;
        addItemType(ItemType.SHOP_CART, R.layout.item_shop_cart);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder holder, final MultipleItemEntity itemEntity) {
        super.convert(holder, itemEntity);
        switch (holder.getItemViewType()) {
            case ItemType.SHOP_CART:
                final int productId = itemEntity.getField(ShopCartDataField.PRODUCT_ID);
                final int id = itemEntity.getField(MultipleFields.ID);
                final String name = itemEntity.getField(ShopCartDataField.NAME);
                final String img = itemEntity.getField(ShopCartDataField.MAIN_IMAGE);
                final String subtitle = itemEntity.getField(ShopCartDataField.SUBTITLE);
                final double totalPrice = itemEntity.getField(ShopCartDataField.TOTAL_PRICE);
                final double productPrice = itemEntity.getField(ShopCartDataField.PRODUCT_PRICE);
                final int quantity = itemEntity.getField(ShopCartDataField.QUANTITY);
                final Integer isChecked = itemEntity.getField(ShopCartDataField.IS_CHECKED);
                //取出控件
                final AppCompatImageView imgThumb = holder.getView(R.id.image_item_shop_cart);
                final AppCompatTextView tvTitle = holder.getView(R.id.tv_item_shop_cart_title);
                final AppCompatTextView tvDesc = holder.getView(R.id.tv_item_shop_cart_desc);
                final AppCompatTextView tvPrice = holder.getView(R.id.tv_item_shop_cart_price);
                final IconTextView iconMinus = holder.getView(R.id.icon_item_minus);
                final IconTextView iconPlus = holder.getView(R.id.icon_item_plus);
                final AppCompatTextView tvCount = holder.getView(R.id.tv_item_shop_cart_count);
                final IconTextView iconIsSelected = holder.getView(R.id.icon_item_shop_cart);

                if (isChecked == 1) {
                    //判断当前商品是否选中
                    iconIsSelected.setTextColor(ContextCompat.getColor(DELEGATE.getContext(), R.color.app_title));
                } else {
                    iconIsSelected.setTextColor(Color.GRAY);
                }
                //添加选中的点击事件
                iconIsSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Integer currentSelect = itemEntity.getField(ShopCartDataField.IS_CHECKED);
                        if (currentSelect == 1) {
                            iconIsSelected.setTextColor(Color.GRAY);
                            //TODO 请求服务器取消该商品的选择，请求参数：productId,userId
                            clickChecked(productId, "un_select");
                        } else {
                            iconIsSelected.setTextColor(ContextCompat.getColor(DELEGATE.getContext(), R.color.app_title));
                            //TODO 请求服务器选择该商品，请求参数：productId,userId
                            clickChecked(productId, "select");
                        }
                    }
                });
                tvTitle.setText(name);
                tvDesc.setText(subtitle);
                tvPrice.setText("￥" + String.valueOf(productPrice));
                tvCount.setText(String.valueOf(quantity));
                Glide.with(mContext)
                        .load(img)
                        .apply(OPTIONS)
                        .into(imgThumb);
                //商品数量减一
                iconMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Integer currentCount = itemEntity.getField(ShopCartDataField.QUANTITY);
                        if (currentCount > 1) {//商品减少数量不能小于1
                            //TODO 请求服务端数量减一，请求参数userId,productId,quantity
                            Integer updateProductNum = currentCount - 1;
                            uploadShopCartProductNum(updateProductNum, productId);
                        }
                    }
                });
                //商品数量减一
                iconPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Integer currentCount = itemEntity.getField(ShopCartDataField.QUANTITY);
                        //TODO 请求服务端数量加一
                        Integer updateProductNum = currentCount + 1;
                        uploadShopCartProductNum(updateProductNum, productId);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void clickChecked(int productId, String url) {
        RestClient.builder()
                .url("app/shopcart/" + url)
                .loader(mContext)
                .params("userId", userId)
                .params("productId", productId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final List<MultipleItemEntity> data =
                                new ShopCartDataConverter().setJsonData(response).convert();
                        //返回的新数据存储在全局回调中
                        final IGlobalCallback<List<MultipleItemEntity>> callback =
                                CallbackManager.getInstance().getCallback(CallbackType.SHOP_CART_PRODUCTS);
                        if (callback != null) {
                            callback.executeCallback(data);
                        }
                    }
                })
                .build()
                .put();
    }

    private void uploadShopCartProductNum(Integer updateProductNum, Integer productId) {
        RestClient.builder()
                .url("app/shopcart/update_quantity")
                .loader(mContext)
                .params("userId", userId)
                .params("productId", productId)
                .params("quantity", updateProductNum)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final List<MultipleItemEntity> data =
                                new ShopCartDataConverter().setJsonData(response).convert();
                        //返回的新数据存储在全局回调中
                        final IGlobalCallback<List<MultipleItemEntity>> callback =
                                CallbackManager.getInstance().getCallback(CallbackType.SHOP_CART_PRODUCTS);
                        if (callback != null) {
                            callback.executeCallback(data);
                        }
                    }
                })
                .build()
                .put();
    }
}
