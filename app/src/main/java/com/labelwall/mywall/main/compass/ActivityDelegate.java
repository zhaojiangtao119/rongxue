package com.labelwall.mywall.main.compass;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.main.compass.add.ActivityCreateDelegate;
import com.labelwall.mywall.main.compass.detail.ActivityDetailDelegate;
import com.labelwall.mywall.main.compass.my.ActivityMyDelegate;
import com.labelwall.mywall.util.locate.AMapLocationHelper;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.RestCreator;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018-01-04.
 */

public class ActivityDelegate extends BottomItemDelegate implements AMapLocationListener {

    @BindView(R2.id.srl_activity_list)
    SwipeRefreshLayout mRefreshLayout = null;
    @BindView(R2.id.rv_activity_list)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.tv_activity_location)
    AppCompatTextView mLocationText;

    private AMapLocationHelper mAMapLocationHelper = null;

    private ActivityRefreshHandler mRefreshHandler = null;
    private final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    private final Handler HANDLER = MyWall.getHandler();
    private AMapLocation mAMapLocation = null;

    @OnClick(R2.id.tv_activity_my)
    void onClickMyActivity() {//我的活动
        getSupportDelegate().start(new ActivityMyDelegate());
    }

    @OnClick(R2.id.icon_activity_add)
    void onClickStartActivity() {//创建活动
        getSupportDelegate().start(new ActivityCreateDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initRefreshLayout();
        initRecyclerView();
        //初始化定位
        mAMapLocationHelper = new AMapLocationHelper(getContext(), this);
        mAMapLocationHelper.initLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAMapLocationHelper.startLocationNow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAMapLocationHelper != null) {
            mAMapLocationHelper.destroyLocationNow();
        }
    }

    @Override
    public void onLocationChanged(final AMapLocation location) {
        if (location != null) {
            //定位成功
            if (location.getErrorCode() == 0) {
                //获取定位城市
                mLocationText.setText(location.getCity());
                Log.e("LOCATION_TAG", location.toString());
                //存储当前用户的位置
                //mAMapLocation = location;
                saveCurrentUserLocation(location);
            } else {
                mLocationText.setText("未知位置");
            }
        }
    }

    private void saveCurrentUserLocation(AMapLocation location) {
        //请求参数：经度，纬度，userId
        final double longitude = location.getLongitude();
        final double latitude = location.getLatitude();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(longitude).append(",").append(latitude);
        List<UserProfile> userProfileList = DataBaseManager
                .getInstance()
                .getDao()
                .queryRaw("where _id=?", new String[]{String.valueOf(USER_ID)});
        UserProfile userProfile = userProfileList.get(0);
        String jsonStr = JSON.toJSONString(userProfile);
        Log.e("GAODE", jsonStr + "," + stringBuilder.toString());
        RestClient.builder()
                .url("nearby/insert")
                .params("userProfile", jsonStr)
                .params("address", stringBuilder.toString())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //TODO
                    }
                })
                .build()
                .post();

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        WallDelegate wallDelegate = this;
        mRefreshHandler = ActivityRefreshHandler
                .create(mRefreshLayout, mRecyclerView, new ActivityDataConverter(), wallDelegate);
        mRefreshHandler.firshActivityPage();
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright
        );
        mRefreshLayout.setProgressViewOffset(true, 80, 100);
    }
}
