package com.labelwall.mywall.main.index.topic;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.delegates.launcher.ScrollLauncherTag;
import com.labelwall.mywall.main.WallBottomDelegate;
import com.labelwall.mywall.main.cart.ShopCartDelegate;
import com.labelwall.mywall.ui.widget.AutoPhotoLayout;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.qiniu.QnUploadHelper;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;
import com.qiniu.android.http.ResponseInfo;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

import static android.R.attr.onClick;

/**
 * Created by Administrator on 2018-01-12.
 */

public class CreateTopicDelegate extends WallDelegate {

    @BindView(R2.id.topic_comment)
    AppCompatEditText mTopicCommentEditText;
    @BindView(R2.id.custom_auto_photo_layout)
    AutoPhotoLayout mAutoPhotoLayout;
    @BindView(R.id.topic_submit_btn)
    AppCompatButton mButton;
    private StringBuilder mImagesUrl = new StringBuilder();

    //当前用户的id
    private long mUserId = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private final WallBottomDelegate WALL_BOTTOM_DELEGATE;

    public CreateTopicDelegate(WallBottomDelegate bottomItemDelegate) {
        this.WALL_BOTTOM_DELEGATE = bottomItemDelegate;
    }

    @OnClick(R.id.topic_submit_btn)
    void onSubmitBtn() {
        final CallbackManager manager = CallbackManager.getInstance().addCallback(CallbackType.IMAGES, new IGlobalCallback<Map<Integer, Uri>>() {
            @Override
            public void executeCallback(Map<Integer, Uri> args) {
                //循环遍历将多张图片上传到七牛云，
                for (final Map.Entry<Integer, Uri> entry : args.entrySet()) {
                    uploadImageToQiniu(entry.getValue().getPath());
                }
            }
        });
        AutoPhotoLayout.setSubmitBtn(mButton);
    }

    private void uploadImageToQiniu(final String path) {
        final String name = "topic_id_" + mUserId + System.currentTimeMillis() + "_images";
        QnUploadHelper.uploadPic(path, name, new QnUploadHelper.UploadCallBack() {
            @Override
            public void success(String url) {
                //上传成功，拼接url
                mImagesUrl.append(name);
                //TODO 只上传一张图片
                onCreateTopic();
            }

            @Override
            public void fail(String key, ResponseInfo info) {
                //上传失败！
                throw new RuntimeException("image upload fail");
            }
        });
        //mImagesUrl.append(name).append(",");
    }

    private void onCreateTopic() {
        //TODO 将帖子信息（content+images+userId）请求到服务器
        //Log.e("图片上传成功：", mImagesUrl.toString());
        RestClient.builder()
                .url("app/topic/publish_post")
                .params("userId", String.valueOf(mUserId))
                .params("image", mImagesUrl)
                .params("content", mTopicCommentEditText.getText().toString())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //TODO 请求成功，跳转页面到index
                       /* final int sortTab = 0;
                        final BottomItemDelegate sortDelegate = WALL_BOTTOM_DELEGATE.getItemDelegates().get(sortTab);
                        WALL_BOTTOM_DELEGATE.getSupportDelegate()
                                .showHideFragment(sortDelegate, CreateTopicDelegate.this);
                        WALL_BOTTOM_DELEGATE.changeColor(sortTab);*/
                        getSupportDelegate().pop();
                    }
                })
                .build()
                .post();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_create_topic;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mAutoPhotoLayout.setDelegate(this);
        CallbackManager.getInstance().addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
            @Override
            public void executeCallback(Uri args) {//已经是剪裁后的图片，保存起来，用来提交
                mAutoPhotoLayout.onCropTarget(args);//将剪裁后的图片显示到控件中
            }
        });
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
