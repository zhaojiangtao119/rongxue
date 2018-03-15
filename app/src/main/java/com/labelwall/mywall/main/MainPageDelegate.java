package com.labelwall.mywall.main;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.main.cart.ShopCartDelegate;
import com.labelwall.mywall.main.compass.ActivityDelegate;
import com.labelwall.mywall.main.index.IndexDelegate;
import com.labelwall.mywall.main.sort.SortDelegate;
import com.labelwall.mywall.main.user.UserDelegate;
import com.labelwall.mywall.util.locate.AMapLocationHelper;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-03-09.
 */

public class MainPageDelegate extends BottomItemDelegate implements AMapLocationListener {

    @BindView(R2.id.rv_main_page)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.tv_main_type)
    AppCompatTextView mMenu = null;

    @OnClick(R2.id.tv_main_type)
    void onClickMainType() {
        mDialog.initDialog();
    }

    @OnClick(R2.id.tv_near_user)
    void onClickNearUser() {
        //获取周围用户
        mAMapLocationHelper.startLocationNow();
        getNearUser();
    }

    private void getNearUser() {
        //在进行一次定位
        RestClient.builder()
                .url("")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                    }
                })
                .build()
                .post();
    }

    private MainPageDialog mDialog = null;
    private MainPageAdapter mAdapter = null;
    private MainPageDataConver mDataconver = new MainPageDataConver();
    private final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    private AMapLocationHelper mAMapLocationHelper = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_main_page;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mDialog = new MainPageDialog(this);
        initRecyclerView();
        mAMapLocationHelper = new AMapLocationHelper(getContext(), this);
        mAMapLocationHelper.initLocation();
    }

    private void initRecyclerView() {
        final StaggeredGridLayoutManager manager =
                new StaggeredGridLayoutManager(8, GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new MainPageAdapter(mDataconver.convert());
        mRecyclerView.setAdapter(mAdapter);
        //设置颜色分割线
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
    }


    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            if (location.getErrorCode() == 0) {
                //获取定位城市
                Log.e("LOCATION_TAG", location.toString());
                //存储当前用户的位置
                //mAMapLocation = location;
                saveCurrentUserLocation(location);
            } else {

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
}
