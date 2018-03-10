package com.labelwall.mywall.main.user;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.main.user.account.ActivityAccountDelegate;
import com.labelwall.mywall.main.user.address.AdressDelegate;
import com.labelwall.mywall.main.user.list.ListAdapter;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.main.user.list.ListItemType;
import com.labelwall.mywall.main.user.order.OrderListDelegate;
import com.labelwall.mywall.main.user.profile.UserProfileDelegate;
import com.labelwall.mywall.main.user.settings.SettingsDelegate;
import com.labelwall.mywall.ui.camera.CameraHandler;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018-01-04.
 */

public class UserDelegate extends BottomItemDelegate {

    @BindView(R2.id.img_user_avatar)
    CircleImageView mUserAvatar = null;
    @BindView(R2.id.tv_user_name)
    AppCompatTextView mUsername = null;
    @BindView(R2.id.rv_personal_setting)
    RecyclerView mRecyclerView = null;

    private long mUserId = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private UserProfile mUserProfile = null;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    @OnClick(R2.id.img_user_avatar)
    void changeUserAvatar() {
        startCameraWithCheck();
        //通过全局的回调来获取剪裁后的图片Uri
        CallbackManager callbackManager = CallbackManager
                .getInstance()
                .addCallback(CallbackType.ON_CROP, new IGlobalCallback() {
                    @Override
                    public void executeCallback(Object args) {
                        Glide.with(_mActivity)
                                .load(args)
                                .apply(OPTIONS)
                                .into(mUserAvatar);
                    }
                });
    }

    @OnClick(R2.id.tc_all_account_arrow)
    void onClickOrderList() {//加载所有的订单
        getParentDelegate().getSupportDelegate().start(new OrderListDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_user;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    /**
     * 修改为懒加载
     *
     * @param savedInstanceState
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //加载用户的基本信息
        findCurrentUserProfile();
        //加载recyclerView的item
        uploadSettingItem();
    }

    private void findCurrentUserProfile() {
        if (mUserId != -1) {
            //greenDao中获取用户信息
            List<UserProfile> userProfileList = DataBaseManager
                    .getInstance()
                    .getDao()
                    .queryRaw("where _id=?", new String[]{String.valueOf(mUserId)});
            if (userProfileList != null && userProfileList.size() > 0) {
                mUserProfile = userProfileList.get(0);
                bindUserProfile();
            }
        }
    }

    private void bindUserProfile() {
        Glide.with(_mActivity)
                .load(mUserProfile.getHead())
                .apply(OPTIONS)
                .into(mUserAvatar);
        mUsername.setText(mUserProfile.getUsername());
    }

    private void uploadSettingItem() {

        final ListBean userProfile = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL_NO_HINT)
                .setId(UserSettingItem.USER_PROFILE)
                .setText(UserSettingItem.USER_PROFILE_VALUE)
                .setDelegate(new UserProfileDelegate())
                .build();
        final ListBean address = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL_NO_HINT)
                .setId(UserSettingItem.ADDRESS)
                .setText(UserSettingItem.ADDRESS_VALUE)
                .setDelegate(new AdressDelegate(null))
                .build();
        final ListBean systems = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL_NO_HINT)
                .setId(UserSettingItem.SYSTEM)
                .setText(UserSettingItem.SYSTEM_VALUE)
                .setDelegate(new SettingsDelegate())
                .build();
        final ListBean activityAccount = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL_NO_HINT)
                .setId(UserSettingItem.ACTIVITY_ACCOUNT)
                .setText(UserSettingItem.ACTIVITY_ACCOUNT_VALUE)
                .setDelegate(new ActivityAccountDelegate())
                .build();
        final List<ListBean> data = new ArrayList<>();
        data.add(userProfile);
        data.add(address);
        data.add(systems);
        data.add(activityAccount);

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new UserClickListener(this));
    }
}
