package com.labelwall.mywall.main.compass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.main.compass.add.ActivityCreateDelegate;
import com.labelwall.mywall.main.compass.my.ActivityMyDelegate;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-01-04.
 */

public class ActivityDelegate extends BottomItemDelegate {

    @BindView(R2.id.srl_activity_list)
    SwipeRefreshLayout mRefreshLayout = null;
    @BindView(R2.id.rv_activity_list)
    RecyclerView mRecyclerView = null;

    @BindView(R2.id.tv_activity_location)
    AppCompatTextView mLocationText;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationClientOption = null;

    private ActivityRefreshHandler mRefreshHandler = null;

    @OnClick(R2.id.tv_activity_my)
    void onClickMyActivity() {//我的活动
        getParentDelegate().getSupportDelegate().start(new ActivityMyDelegate());
    }

    @OnClick(R2.id.icon_activity_add)
    void onClickStartActivity() {//创建活动
        getParentDelegate().getSupportDelegate().start(new ActivityCreateDelegate());
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
        initLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        locationClient.startLocation();
    }

    private void initLocation() {
        //动态获取权限
        startLocation();
        locationClient = new AMapLocationClient(this.getContext());
        locationClientOption = getDefaultOption();
        locationClient.setLocationOption(locationClientOption);
        locationClient.setLocationListener(locationListener);
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (location != null) {
                if (location.getErrorCode() == 0) {
                    //获取定位城市
                    mLocationText.setText(location.getCity());
                } else {
                    //
                    mLocationText.setText("未知位置");
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationClient != null) {
            locationClient.onDestroy();
            locationClient = null;
            locationClientOption = null;
        }
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        option.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        option.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        option.setInterval(2000);
        option.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        option.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        option.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        option.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        option.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        option.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return option;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        WallDelegate wallDelegate = getParentDelegate();
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
