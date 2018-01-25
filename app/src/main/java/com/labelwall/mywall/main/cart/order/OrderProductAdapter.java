package com.labelwall.mywall.main.cart.order;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.cart.ShopCartDataField;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018-01-25.
 */

public class OrderProductAdapter extends MultipleRecyclerViewAdapter {

    private final WallDelegate DELEGATE;

    private final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    public OrderProductAdapter(List<MultipleItemEntity> data, WallDelegate delegate) {
        super(data);
        this.DELEGATE = delegate;
        addItemType(ItemType.ORDER_PRODUCT, R.layout.item_order_product);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()) {
            case ItemType.ORDER_PRODUCT:
                final int productId = item.getField(ShopCartDataField.PRODUCT_ID);
                final String productName = item.getField(ShopCartDataField.NAME);
                final String productImage = item.getField(ShopCartDataField.MAIN_IMAGE);
                final BigDecimal productPrice = item.getField(ShopCartDataField.PRODUCT_PRICE);
                final Integer productQuantity = item.getField(ShopCartDataField.QUANTITY);
                final BigDecimal productTotalPrice = item.getField(ShopCartDataField.PRODUCT_TOTAL_PRICE);

                final AppCompatImageView mainImage = helper.getView(R.id.image_item_product_order);
                final AppCompatTextView name = helper.getView(R.id.tv_item_order_product_title);
                final AppCompatTextView price = helper.getView(R.id.tv_item_order_product_price);
                final AppCompatTextView count = helper.getView(R.id.tv_item_order_product_num);
                final AppCompatTextView totalPricel = helper.getView(R.id.tv_order_product_total_price);

                Glide.with(DELEGATE.getContext())
                        .load(productImage)
                        .apply(OPTIONS)
                        .into(mainImage);
                name.setText(productName);
                price.setText("￥" + String.valueOf(productPrice));
                count.setText("×" + String.valueOf(productQuantity));
                totalPricel.setText("￥" + String.valueOf(productTotalPrice));
                break;
            default:
                break;
        }
    }
}
