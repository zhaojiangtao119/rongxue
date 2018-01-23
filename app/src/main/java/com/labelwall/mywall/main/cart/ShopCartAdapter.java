package com.labelwall.mywall.main.cart;

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

import java.util.List;

/**
 * Created by Administrator on 2018-01-23.
 */

public class ShopCartAdapter extends
        BaseMultiItemQuickAdapter<MultipleItemEntity, ShopCartAdapter.ShopCartViewHolder> {

    private final ShopCartDelegate DELEGATE;

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
    protected void convert(ShopCartViewHolder holder, MultipleItemEntity itemEntity) {
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
                final int isChecked = itemEntity.getField(ShopCartDataField.IS_CHECKED);
                //取出控件
                final AppCompatImageView imgThumb = holder.getView(R.id.image_item_shop_cart);
                final AppCompatTextView tvTitle = holder.getView(R.id.tv_item_shop_cart_title);
                final AppCompatTextView tvDesc = holder.getView(R.id.tv_item_shop_cart_desc);
                final AppCompatTextView tvPrice = holder.getView(R.id.tv_item_shop_cart_price);
                final IconTextView iconMinus = holder.getView(R.id.icon_item_minus);
                final IconTextView iconPlus = holder.getView(R.id.icon_item_plus);
                final AppCompatTextView tvCount = holder.getView(R.id.tv_item_shop_cart_count);
                final IconTextView iconIsSelected = holder.getView(R.id.icon_item_shop_cart);
                if (isChecked == 1) {//判断当前商品是否选中
                    iconIsSelected.setTextColor(ContextCompat.getColor(DELEGATE.getContext(), R.color.app_title));
                }
                tvTitle.setText(name);
                tvDesc.setText(subtitle);
                tvPrice.setText("￥" + String.valueOf(productPrice));
                tvCount.setText(String.valueOf(quantity));
                Glide.with(mContext)
                        .load(img)
                        .apply(OPTIONS)
                        .into(imgThumb);
                break;
            default:
                break;
        }
    }

    public class ShopCartViewHolder extends BaseViewHolder {

        public ShopCartViewHolder(View view) {
            super(view);
        }
    }
}
