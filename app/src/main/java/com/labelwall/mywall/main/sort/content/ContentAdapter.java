package com.labelwall.mywall.main.sort.content;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.sort.content.detail.ProductDetailDelegate;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018-01-20.
 */

public class ContentAdapter extends MultipleRecyclerViewAdapter {

    private final WallDelegate DELEGATE;
    private RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .centerCrop();

    protected ContentAdapter(List<MultipleItemEntity> data, WallDelegate delegate) {
        super(data);
        this.DELEGATE = delegate;
        addItemType(ItemType.PRODUCT_CONTENT, R.layout.item_content_product);
    }

    public static ContentAdapter create(DataConverter dataConverter, WallDelegate delegate) {
        return new ContentAdapter(dataConverter.convert(), delegate);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()) {
            case ItemType.PRODUCT_CONTENT:
                final int productId = item.getField(MultipleFields.ID);
                final String name = item.getField(ContentDataField.NAME);
                final String mainImg = item.getField(ContentDataField.MAINIMAGE);
                helper.setText(R.id.tv_product_name, name);
                Glide.with(mContext)
                        .load(mainImg)
                        .apply(OPTIONS)
                        .into((ImageView) helper.getView(R.id.iv_product_img));
                //为商品Item设置单击事件
                View itemView = helper.itemView;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到详情页面
                        DELEGATE.getSupportDelegate().start(new ProductDetailDelegate(productId));
                    }
                });
                break;
            default:
                break;
        }
    }
}
