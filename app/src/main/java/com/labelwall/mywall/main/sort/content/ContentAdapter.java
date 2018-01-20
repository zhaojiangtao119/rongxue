package com.labelwall.mywall.main.sort.content;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.labelwall.mywall.R;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2018-01-20.
 */

public class ContentAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, ContentAdapter.ContentViewHolder> {

    private RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .centerCrop();

    protected ContentAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(ItemType.PRODUCT_CONTENT, R.layout.item_content_product);
    }

    public static ContentAdapter create(DataConverter dataConverter) {
        return new ContentAdapter(dataConverter.convert());
    }

    @Override
    protected void convert(ContentViewHolder helper, MultipleItemEntity item) {
        switch (helper.getItemViewType()) {
            case ItemType.PRODUCT_CONTENT:
                final String name = item.getField(ContentDataField.NAME);
                final String mainImg = item.getField(ContentDataField.MAINIMAGE);
                helper.setText(R.id.tv_product_name, name);
                Glide.with(mContext)
                        .load(mainImg)
                        .apply(OPTIONS)
                        .into((ImageView) helper.getView(R.id.iv_product_img));

                break;
            default:
                break;
        }
    }

    public class ContentViewHolder extends BaseViewHolder {

        public ContentViewHolder(View view) {
            super(view);
        }
    }

}
