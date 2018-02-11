package com.labelwall.mywall.main.compass.add;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.UserClickListener;
import com.labelwall.mywall.main.user.UserSettingItem;
import com.labelwall.mywall.main.user.address.AdressDelegate;
import com.labelwall.mywall.main.user.list.ListAdapter;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.main.user.list.ListItemType;
import com.labelwall.mywall.main.user.profile.UserProfileDelegate;
import com.labelwall.mywall.main.user.profile.location.JsonBean;
import com.labelwall.mywall.main.user.settings.SettingsDelegate;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;
import com.labelwall.mywall.util.location.LocationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-02-10.
 * 创建活动
 */

public class ActivityCreateDelegate extends WallDelegate {

    @BindView(R2.id.rv_create_activity_info)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.tv_activity_upload_poster)
    AppCompatTextView mUploadActivityPoster = null;
    @BindView(R2.id.iv_activity_poster_img)
    AppCompatImageView mAcitivtyPoster = null;
    @BindView(R2.id.et_activity_content)
    AppCompatEditText mActivityContent = null;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .centerCrop()
            .dontAnimate();

    private ArrayList<JsonBean> options1Item = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Item = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Item = new ArrayList<>();

    @OnClick(R2.id.tv_activity_upload_poster)
    void onClickUploadPoster() {
        startCameraWithCheck();
        //图片剪裁的回调
        CallbackManager manager = CallbackManager
                .getInstance()
                .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void executeCallback(Uri args) {
                        mUploadActivityPoster.setVisibility(View.INVISIBLE);
                        Glide.with(_mActivity)
                                .load(args)
                                .apply(OPTIONS)
                                .into(mAcitivtyPoster);
                    }
                });
    }

    @OnClick(R2.id.btn_activity_submit)
    void onClickActivitySubmit() {
        Map<Integer, Object> activityInfo = ActivityCreateClickListener.getActivityInfo();
        Log.e("INFO:", activityInfo.toString());
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_create_activity;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initLocationData();
    }

    private void initLocationData() {
        Map<String, Object> locationData = LocationUtil.create(this)
                .initJsonData()
                .getLocationData();
        options1Item.addAll((ArrayList<JsonBean>) locationData.get(LocationUtil.ITEM1));
        options2Item.addAll((ArrayList<ArrayList<String>>) locationData.get(LocationUtil.ITEM2));
        options3Item.addAll((ArrayList<ArrayList<ArrayList<String>>>) locationData.get(LocationUtil.ITEM3));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initSetList();//设置条目信息
    }

    private void initSetList() {
        final ListBean applyStartTime = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.APPLY_START_TIME)
                .setText(ActivityCreateInfoItem.APPLY_START_TIME_VALUE)
                .build();
        final ListBean applyEntTime = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.APPLY_END_TIME)
                .setText(ActivityCreateInfoItem.APPLY_END_TIME_VALUE)
                .build();
        final ListBean activityStartTime = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.ACTIVITY_START_TIME)
                .setText(ActivityCreateInfoItem.ACTIVITY_START_TIME_VALUE)
                .build();
        final ListBean activityEndTime = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.ACTIVITY_END_TIME)
                .setText(ActivityCreateInfoItem.ACTIVITY_END_TIME_VALUE)
                .build();
        final ListBean activityFree = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.ACTIVITY_FREE)
                .setText(ActivityCreateInfoItem.ACTIVITY_FREE_VALUE)
                .build();
        final ListBean activityStyle = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.ACTIVITY_STYLE)
                .setText(ActivityCreateInfoItem.ACTIVITY_STYLE_VALUE)
                .build();
        final ListBean activityType = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.ACTIVITY_TYPE)
                .setText(ActivityCreateInfoItem.ACTIVITY_TYPE_VALUE)
                .build();
        final ListBean activityLocation = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.ACTIVITY_LOCATION)
                .setText(ActivityCreateInfoItem.ACTIVITY_LOCATION_VALUE)
                .build();
        final ListBean activitySchool = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.ACTIVITY_SCHOOL)
                .setText(ActivityCreateInfoItem.ACTIVITY_SCHOOL_VALUE)
                .build();
        final ListBean activityAmount = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.ACTIVITY_AMOUNT)
                .setText(ActivityCreateInfoItem.ACTIVITY_AMOUNT_VALUE)
                .build();
        final List<ListBean> data = new ArrayList<>();
        data.add(applyStartTime);
        data.add(applyEntTime);
        data.add(activityStartTime);
        data.add(activityEndTime);
        data.add(activityFree);
        data.add(activityStyle);
        data.add(activityType);
        data.add(activityLocation);
        data.add(activitySchool);
        data.add(activityAmount);


        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRecyclerView.setAdapter(adapter);
        //设置监听事件
        mRecyclerView.addOnItemTouchListener(
                new ActivityCreateClickListener(this, options1Item, options2Item, options3Item));
    }
}
