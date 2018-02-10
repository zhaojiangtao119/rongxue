package com.labelwall.mywall.main.compass;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.compass.detail.ActivityDetailDelegate;
import com.labelwall.mywall.main.compass.detail.my.ActivityDetailMyDelegate;
import com.labelwall.mywall.main.compass.detail.my.join.ActivityDetailMyJoinDelegate;
import com.labelwall.mywall.main.user.UserProfileField;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.math.BigDecimal;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018-02-06.
 */

public class ActivityAdapter extends MultipleRecyclerViewAdapter {

    private final WallDelegate DELEGATE;
    private final long USER_ID =
            WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    private final static RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    public ActivityAdapter(List<MultipleItemEntity> data, WallDelegate delegate) {
        super(data);
        this.DELEGATE = delegate;
        addItemType(ItemType.ACTIVITY_LIST, R.layout.item_activity_list);
    }

    private void getUserJoinActivityIds() {

    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, final MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()) {
            case ItemType.ACTIVITY_LIST:
                final String theme = item.getField(ActivityFeilds.THEME);
                final String location = item.getField(ActivityFeilds.LOCATION);
                final String city = item.getField(ActivityFeilds.CITY);
                final String county = item.getField(ActivityFeilds.COUNTY);
                final String school = item.getField(ActivityFeilds.SCHOOL);
                final String type = item.getField(ActivityFeilds.TYPE);
                final String style = item.getField(ActivityFeilds.STYLE);
                final String poster = item.getField(ActivityFeilds.POSTER);
                final BigDecimal amount = item.getField(ActivityFeilds.AMOUNT);
                final Integer limitNum = item.getField(ActivityFeilds.LIMIT_NUM);
                final String content = item.getField(ActivityFeilds.CONTENT);

                final AppCompatTextView typeView = helper.getView(R.id.tv_activity_type);
                final AppCompatTextView styleView = helper.getView(R.id.tv_activity_style);
                final AppCompatTextView amountView = helper.getView(R.id.tv_activity_amount);
                final CircleImageView posterView = helper.getView(R.id.iv_activity_poster);
                final AppCompatTextView themeView = helper.getView(R.id.tv_activity_theme);
                final AppCompatTextView contentView = helper.getView(R.id.tv_activity_content);
                final AppCompatTextView locationView = helper.getView(R.id.tv_activity_location);
                final AppCompatTextView limitNumView = helper.getView(R.id.tv_activity_limit_num);

                typeView.setText(type);
                styleView.setText(style);
                amountView.setText(String.valueOf("金豆：" + amount));
                Glide.with(mContext)
                        .load(poster)
                        .apply(OPTIONS)
                        .into(posterView);
                themeView.setText(theme);
                contentView.setText(content);
                if (!StringUtils.isEmpty(school)) {
                    locationView.setText(school);
                } else if (StringUtils.isEmpty(school) && !StringUtils.isEmpty(location)) {
                    locationView.setText(location);
                }
                limitNumView.setText(String.valueOf(limitNum));
                //点击item跳转到活动详情
                //如何判断当前活动是用户创建的
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Integer activityId = item.getField(MultipleFields.ID);
                        //活动创建者的id
                        final Integer userId = item.getField(UserProfileField.USER_ID);
                        if (userId != USER_ID) {
                            //TODO 判断该活动是否是当前用户参加的活动，跳转
                            validateActivity(activityId);
                        } else {
                            DELEGATE.getSupportDelegate().start(ActivityDetailMyDelegate.create(activityId));
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private void validateActivity(final Integer activityId) {
        RestClient.builder()
                .url("activity/user/" + USER_ID + "/" + activityId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject jsonResponse = JSON.parseObject(response);
                        final int status = jsonResponse.getInteger("status");
                        if (status == 0) {
                            //参加了该活动
                            DELEGATE.getSupportDelegate().start(ActivityDetailMyJoinDelegate.create(activityId));
                        } else {
                            DELEGATE.getSupportDelegate().start(ActivityDetailDelegate.create(activityId));
                        }
                    }
                })
                .build()
                .get();
    }
}
