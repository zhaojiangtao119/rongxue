package com.labelwall.mywall.main.compass.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;

import butterknife.BindFloat;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018-02-07.
 */

public class ActivityDetailInfoDelegate extends WallDelegate {

    //startUserProfile，activtyInfo
    @BindView(R2.id.circle_user_avatar)
    CircleImageView mStartUserAvatar = null;
    @BindView(R2.id.tv_start_username)
    AppCompatTextView mStartUserUsername = null;

    @BindView(R2.id.tv_activity_detail_theme)
    AppCompatTextView mActivityTheme = null;
    @BindView(R2.id.tv_detail_type)
    AppCompatTextView mActivityType = null;
    @BindView(R2.id.tv_detail_style)
    AppCompatTextView mActivityStyle = null;
    @BindView(R2.id.tv_detail_amount)
    AppCompatTextView mActivityAmount = null;
    @BindView(R2.id.tv_activity_apply_start_time)
    AppCompatTextView mActivityApplyStartTime = null;
    @BindView(R2.id.tv_activity_apply_end_time)
    AppCompatTextView mActivityApplyEndTime = null;
    @BindView(R2.id.tv_activity_start_time)
    AppCompatTextView mActivityStartTime = null;
    @BindView(R2.id.tv_activity_end_time)
    AppCompatTextView mActivityEndTime = null;
    @BindView(R2.id.tv_activity_content)
    AppCompatTextView mActivityContent = null;
    @BindView(R2.id.tv_activity_location)
    AppCompatTextView mActivityLocation = null;


    private static final String ARG_ACTIVITY_DATA = "ARG_ACTIVITY_DATA";
    private JSONObject mActivityDto = null;
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .centerCrop();

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_info;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle agrs = getArguments();
        if (agrs != null) {
            final String activityInfo = agrs.getString(ARG_ACTIVITY_DATA);
            mActivityDto = JSON.parseObject(activityInfo);
        }
    }

    public static ActivityDetailInfoDelegate create(String data) {
        final Bundle args = new Bundle();
        args.putString(ARG_ACTIVITY_DATA, data);
        final ActivityDetailInfoDelegate activityDetailInfoDelegate = new ActivityDetailInfoDelegate();
        activityDetailInfoDelegate.setArguments(args);
        return activityDetailInfoDelegate;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initActivityData();
    }

    private void initActivityData() {
        //初始化活动信息
        final String theme = mActivityDto.getString("theme");
        final String type = mActivityDto.getString("type");
        final String style = mActivityDto.getString("style");
        final String applyStartTime = mActivityDto.getString("starttime");
        final String applyEndTime = mActivityDto.getString("endtime");
        final String startTime = mActivityDto.getString("detailStartTime");
        final String endTime = mActivityDto.getString("detailEndTime");
        final Integer amount = mActivityDto.getInteger("amount");
        final String content = mActivityDto.getString("content");
        final String location = mActivityDto.getString("location");
        final String city = mActivityDto.getString("city");
        final String county = mActivityDto.getString("county");
        final String school = mActivityDto.getString("school");
        mActivityTheme.setText(theme);
        mActivityType.setText(type);
        mActivityStyle.setText(style);
        mActivityApplyStartTime.setText(applyStartTime);
        mActivityApplyEndTime.setText(applyEndTime);
        mActivityStartTime.setText(startTime);
        mActivityEndTime.setText(endTime);
        mActivityAmount.setText("金豆" + String.valueOf(amount));
        mActivityContent.setText(content);
        if (!StringUtils.isEmpty(school)) {
            mActivityLocation.setText(school);
        } else if (StringUtils.isEmpty(school) && !StringUtils.isEmpty(location)) {
            mActivityLocation.setText(location);
        }

        //初始化发起者信息
        final JSONObject startUser = mActivityDto.getJSONObject("startUser");
        final String startUserAvatar = startUser.getString("head");
        final String startUserUsername = startUser.getString("username");
        Glide.with(_mActivity)
                .load(startUserAvatar)
                .apply(OPTIONS)
                .into(mStartUserAvatar);
        mStartUserUsername.setText(startUserUsername);
    }
}
