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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.compass.ActivityJPushTag;
import com.labelwall.mywall.main.compass.add.charge.ActivityCreatePayDelegate;
import com.labelwall.mywall.main.compass.my.ActivityMyDelegate;
import com.labelwall.mywall.main.user.UserClickListener;
import com.labelwall.mywall.main.user.UserSettingItem;
import com.labelwall.mywall.main.user.address.AdressDelegate;
import com.labelwall.mywall.main.user.list.ListAdapter;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.main.user.list.ListItemType;
import com.labelwall.mywall.main.user.profile.UserProfileDelegate;
import com.labelwall.mywall.main.user.profile.location.JsonBean;
import com.labelwall.mywall.main.user.settings.SettingsDelegate;
import com.labelwall.mywall.push.JPushAliasTagSequence;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;
import com.labelwall.mywall.util.location.LocationUtil;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.RestCreator;
import com.labelwall.mywall.util.net.callback.IRequest;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.net.rx.RxRestClient;
import com.labelwall.mywall.util.net.rx.RxRestService;
import com.labelwall.mywall.util.qiniu.QnUploadHelper;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;
import com.qiniu.android.http.ResponseInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    AppCompatEditText mActivityContent = null;//活动概述
    @BindView(R2.id.et_activity_title)
    AppCompatEditText mActivityTitle = null;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .centerCrop()
            .dontAnimate();

    private ArrayList<JsonBean> options1Item = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Item = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Item = new ArrayList<>();

    private Map<String, Object> mCreateActivityParams = new HashMap<>();
    private Uri mPosterUri = null;
    private final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private boolean mValidateTime = false;

    @OnClick(R2.id.tv_activity_upload_poster)
    void onClickUploadPoster() {
        startCameraWithCheck();
        //图片剪裁的回调
        CallbackManager manager = CallbackManager
                .getInstance()
                .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void executeCallback(Uri args) {
                        mPosterUri = args;
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
        mCreateActivityParams.putAll(ActivityCreateClickListener.getActivityInfo());
        //验证信息
        boolean flag = validataParams(mCreateActivityParams);
        if (flag) {
            createActivity();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private boolean validataParams(Map<String, Object> activityInfo) {
        boolean flag = true;
        if (mPosterUri == null) {
            Toast.makeText(_mActivity, "请上传图片", Toast.LENGTH_SHORT).show();
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.APPLY_START_TIME_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.APPLY_START_TIME_VALUE);
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.APPLY_END_TIME_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.APPLY_END_TIME_VALUE);
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.ACTIVITY_START_TIME_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.ACTIVITY_START_TIME_VALUE);
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.ACTIVITY_END_TIME_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.ACTIVITY_END_TIME_VALUE);
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.ACTIVITY_FREE_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.ACTIVITY_FREE_VALUE);
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.ACTIVITY_STYLE_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.ACTIVITY_STYLE_VALUE);
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.ACTIVITY_TYPE_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.ACTIVITY_TYPE_VALUE);
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.ACTIVITY_SCHOOL_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.ACTIVITY_SCHOOL_VALUE);
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.ACTIVITY_PROVINCE_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.ACTIVITY_LOCATION_VALUE);
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.ACTIVITY_CITY_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.ACTIVITY_LOCATION_VALUE);
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.ACTIVITY_COUNTY_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.ACTIVITY_LOCATION_VALUE);
            return !flag;
        }
        if (activityInfo.get(ActivityCreateInfoItem.ACTIVITY_USER_NUM_PARAM) == null) {
            hintMessage(ActivityCreateInfoItem.ACTIVITY_USER_NUM_VALUE);
            return !flag;
        }
        if (StringUtils.isEmpty(mActivityContent.getText().toString())) {
            hintMessage("活动概括");
            return !flag;
        } else {
            mCreateActivityParams.put(ActivityCreateInfoItem.ACTIVITY_CONTENT_PRAMS,
                    mActivityContent.getText().toString());
        }
        if (StringUtils.isEmpty(mActivityTitle.getText().toString())) {
            hintMessage("活动标题");
            return !flag;
        } else {
            mCreateActivityParams.put(ActivityCreateInfoItem.ACTIVITY_TITLE_PRAMS,
                    mActivityTitle.getText().toString());
        }
        mCreateActivityParams.put(ActivityCreateInfoItem.ACTIVITY_USER_ID_PRAMS, USER_ID);
        mCreateActivityParams.put(ActivityCreateInfoItem.ACTIVITY_POSTER, mPosterUri);
        return flag;
    }

    private void hintMessage(String message) {
        Toast.makeText(getContext(), message + "未设置", Toast.LENGTH_SHORT).show();
    }

    private void createActivity() {
        //1.首先判断活动的收费情况
        int free = (int) mCreateActivityParams.get(ActivityCreateInfoItem.ACTIVITY_FREE_PARAM);
        if (free == 0) {
            //免费
            RestClient.builder()
                    .url("activity/create")
                    .params(mCreateActivityParams)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            final JSONObject jsonResponse = JSON.parseObject(response);
                            final int status = jsonResponse.getInteger("status");
                            final String message = jsonResponse.getString("msg");
                            final Integer activityId = jsonResponse.getInteger("data");
                            if (status == 0) {
                                //创建成功，上传图片，将图片的url提交的后台修改该活动信息
                                if (mPosterUri != null) {
                                    final String key = "activity/poster/" + USER_ID + "/" + System.currentTimeMillis() + "/images";
                                    QnUploadHelper.uploadPic(mPosterUri.getPath(), key, new QnUploadHelper.UploadCallBack() {

                                        @Override
                                        public void success(String url) {
                                            //将图片的url存储到DB
                                            updateActivityInfo(activityId, url);
                                        }

                                        @Override
                                        public void fail(String key, ResponseInfo info) {

                                        }
                                    });
                                }
                                //创建成功后设置该活动的JPush Tag
                                setAcitivtyTag(activityId);
                            } else {
                                Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .build()
                    .post();
        } else if (free == 1) {
            //TODO 操作之前需要验证用户的填写的时间是否合适，验证当前用户的关联事件是否存在时间上的冲突
            //使用RxJava+Retrofit请求，解决Retrofit链式请求的回调问题
            final Observable<String> observable = RestCreator
                    .getRxRestService()
                    .post("activity/validate", mCreateActivityParams);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull String s) {
                            //获取的数据
                            final JSONObject jsonResponse = JSON.parseObject(s);
                            final int status = jsonResponse.getInteger("status");
                            final String message = jsonResponse.getString("msg");
                            if (status == 0) {
                                mValidateTime = true;
                            } else {
                                Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            forwordPayPage();
                        }
                    });
        }
    }

    private void forwordPayPage() {
        if (mValidateTime) {
            getSupportDelegate().startWithPop(new ActivityCreatePayDelegate(mCreateActivityParams));
        }
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
                            getSupportDelegate().startWithPop(new ActivityMyDelegate());
                        }
                    }
                })
                .build()
                .put();
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
        final ListBean activityUserNum = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(ActivityCreateInfoItem.ACTIVITY_USER_NUM)
                .setText(ActivityCreateInfoItem.ACTIVITY_USER_NUM_VALUE)
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
        data.add(activityUserNum);

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRecyclerView.setAdapter(adapter);
        //设置监听事件
        mRecyclerView.addOnItemTouchListener(
                new ActivityCreateClickListener(this, options1Item, options2Item, options3Item));
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
