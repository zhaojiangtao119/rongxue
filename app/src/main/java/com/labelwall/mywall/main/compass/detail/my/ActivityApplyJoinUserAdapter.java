package com.labelwall.mywall.main.compass.detail.my;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018-02-07.
 */

public class ActivityApplyJoinUserAdapter extends MultipleRecyclerViewAdapter {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    public ActivityApplyJoinUserAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(ItemType.APPLY_JOIN_USERS, R.layout.item_activity_apply_join_user);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, final MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()) {
            case ItemType.APPLY_JOIN_USERS:
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
                final AppCompatTextView applyJoinUser = helper.getView(R.id.tv_activity_apply_join_user);

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

                applyJoinUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Integer userId = item.getField(UserProfileField.USER_ID);
                        agreeJoin(userId);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void agreeJoin(Integer userId) {
        //TODO 请求服务器同意申请用户加入，修改UI的展示
    }
}
