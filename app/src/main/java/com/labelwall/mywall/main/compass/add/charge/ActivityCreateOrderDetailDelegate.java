package com.labelwall.mywall.main.compass.add.charge;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.WallBottomDelegate;
import com.labelwall.mywall.main.compass.ActivityJPushTag;
import com.labelwall.mywall.main.compass.add.ActivityCreateInfoItem;
import com.labelwall.mywall.main.compass.detail.ActivityDetailDelegate;
import com.labelwall.mywall.main.compass.detail.ActivityDetailInfoDelegate;
import com.labelwall.mywall.main.compass.my.ActivityMyDelegate;
import com.labelwall.mywall.main.user.account.add.ActivityAccountJindouAdapter;
import com.labelwall.mywall.push.JPushAliasTagSequence;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.qiniu.QnUploadHelper;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;
import com.qiniu.android.http.ResponseInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-02-24.
 */

public class ActivityCreateOrderDetailDelegate extends WallDelegate {

    @BindView(R2.id.tv_account_order_no)
    AppCompatTextView mOrderNoView = null;
    @BindView(R2.id.tv_account_order_info)
    AppCompatTextView mOrderInfo = null;

    @BindView(R2.id.tv_activity_order_desc)
    AppCompatTextView mOrderPriceDesc = null;
    @BindView(R2.id.tv_account_order_price)
    AppCompatTextView mOrderPriceView = null;

    @BindView(R2.id.tv_account_order_jindou_num)
    AppCompatTextView mJindouNum = null;
    @BindView(R2.id.tv_account_order_status)
    AppCompatTextView mOrderStatus = null;

    @BindView(R2.id.btn_submit_add_order)
    Button mPayOrder = null;

    @OnClick(R2.id.btn_submit_add_order)
    void onClickPayActivityOrder() {
        if (ACTIVITY_PARAMS != null) {//创建收费活动
            final Uri posterUri = (Uri) ACTIVITY_PARAMS.get(ActivityCreateInfoItem.ACTIVITY_POSTER);
            //支付并创建收费活动
            RestClient.builder()
                    .url("activity/account/trade/pay/" + mOrderNo)
                    .params(ACTIVITY_PARAMS)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            final JSONObject jsonResponse = JSON.parseObject(response);
                            final int status = jsonResponse.getInteger("status");
                            final String message = jsonResponse.getString("msg");
                            final Integer activityId = jsonResponse.getInteger("data");
                            if (status == 0) {
                                //1.将活动海报存储到服务器，
                                //创建成功，上传图片，将图片的url提交的后台修改该活动信息
                                if (posterUri != null) {
                                    final String key = "activity/poster/" + USER_ID + "/" + System.currentTimeMillis() + "/images";
                                    QnUploadHelper.uploadPic(posterUri.getPath(), key, new QnUploadHelper.UploadCallBack() {

                                        @Override
                                        public void success(String url) {
                                            //将图片的url存储到DB
                                            updateActivityInfo(activityId, url);
                                        }

                                        @Override
                                        public void fail(String key, ResponseInfo info) {

                                        }
                                    });
                                    //创建JPush Tag
                                    setAcitivtyTag(activityId);
                                }
                            } else {
                                Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .build()
                    .post();
        } else {
            //支付订单并加入收费活动 1.修改订单状态，2.修改账户余额，3.创建trade_histroy,4.activity_join新建记录，
            RestClient.builder()
                    .url("activity/account/trade/pay/join")
                    .params("id", mOrderId)
                    .params("userId", USER_ID)
                    .params("orderNo", mOrderNo)
                    .params("orderPrice", mOrderPrice)
                    .params("activityId", mActivityId)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            final JSONObject jsonResponse = JSON.parseObject(response);
                            final int status = jsonResponse.getInteger("status");
                            final String message = jsonResponse.getString("msg");
                            if (status == 0) {
                                //TODO 加入成功，跳转到活动详情页面吧
                                joinActivitySccess();
                            } else {
                                Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .build()
                    .post();
        }
    }

    private void joinActivitySccess() {
        RestClient.builder()
                .url("activity/" + mActivityId)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject activityDto =
                                JSON.parseObject(response).getJSONObject("data");
                        //初始化数据
                        getSupportDelegate()
                                .startWithPop(ActivityDetailDelegate.create(mActivityId));
                    }
                })
                .build()
                .get();
    }

    private void updateActivityInfo(Integer activityId, String url) {
        RestClient.builder()
                .url("activity/poster")
                .params("userId", USER_ID)
                .params("activityId", activityId)
                .params("posterUrl", url)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject jsonResponse = JSON.parseObject(response);
                        final int status = jsonResponse.getInteger("status");
                        final String message = jsonResponse.getString("msg");
                        if (status == 1) {
                            Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                        } else if (status == 0) {
                            //页面跳转，问题：在网络请求过程中跳转页面，该页面涉及到了网络请求，则无法发送该请求
                            getSupportDelegate().startWithPop(new ActivityMyDelegate());
                        }
                    }
                })
                .build()
                .put();
    }

    private final Map<String, Object> ACTIVITY_PARAMS;
    private final JSONObject ORDER_PARAMS;
    private String mOrderNo = null;
    private Integer mOrderPrice = null;
    private Integer mActivityId = null;
    private Integer mOrderId = null;
    private final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    public ActivityCreateOrderDetailDelegate(JSONObject orderParams, Map<String, Object> activityParams) {
        this.ORDER_PARAMS = orderParams;
        this.ACTIVITY_PARAMS = activityParams;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_account_add_order_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        bindData();
    }

    private void bindData() {
        mOrderId = ORDER_PARAMS.getInteger("id");
        mActivityId = ORDER_PARAMS.getInteger("activityId");
        mOrderNo = ORDER_PARAMS.getString("orderNo");
        final String typeDesc = ORDER_PARAMS.getString("typeDesc");
        mOrderPrice = ORDER_PARAMS.getInteger("orderPrice");
        final String orderInfo = ORDER_PARAMS.getString("orderInfo");
        final String orderStatus = ORDER_PARAMS.getString("statusDesc");
        mPayOrder.setText("支付并创建活动");
        mOrderNoView.setText(mOrderNo);
        mOrderInfo.setText(typeDesc);
        mOrderPriceDesc.setText("订单描述");
        mOrderPriceView.setText(orderInfo);
        mJindouNum.setText(String.valueOf(mOrderPrice));
        mOrderStatus.setText(orderStatus);

        if (ACTIVITY_PARAMS == null) {
            mPayOrder.setText("支付并参加活动");
        } else {
            mPayOrder.setText("支付并创建活动");
        }
    }

    public void setAcitivtyTag(Integer acitivtyTag) {
        //获取当前用户的信息
        List<UserProfile> userProfileList = DataBaseManager
                .getInstance()
                .getDao()
                .queryRaw("where _id=?", new String[]{String.valueOf(USER_ID)});
        if (userProfileList != null && userProfileList.size() > 0) {
            String username = userProfileList.get(0).getUsername();
            String tag = acitivtyTag + username;
            ActivityJPushTag.getInstance()
                    .addJPushTag(getContext(), JPushAliasTagSequence.ACTION_TAG_ADD, tag);
        }
    }
}
