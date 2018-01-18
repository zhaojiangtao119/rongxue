package com.labelwall.mywall.main.user.list;

import android.widget.CompoundButton;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.labelwall.mywall.delegates.base.WallDelegate;

/**
 * Created by Administrator on 2018-01-17.
 * 设置选项的itemBean
 */

public class ListBean implements MultiItemEntity {

    private int mItemType = 0;
    private String mImageUrl = null;
    private String mText = null;
    private String mValue = null;
    private int mId = 0;
    private WallDelegate mDelegate = null;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = null;

    public ListBean(int itemType, String imageUrl, String text, String value,
                    int id, WallDelegate delegate, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.mItemType = itemType;
        this.mImageUrl = imageUrl;
        this.mText = text;
        this.mValue = value;
        this.mId = id;
        this.mDelegate = delegate;
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getText() {
        if (mText == null) {
            return "";
        }
        return mText;
    }

    public String getValue() {
        if (mValue == null) {
            return "";
        }
        return mValue;
    }

    public int getId() {
        return mId;
    }

    public WallDelegate getDelegate() {
        return mDelegate;
    }

    public CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public static final class builder {
        private int id = 0;
        private int itemType = 0;
        private String imageUrl = null;
        private String text = null;
        private String value = null;
        private WallDelegate delegate = null;
        private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = null;

        public builder setId(int id) {
            this.id = id;
            return this;
        }

        public builder setItemType(int itemType) {
            this.itemType = itemType;
            return this;
        }

        public builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public builder setText(String text) {
            this.text = text;
            return this;
        }

        public builder setValue(String value) {
            this.value = value;
            return this;
        }

        public builder setDelegate(WallDelegate delegate) {
            this.delegate = delegate;
            return this;
        }

        public builder setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
            this.onCheckedChangeListener = onCheckedChangeListener;
            return this;
        }

        public ListBean build() {
            return new ListBean(itemType, imageUrl, text, value, id,
                    delegate, onCheckedChangeListener);
        }
    }

}
