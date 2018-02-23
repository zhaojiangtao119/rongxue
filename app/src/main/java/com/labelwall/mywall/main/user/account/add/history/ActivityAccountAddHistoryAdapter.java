package com.labelwall.mywall.main.user.account.add.history;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.main.user.account.ActivityAccountOrderField;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018-02-22.
 */

public class ActivityAccountAddHistoryAdapter extends MultipleRecyclerViewAdapter {

    public ActivityAccountAddHistoryAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(ItemType.ACCOUNT_ORDER_LIST, R.layout.item_account_order);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()) {
            case ItemType.ACCOUNT_ORDER_LIST:
                final int id = item.getField(MultipleFields.ID);
                final int acitivityId = item.getField(ActivityAccountOrderField.ACCOUNT_ID);
                final String orderNo = item.getField(ActivityAccountOrderField.ORDER_NO);
                final String typeDesc = item.getField(ActivityAccountOrderField.TYPE_DESC);
                final double orderPrice = item.getField(ActivityAccountOrderField.ORDER_PRICE);
                final String orderInfo = item.getField(ActivityAccountOrderField.ORDER_INFO);
                final String createTime = item.getField(ActivityAccountOrderField.ORDER_CREATE_TIME);
                final String statusDesc = item.getField(ActivityAccountOrderField.STATUS_DESC);

                final AppCompatTextView orderNoView = helper.getView(R.id.tv_account_order_no);
                final AppCompatTextView orderPriceView = helper.getView(R.id.tv_account_order_price);
                final AppCompatTextView createTimeView = helper.getView(R.id.tv_account_order_create_time);
                final AppCompatTextView typeDescView = helper.getView(R.id.tv_account_order_type);
                final AppCompatTextView statuDescView = helper.getView(R.id.tv_account_order_status);
                final IconTextView iconOrder_detail = helper.getView(R.id.icon_account_order_detail);
                iconOrder_detail.setVisibility(View.INVISIBLE);
                orderNoView.setText(orderNo);
                orderPriceView.setText("￥" + String.valueOf(orderPrice));
                createTimeView.setText(createTime);
                typeDescView.setText(typeDesc);
                statuDescView.setText(statusDesc);
                break;
            default:
                break;
        }
    }
}
