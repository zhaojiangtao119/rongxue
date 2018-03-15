package com.labelwall.mywall.util.locate;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by Administrator on 2018-03-15.
 */

public class AMapLocationHelper {

    private final Context CONTEXT;
    private final AMapLocationListener LISTENER;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationClientOption = null;

    public AMapLocationHelper(Context context, AMapLocationListener listener) {
        this.CONTEXT = context;
        this.LISTENER = listener;
    }

    //初始化位置
    public void initLocation() {
        mLocationClient = new AMapLocationClient(CONTEXT);
        mLocationClientOption = getDefaultOption();
        mLocationClient.setLocationOption(mLocationClientOption);
        mLocationClient.setLocationListener(LISTENER);
    }

    //设置默认的定位参数
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        //option.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        //option.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        option.setInterval(2000);
        option.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        option.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        option.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        option.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        option.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        option.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return option;
    }

    //开始定位
    public void startLocationNow() {
        mLocationClient.startLocation();
    }

    //停止定位
    public void stopLocationNow() {
        mLocationClient.stopLocation();
    }

    //销毁定位
    public void destroyLocationNow() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationClientOption = null;
        }
    }
}
