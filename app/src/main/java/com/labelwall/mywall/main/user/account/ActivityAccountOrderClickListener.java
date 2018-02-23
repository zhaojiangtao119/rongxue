package com.labelwall.mywall.main.user.account;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.account.detail.ActivityAccountOrderDetailDelegate;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;

/**
 * Created by Administrator on 2018-02-23.
 */

public class ActivityAccountOrderClickListener extends SimpleClickListener {

    private final WallDelegate DELEGATE;

    public ActivityAccountOrderClickListener(WallDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
        final int orderId = entity.getField(MultipleFields.ID);
        final int activityId = entity.getField(ActivityAccountOrderField.ACTIVITY_ID);
        DELEGATE.getSupportDelegate().start(new ActivityAccountOrderDetailDelegate(orderId, activityId));
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
