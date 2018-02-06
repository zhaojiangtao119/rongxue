package com.labelwall.mywall.main.compass;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;

import java.math.BigDecimal;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018-02-06.
 */

public class ActivityAdapter extends MultipleRecyclerViewAdapter {

    private final WallDelegate DELEGATE;

    private final static RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    public ActivityAdapter(List<MultipleItemEntity> data, WallDelegate delegate) {
        super(data);
        this.DELEGATE = delegate;
        addItemType(ItemType.ACTIVITY_LIST, R.layout.item_activity_list);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()) {
            case ItemType.ACTIVITY_LIST:
                final Integer id = item.getField(MultipleFields.ID);
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
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            default:
                break;
        }
    }

}
