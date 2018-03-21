package com.labelwall.mywall.main.live.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.MyWallApplication;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.live.util.LiveRoomControlState;
import com.labelwall.mywall.main.live.util.MessageObservable;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.livesdk.ILVText;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-03-17.
 */

public class LiveDelegate extends WallDelegate {

    @BindView(R2.id.arv_root)
    AVRootView mAvRootView = null;
    @BindView(R2.id.iv_switch_camera)
    AppCompatImageView mSwitchCamera = null;

    @OnClick(R2.id.iv_switch_camera)
    void onClickSwitchCamera() {
        if (ILiveConstants.NONE_CAMERA != ILiveRoomManager.getInstance().getActiveCameraId()) {
            ILiveRoomManager.getInstance().switchCamera(1 - ILiveRoomManager.getInstance().getActiveCameraId());
        }else{
            ILiveRoomManager.getInstance().switchCamera(ILiveConstants.FRONT_CAMERA);
        }
    }

    private LiveRoomControlState mRoomState = new LiveRoomControlState();
    private final JSONObject LIVE_ROOM_INFO;
    private int mUserId = -1;
    private int mRoomId = -1;
    private Timer mHeartBeatTimer = new Timer();

    public LiveDelegate(JSONObject liveRoomInfo) {
        this.LIVE_ROOM_INFO = liveRoomInfo;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_live_now;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ILVLiveManager.getInstance().setAvVideoView(mAvRootView);
        initRoomInfo();
        initLiveRoom();
    }


    private void initLiveRoom() {
        ILVLiveConfig liveConfig = MyWallApplication.getILVLiveConfig();
        liveConfig.setLiveMsgListener(new ILVLiveConfig.ILVLiveMsgListener() {
            @Override
            public void onNewTextMsg(ILVText text, String SenderId, TIMUserProfile userProfile) {
                //接收到文本消息
            }

            @Override
            public void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile) {
                //接收到自定义消息(列表消息,弹幕消息，礼物消息（连续礼物，全屏礼物），观众进入，离开)
            }

            @Override
            public void onNewOtherMsg(TIMMessage message) {
                //接收到其他消息
            }
        });

        //创建房间配置项
        ILVLiveRoomOption hostOption = new ILVLiveRoomOption(ILiveLoginManager.getInstance().getMyUserId()).
                controlRole("LiveMaster")//角色设置
                .autoFocus(true)
                .autoMic(mRoomState.isVoiceOn())//麦克风，默认打开
                .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)//权限设置
                .cameraId(mRoomState.getCameraid())//摄像头前置后置，默认前摄像头
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);//是否开始半自动接收

        //创建房间
        ILVLiveManager.getInstance().createRoom(mRoomId, hostOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //发送心跳包，确保直播间是正常运行的
                startHeartBeat();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //失败的情况下，退出界面。
                Toast.makeText(_mActivity, "创建直播失败！" + errCode + "," + errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startHeartBeat() {
        //参数1：task异步任务，参数2：延迟时间，参数3：执行周期
        mHeartBeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                RestClient.builder()
                        .url("live/heart")
                        .params("roomId", mRoomId)
                        .params("userId", mUserId)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                Log.e("HeartBeat", response);
                            }
                        })
                        .build()
                        .post();
            }
        }, 0, 5000);
    }

    private void initRoomInfo() {
        mRoomId = LIVE_ROOM_INFO.getInteger("id");
        mUserId = LIVE_ROOM_INFO.getInteger("userId");
    }

    @Override
    public void onPause() {
        super.onPause();
        ILVLiveManager.getInstance().onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        ILVLiveManager.getInstance().onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //停止向服务端发送心跳包
        mHeartBeatTimer.cancel();
    }


    @Override
    public boolean onBackPressedSupport() {
        quitLive();
        return super.onBackPressedSupport();
    }

    private void quitLive() {
        ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                //
                logout();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                logout();
            }
        });
    }

    private void logout() {
        //请求服务关闭直播
        RestClient.builder()
                .url("live/logout")
                .params("userId", mUserId)
                .params("id", mRoomId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                    }
                })
                .build()
                .put();
    }
}
