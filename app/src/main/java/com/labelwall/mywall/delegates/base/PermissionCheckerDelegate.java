package com.labelwall.mywall.delegates.base;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.labelwall.mywall.ui.camera.CameraImageBean;
import com.labelwall.mywall.ui.camera.RequestCode;
import com.labelwall.mywall.ui.camera.WallCamera;
import com.labelwall.mywall.ui.scanner.ScannerDelegate;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;
import com.labelwall.mywall.util.file.FileUtil;
import com.yalantis.ucrop.UCrop;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2018-01-03.
 * 中间层，用于权限的判定，动态权限的设置使用permissionDispatcher插件
 */
@RuntimePermissions
public abstract class PermissionCheckerDelegate extends BaseDelegate {

    //不是直接调用方法
    //需要权限，权限名Manifest.permission.CAMERA
    @NeedsPermission(Manifest.permission.CAMERA)
    void startCamera() {
        WallCamera.start(this);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void startLiveCamera() {

    }

    //SD卡的读写权限
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void checkWrite() {

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void checkRed() {
    }

    //真正调用的方法
    public void startCameraWithCheck() {
        PermissionCheckerDelegatePermissionsDispatcher.startCameraWithCheck(this);//请求相机的权限
        PermissionCheckerDelegatePermissionsDispatcher.checkWriteWithCheck(this);//请求sd卡写权限
        PermissionCheckerDelegatePermissionsDispatcher.checkRedWithCheck(this);//请求sd卡的读权限
    }

    //权限被拒绝调用的方法
    @OnPermissionDenied(Manifest.permission.CAMERA)
    void onCameraDenied() {
        Toast.makeText(getContext(), "不允许拍照", Toast.LENGTH_SHORT).show();
    }

    //向用户解释为什么需要该权限
    @OnShowRationale(Manifest.permission.CAMERA)
    void onCameraRationale(PermissionRequest request) {
        showRationaleDialog(request);
    }

    private void showRationaleDialog(final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setPositiveButton("同意使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("权限管理")
                .show();
    }

    //勾选了不再提示禁止后调用的方法
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onCameraNever() {
        Toast.makeText(getContext(), "永久拒绝权限", Toast.LENGTH_SHORT).show();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onWriteaDenied() {
        Toast.makeText(getContext(), "不允许写文件", Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onWriteNever() {
        Toast.makeText(getContext(), "永久拒绝写文件", Toast.LENGTH_LONG).show();
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onWriteRationale(PermissionRequest request) {
        showRationaleDialog(request);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void onRedDenied() {
        Toast.makeText(getContext(), "不允读文件", Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void onRedNever() {
        Toast.makeText(getContext(), "永久拒绝读文件", Toast.LENGTH_LONG).show();
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void onRedRationale(PermissionRequest request) {
        showRationaleDialog(request);
    }


    //扫描二维码权限（不直接调用）
    @NeedsPermission(Manifest.permission.CAMERA)
    void startScan(BaseDelegate delegate) {
        delegate.getSupportDelegate().startForResult(new ScannerDelegate(), RequestCode.SCAN);
    }

    //真正调用的方法
    public void startScanWithCheck(BaseDelegate delegate) {
        PermissionCheckerDelegatePermissionsDispatcher.startScanWithCheck(this, delegate);
    }

    //处理请求权限的返回结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCheckerDelegatePermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 请求定位的权限
     */
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    void startInternatLocation() {

    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void startGPSLocation() {

    }

    //调用该方法
    public void startLocation() {
        PermissionCheckerDelegatePermissionsDispatcher.startGPSLocationWithCheck(this);
        PermissionCheckerDelegatePermissionsDispatcher.startInternatLocationWithCheck(this);
    }

    /**
     * 集成腾讯互动直播需要的权限
     */
    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    void startRecordAudio() {

    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    void startReadPhoneState() {

    }

    @NeedsPermission(value = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA})
    void startTencentLive() {

    }

    public void startTencentLiveCheck(){
        PermissionCheckerDelegatePermissionsDispatcher.startTencentLiveWithCheck(this);
    }



    //接收图片上传回调回来的数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.TAKE_PHOTO:
                    //获取拍摄图片的Uri
                    final Uri resultUri = CameraImageBean.getInstance().getPath();
                    //剪裁图片：参数1：原始路径，参数2：剪裁完存储的路径。
                    // 剪裁的图片路径覆盖之前的图片路径
                    UCrop.of(resultUri, resultUri)
                            .withMaxResultSize(400, 400)
                            .start(getContext(), this);
                    break;
                case RequestCode.PICK_PHOTO:
                    if (data != null) {
                        //需要创建一个文件
                        //相册图片的原路径
                        final Uri pickPath = data.getData();
                        //从相册选择后需要有个路径存放剪裁过的图片
                        final String pickCrop = WallCamera.createCropFile().getPath();
                        UCrop.of(pickPath, Uri.parse(pickCrop))
                                .withMaxResultSize(400, 400)
                                .start(getContext(), this);
                    }
                    break;
                case RequestCode.CROP_PHOTO:
                    //剪裁的回调处理（全局的回调），将剪裁的图片存储在全局的回调中
                    final Uri cropUri = UCrop.getOutput(data);
                    //将剪裁的图片uri存储在全局的回调中

                    //通过Tag获取回调接口，并执行接口中的方法
                    final IGlobalCallback<Uri> callback = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.ON_CROP);
                    if (callback != null) {
                        callback.executeCallback(cropUri);
                    }
                    break;
                case RequestCode.CROP_ERROR:
                    Toast.makeText(getContext(), "剪裁出错", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }


}
