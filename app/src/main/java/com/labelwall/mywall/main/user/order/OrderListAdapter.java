package com.labelwall.mywall.main.user.order;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.cart.order.OrderDetailDelegate;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.http.DELETE;

/**
 * Created by Administrator on 2018-01-26.
 */

public class OrderListAdapter extends MultipleRecyclerViewAdapter {

    private final WallDelegate DELEGATE;
    private final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    public OrderListAdapter(List<MultipleItemEntity> data, WallDelegate delegate) {
        super(data);
        this.DELEGATE = delegate;
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

                orderDetailView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到order的详情页面
                        uploadOrderDetail(orderNo);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void uploadOrderDetail(Long orderNo) {
        RestClient.builder()
                .url("app/order/detail")
                .params("userId", USER_ID)
                .params("orderNo", orderNo)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject data = JSON.parseObject(response);
                        //跳转到订单详情页面
                        DELEGATE.getSupportDelegate().start(new OrderDetailDelegate(data));
                    }
                })
                .build()
                .get();
    }
}
