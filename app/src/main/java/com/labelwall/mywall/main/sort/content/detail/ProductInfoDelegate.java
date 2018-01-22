package com.labelwall.mywall.main.sort.content.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;

import java.math.BigDecimal;

import butterknife.BindView;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by Administrator on 2018-01-22.
 */

public class ProductInfoDelegate extends WallDelegate {

    @BindView(R2.id.tv_product_info_title)
    AppCompatTextView mProductTitle = null;
    @BindView(R2.id.tv_product_info_desc)
    AppCompatTextView mProductDesc = null;
    @BindView(R2.id.tv_product_info_price)
    AppCompatTextView mProductPrice = null;

    private static final String GOODS_NAME = "NAME";
    private static final String GOODS_SUB_TITLE = "SUB_TITLE";
    private static final String GOODS_PRICE = "PRICE";
    private String mName = null;
    private String mSubTitle = null;
    private double mPrice = 0.0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(GOODS_NAME);
            mSubTitle = args.getString(GOODS_SUB_TITLE);
            mPrice = args.getDouble(GOODS_PRICE);
        }
    }

    public static ProductInfoDelegate create(String name, String subtitle, double price) {
        final Bundle args = new Bundle();
        args.putString(GOODS_NAME, name);
        args.putString(GOODS_SUB_TITLE, subtitle);
        args.putDouble(GOODS_PRICE, price);
        final ProductInfoDelegate delegate = new ProductInfoDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_product_info;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initData();
    }

    private void initData() {
        mProductTitle.setText(mName);
        mProductDesc.setText(mSubTitle);
        mProductPrice.setText(String.valueOf(mPrice));
    }
}
