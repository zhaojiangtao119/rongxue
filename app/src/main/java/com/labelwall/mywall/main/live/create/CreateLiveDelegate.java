package com.labelwall.mywall.main.live.create;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.main.live.util.TencentLiveUserAccount;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;
import com.labelwall.mywall.util.image.ImageUtil;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.livesdk.ILVLiveManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-03-16.
 */

public class CreateLiveDelegate extends BottomItemDelegate {


    @BindView(R2.id.tb_create_live)
    Toolbar mToolbar = null;
    @BindView(R2.id.iv_live_cover)
    AppCompatImageView mLiveCover = null;
    @BindView(R2.id.tv_pic_tip)
    AppCompatTextView mLiveCoverHint = null;
    @BindView(R2.id.et_live_title)
    AppCompatEditText mLiveTilte = null;

    private final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private UserProfile mUserProfile = null;
    private boolean mIsTencentLogin = false;

    @OnClick(R2.id.tv_pic_tip)
    void onClickLiveAvatar() {
        startCameraWithCheck();
        final CallbackManager manager = CallbackManager.getInstance()
                .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void executeCallback(Uri args) {
                        ImageUtil.loadImage(_mActivity, args, mLiveCover);
                        mLiveCoverHint.setVisibility(View.GONE);
                        //TODO 将图片存储在七牛云

                    }
                });
    }

    @OnClick(R2.id.tv_create_live)
    void onClickCreateLive() {
        if (StringUtils.isEmpty(mLiveTilte.getText())) {
            Toast.makeText(_mActivity, "请输入直播间的标题！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mIsTencentLogin) {
            //请求服务端创建直播间：
            createLiveRoom();
        } else {
            Toast.makeText(_mActivity, "连接服务器异常！", Toast.LENGTH_SHORT).show();
        }
    }

    private void createLiveRoom() {
        RestClient.builder()
                .url("live/start")
                .params("userId", USER_ID)
                .params("liveTitle", mLiveTilte.getText())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject jsonResponse = JSON.parseObject(response);
                        final int status = jsonResponse.getInteger("status");
                        final String message = jsonResponse.getString("msg");
                        final JSONObject liveRoomInfo = jsonResponse.getJSONObject("data");
                        if (status == 0) {
                            getSupportDelegate().startWithPop(new LiveDelegate(liveRoomInfo));
                        } else {
                            Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build()
                .post();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_create_live;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initToolbar();
        List<UserProfile> userProfileList = DataBaseManager
                .getInstance()
                .getDao()
                .queryRaw("where _id=?", new String[]{String.valueOf(USER_ID)});
        mUserProfile = userProfileList.get(0);
        //检查直播权限
        startTencentLiveCheck();
        //用户登录到Tencent的直播sdk上
        loginTencentLive();
    }

    private void initToolbar() {
        mToolbar.setTitle("开始我的直播");
        mToolbar.setTitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
    }

    private void loginTencentLive() {
        if (mUserProfile == null) {
            Toast.makeText(_mActivity, "用户未登录", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
           腾讯直播的账号，identifier字段：长度为4~24个字节，请使用英文字符和下划线，不能全为数字，大小写不敏感。
            通过用户信息构建该identifier字段：用户的id+labelwall，labelwall+id
        */
        final String accountStr = TencentLiveUserAccount.getTencentIdentifier();
        final String passwordStr = TencentLiveUserAccount.getTencentPassword();
        //调用腾讯IM登录
        ILiveLoginManager.getInstance().tlsLogin(accountStr, passwordStr, new ILiveCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                //登陆成功。
                loginLive(accountStr, data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //登录失败
                if (errCode == 229) {
                    //注册该账户
                    registerTencentLive(accountStr, passwordStr);
                }
            }
        });
    }

    private void loginLive(String accountStr, String data) {
        ILiveLoginManager.getInstance().iLiveLogin(accountStr, data, new ILiveCallBack() {

            @Override
            public void onSuccess(Object data) {
                //最终登录成功
                mIsTencentLogin = true;
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //登录失败
            }
        });
    }

    private void registerTencentLive(String account, String password) {
        ILiveLoginManager.getInstance().tlsRegister(account, password, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //修改Tencent Live上的nickName，将用户名设置为nickname
                //setNickName(mUserProfile.getUsername());
                //注册成功，再次登录
                loginTencentLive();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
            }
        });
    }

    private void setNickName(String nickname) {
        TIMFriendshipManager.getInstance().setNickName(nickname, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
            }
        });
    }
}
