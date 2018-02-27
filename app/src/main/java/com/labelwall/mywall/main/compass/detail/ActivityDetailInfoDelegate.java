package com.labelwall.mywall.main.compass.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.compass.add.charge.ActivityCreateOrderDetailDelegate;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import butterknife.BindFloat;
import butterknife.BindView;
import butterknife.OnClick;
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
    @BindView(R2.id.tv_activity_apply_join)
    AppCompatTextView mJoinActivity = null;


    @OnClick(R2.id.tv_activity_apply_join)
    void onJoinActivity() {
        String hintMessage = mJoinActivity.getText().toString();
        if (hintMessage.equals("放弃活动")) {
            //放弃申请参加的活动
            RestClient.builder()
                    .url("activity/user/" + USER_ID + "/" + mActivityId)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            final JSONObject jsonResponse = JSON.parseObject(response);
                            final Integer status = jsonResponse.getInteger("status");
                            final String message = jsonResponse.getString("msg");
                            if (status == 0) {
                                mJoinActivity.setText("加入活动");
                            } else {
                                Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .build()
                    .delete();
        } else if (hintMessage.equals("加入活动")) {
            //判断加入的活动的收费情况
            if (mAmount == 0) {//免费活动
                RestClient.builder()
                        .url("activity/join/" + mActivityId + "/" + USER_ID)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                final JSONObject jsonResponse = JSON.parseObject(response);
                                final Integer status = jsonResponse.getInteger("status");
                                final String message = jsonResponse.getString("msg");
                                if (status == 0) {
                                    mJoinActivity.setText("放弃活动");
                                } else if (status == 2) {
                                    Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .build()
                        .post();
            } else if (mAmount > 0) {//收费活动
                //1.判断用户的时间是否与加入的时间存在冲突，人数是否冲突
                RestClient.builder()
                        .url("activity/validate/join")
                        .params("activityId", mActivityId)
                        .params("userId", USER_ID)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                final JSONObject jsonResponse = JSON.parseObject(response);
                                final int status = jsonResponse.getInteger("status");
                                final String message = jsonResponse.getString("msg");
                                if (status == 0) {
                                    createJoinActivityOrder();
                                } else {
                                    Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .build()
                        .post();

            } else {
                Toast.makeText(_mActivity, "未知错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createJoinActivityOrder() {
        //2.首先创建加入活动的订单
        RestClient.builder()
                .url("activity/account/trade/add/a")
                .params("userId", USER_ID)
                .params("orderPrice", mAmount)
                .params("orderInfo", mTheme)
                .params("activityId", mActivityId)
                .params("type", 1)//1加入活动
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject jsonResponse = JSON.parseObject(response);
                        final int status = jsonResponse.getInteger("status");
                        final String message = jsonResponse.getString("msg");
                        final JSONObject data = jsonResponse.getJSONObject("data");
                        //跳转到订单详情页面
                        if (status == 0) {
                            //跳转到订单详情页面
                            getParentDelegate().getSupportDelegate().startWithPop(new ActivityCreateOrderDetailDelegate(data, null));
                        } else {
                            Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build()
                .post();
    }

    private static final String ARG_ACTIVITY_DATA = "ARG_ACTIVITY_DATA";
    private JSONObject mActivityDto = null;
    private static final String ARG_ACTIVITY_ID = "ARG_ACTIVITY_ID";
    private Integer mActivityId = null;
    private final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .dontAnimate()
            .centerCrop();
    private int mAmount = -1;
    private String mTheme = null;

    public ActivityDetailInfoDelegate(JSONObject activityDto, Integer activityId) {
        this.mActivityDto = activityDto;
        this.mActivityId = activityId;
    }

    ActivityDetailInfoDelegate() {
        
    }


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
            mActivityId = agrs.getInt(ARG_ACTIVITY_ID);
        }
    }

    public static ActivityDetailInfoDelegate create(String data, Integer activityId) {
        final Bundle args = new Bundle();
        args.putString(ARG_ACTIVITY_DATA, data);
        args.putInt(ARG_ACTIVITY_ID, activityId);
        final ActivityDetailInfoDelegate activityDetailInfoDelegate = new ActivityDetailInfoDelegate();
        activityDetailInfoDelegate.setArguments(args);
        return activityDetailInfoDelegate;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mJoinActivity.setText("加入活动");
        initActivityData();
    }

    private void initActivityData() {
        //初始化活动信息
        mTheme = mActivityDto.getString("theme");
        final String type = mActivityDto.getString("type");
        final String style = mActivityDto.getString("style");
        final String applyStartTime = mActivityDto.getString("starttime");
        final String applyEndTime = mActivityDto.getString("endtime");
        final String startTime = mActivityDto.getString("detailStartTime");
        final String endTime = mActivityDto.getString("detailEndTime");
        mAmount = mActivityDto.getInteger("amount");
        final String content = mActivityDto.getString("content");
        final String location = mActivityDto.getString("location");
        final String city = mActivityDto.getString("city");
        final String county = mActivityDto.getString("county");
        final String school = mActivityDto.getString("school");
        mActivityTheme.setText(mTheme);
        mActivityType.setText(type);
        mActivityStyle.setText(style);
        mActivityApplyStartTime.setText(applyStartTime);
        mActivityApplyEndTime.setText(applyEndTime);
        mActivityStartTime.setText(startTime);
        mActivityEndTime.setText(endTime);
        mActivityAmount.setText("金豆" + String.valueOf(mAmount));
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
