package com.labelwall.mywall.ui.camera;

import android.net.Uri;

import com.labelwall.mywall.delegates.base.PermissionCheckerDelegate;
import com.labelwall.mywall.util.file.FileUtil;

/**
 * Created by Administrator on 2018-01-13.
 * 相机调用类
 */

public class WallCamera {
    //剪裁的图片文件
    public static Uri createCropFile() {
        return Uri.parse(FileUtil.createFile("crop_image",
                FileUtil.getFileNameByTime("IMG", "jpg")).getPath());
    }

    public static void start(PermissionCheckerDelegate delegate) {
        new CameraHandler(delegate).beginCameraDialog();
    }
}
