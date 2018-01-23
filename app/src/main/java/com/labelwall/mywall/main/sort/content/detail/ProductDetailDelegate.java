package com.labelwall.mywall.main.sort.content.detail;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.YoYo;
import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.ui.animation.BezierAnimation;
import com.labelwall.mywall.ui.animation.BezierUtil;
import com.labelwall.mywall.ui.banner.HolderCreator;
import com.labelwall.mywall.ui.widget.CircleTextView;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Administrator on 2018-01-22.
 */

public class ProductDetailDelegate extends WallDelegate implements
        AppBarLayout.OnOffsetChangedListener,
        BezierUtil.AnimationListener {

    private final int PRODUCT_ID;

    @BindView(R2.id.goods_detail_toolbar)
    Toolbar mToolbar = null;
    @BindView(R2.id.tab_layout)
    TabLayout mTabLayout = null;
    @BindView(R2.id.view_pager)
    ViewPager mViewPager = null;
    @BindView(R2.id.detail_banner)
    ConvenientBanner<String> mBanner = null;
    @BindView(R2.id.collapsing_toolbar_detail)
    CollapsingToolbarLayout mCollapsingToolbarLayout = null;
    @BindView(R2.id.app_bar_detail)
    AppBarLayout mAppBar = null;

    //底部
    @BindView(R2.id.icon_favor)
    IconTextView mIconFavor = null;
    @BindView(R2.id.tv_shopping_cart_amount)
    CircleTextView mCircleTextView = null;
    @BindView(R2.id.rl_add_shop_cart)
    RelativeLayout mRlAddShopCart = null;
    @BindView(R2.id.icon_shop_cart)
    IconTextView mIconShopCart = null;

    private String mProductThumbUrl = null;
    private int mShopCount = 0;

    private final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate()
            .override(100, 100);

    @OnClick(R2.id.rl_add_shop_cart)
    void onClickAddShopCart() {
        //商品加入购物车
        final CircleImageView animImg = new CircleImageView(getContext());
        Glide.with(this)
                .load(mProductThumbUrl)
                .apply(OPTIONS)
                .into(animImg);
        BezierAnimation.addCart(this, mRlAddShopCart, mIconShopCart, animImg, this);
    }

    @Override
    public void onAnimationEnd() {
        //商品加入购物车动画结束后，
        YoYo.with(new ScaleUpAnimator())
                .duration(500)
                .playOn(mIconShopCart);
        mShopCount++;
        //TODO 请求服务器将添加到购物的商品信息写入DB,参数userId,count,productId
        final long userId = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
        RestClient.builder()
                .url("shopcart/app_add_cart")
                .params("userId", userId)
                .params("quantity", 1)
                .params("productId", PRODUCT_ID)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject data = JSON.parseObject(response);
                        final int status = data.getInteger("status");
                        if (status == 0) {
                            mCircleTextView.setVisibility(View.VISIBLE);
                            mCircleTextView.setText(String.valueOf(mShopCount));
                        }
                    }
                })
                .build()
                .post();
    }


    public ProductDetailDelegate(int productId) {
        this.PRODUCT_ID = productId;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_product_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mCollapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(_mActivity, R.color.app_background));
        mAppBar.addOnOffsetChangedListener(this);
        mCircleTextView.setCircleBackground(Color.RED);
        initData();
        initTabLayout();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    private void initData() {
        RestClient.builder()
                .url("product/get_product_detail/" + PRODUCT_ID)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject data = JSON.parseObject(response).getJSONObject("data");
                        initBanner(data);
                        initProductInfo(data);
                    }
                })
                .build()
                .get();
    }


    private void initBanner(JSONObject data) {
        if (mShopCount == 0) {
            mCircleTextView.setVisibility(View.GONE);
        }
        mProductThumbUrl = data.getString("mainImage");
        final List<String> images = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            images.add(mProductThumbUrl.toString());
        }
        mBanner
                .setPages(new HolderCreator(), images)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);
    }

    private void initProductInfo(JSONObject data) {
        final String name = data.getString("name");
        final String subtitle = data.getString("subtitle");
        final double price = data.getDouble("price");
        getSupportDelegate().
                loadRootFragment(R.id.frame_goods_info,
                        ProductInfoDelegate.create(name, subtitle, price));
    }

    private void initTabLayout() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(_mActivity, R.color.app_title));
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setupWithViewPager(mViewPager);//关联viewPager
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

}
