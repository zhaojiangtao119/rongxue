package com.labelwall.mywall.main.compass.detail;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.labelwall.mywall.R;
import com.labelwall.mywall.main.user.UserProfileField;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018-02-07.
 */

public class ActivityJoinUserAdapter extends MultipleRecyclerViewAdapter {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();
    private final Integer ACTIVITY_ID;
    private final long USER_ID =
            WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    public ActivityJoinUserAdapter(List<MultipleItemEntity> data, Integer activityId) {
        super(data);
        this.ACTIVITY_ID = activityId;
        addItemType(ItemType.JOIN_USERS, R.layout.item_activity_join_user);
        addItemType(ItemType.APPLY_JOIN_USERS, R.layout.item_activity_apply_join_user);
        addItemType(ItemType.MY_JOIN_USERS, R.layout.item_activity_my_join_user);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, final MultipleItemEntity item) {
        super.convert(helper, item);
        final String username = item.getField(UserProfileField.USERNAME);
        final String avatar = item.getField(UserProfileField.AVATAR);
        final String schoolName = item.getField(UserProfileField.SCHOOL_NAME);
        final String city = item.getField(UserProfileField.CITY);
        final String county = item.getField(UserProfileField.COUNTY);
        final String province = item.getField(UserProfileField.PROVINCE);

        final CircleImageView avatarView = helper.getView(R.id.circle_join_user_avatar);
        final AppCompatTextView usernameView = helper.getView(R.id.tv_join_user_username);
        final AppCompatTextView schoolView = helper.getView(R.id.tv_join_user_school);
        final AppCompatTextView locationView = helper.getView(R.id.tv_join_user_location);

        Glide.with(mContext)
                .load(avatar)
                .apply(OPTIONS)
                .into(avatarView);
        usernameView.setText(username);
        schoolView.setText(schoolName);
        if (!StringUtils.isEmpty(province) && !StringUtils.isEmpty(city)) {
            if (province.equals(city)) {
                locationView.setText(province + county);
            } else {
                locationView.setText(province + city + county);
            }
        }
        switch (helper.getItemViewType()) {
            case ItemType.JOIN_USERS:
                break;
            case ItemType.APPLY_JOIN_USERS:
                final AppCompatTextView applyJoinUser = helper.getView(R.id.tv_activity_apply_join_user);
                applyJoinUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Integer userId = item.getField(UserProfileField.USER_ID);
                        agreeJoin(userId, applyJoinUser);
                    }
                });
                break;
            case ItemType.MY_JOIN_USERS:
                final String email = item.getField(UserProfileField.EMAIL);
                final AppCompatTextView emailView = helper.getView(R.id.tv_join_user_email);
                emailView.setText(email);
                break;
            default:
                break;
        }
    }

    private void agreeJoin(Integer userId, final AppCompatTextView applyJoinUser) {
        //TODO 请求服务器同意申请用户加入，修改UI的展示
        RestClient.builder()
                .url("activity/agreeJoin/" + USER_ID + "/" + ACTIVITY_ID + "/" + userId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject jsonResponse = JSON.parseObject(response);
                        final int status = jsonResponse.getInteger("status");
                        final String message = jsonResponse.getString("msg");
                        if (status == 2) {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        } else if (status == 0) {
                            applyJoinUser.setText("已经加入活动");
                        }
                    }
                })
                .build()
                .put();
    }
}
