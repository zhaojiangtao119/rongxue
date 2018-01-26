package com.labelwall.mywall.main.user.order;

import android.support.v7.widget.AppCompatTextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018-01-26.
 */

public class OrderListAdapter extends MultipleRecyclerViewAdapter {

    public OrderListAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(ItemType.ORDER_LIST, R.layout.item_order_list);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()) {
            case ItemType.ORDER_LIST:
                final AppCompatTextView orderNoView = helper.getView(R.id.tv_order_no);
                final AppCompatTextView orderPriceView = helper.getView(R.id.tv_order_price);
                final AppCompatTextView orderCreateTimeView = helper.getView(R.id.tv_order_create_time);
                final AppCompatTextView orderPayTypeView = helper.getView(R.id.tv_order_pay_type);
                final AppCompatTextView orderPayStatusView = helper.getView(R.id.tv_order_pay_status);
                final IconTextView orderDetailView = helper.getView(R.id.icon_order_detail);

                final Long orderNo = item.getField(OrderListField.ORDER_NO);
                final BigDecimal orderPrice = item.getField(OrderListField.TOTAL_PRICE);
                final String orderCreateTime = item.getField(OrderListField.CREATE_TIME);
                final String payType = item.getField(OrderListField.PAYMENT_TYPE);
                final String status = item.getField(OrderListField.STATUS);
                orderNoView.setText(String.valueOf(orderNo));
                orderPriceView.setText("￥" + String.valueOf(orderPrice));
                orderCreateTimeView.setText(orderCreateTime);
                orderPayTypeView.setText(payType);
                orderPayStatusView.setText(status);
                break;
            default:
                break;
        }
    }
}
