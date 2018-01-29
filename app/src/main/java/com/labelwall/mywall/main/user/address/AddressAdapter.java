package com.labelwall.mywall.main.user.address;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.List;

import retrofit2.http.DELETE;

/**
 * Created by Administrator on 2018-01-27.
 */

public class AddressAdapter extends MultipleRecyclerViewAdapter {

    private final WallDelegate DELEGATE;
    private final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private int mProPosition = 0;
    private final Long ORDER_NO;

    public AddressAdapter(List<MultipleItemEntity> data, WallDelegate delegate, Long orderNo) {
        super(data);
        this.DELEGATE = delegate;
        this.ORDER_NO = orderNo;
        addItemType(ItemType.ADDRESS_LIST, R.layout.item_address_list);
    }

    @Override
    protected void convert(final MultipleRecyclerViewHolder helper, final MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()) {
            case ItemType.ADDRESS_LIST:
                final AppCompatTextView nameView = helper.getView(R.id.tv_address_username);
                //final AppCompatTextView phoneView = helper.getView(R.id.tv_address_phone);
                final AppCompatTextView mobileView = helper.getView(R.id.tv_address_mobile);
                final AppCompatTextView locationView = helper.getView(R.id.tv_address_location);
                final AppCompatTextView addressView = helper.getView(R.id.tv_address_detail);
                final AppCompatTextView zipView = helper.getView(R.id.tv_address_zip);
                final AppCompatTextView deleteView = helper.getView(R.id.tv_address_delete);
                final IconTextView selectedView = helper.getView(R.id.icon_address_select);

                final Integer id = item.getField(MultipleFields.ID);
                final String name = item.getField(AddressField.RECEIVER_NAME);
                final String phone = item.getField(AddressField.RECEIVER_PHONE);
                final String mobile = item.getField(AddressField.RECEIVER_MOBILE);
                final String province = item.getField(AddressField.RECEIVER_PROVINCE);
                final String city = item.getField(AddressField.RECEIVER_CITY);
                final String county = item.getField(AddressField.RECEIVER_COUNTY);
                final String address = item.getField(AddressField.RECEIVER_ADDRESS);
                final String zip = item.getField(AddressField.RECEIVER_ZIP);
                final Integer selected = item.getField(AddressField.SELECTED);

                nameView.setText(name);
                //phoneView.setText(phone);
                mobileView.setText(mobile);
                locationView.setText(province + city);
                addressView.setText(address);
                zipView.setText(zip);

                deleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除地址
                        final AlertDialog alertDialog = new AlertDialog.Builder(DELEGATE.getContext()).create();
                        alertDialog.setTitle("删除收货地址");
                        alertDialog.setMessage("是否删除？");
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAddress(helper, id);
                            }
                        });
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                });

                selectedView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int currentPosition = helper.getAdapterPosition();
                        RestClient.builder()
                                .url("app/shopping/default")
                                .params("userId", USER_ID)
                                .params("shoppingId", id)
                                .loader(DELEGATE.getContext())
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        if (mProPosition != currentPosition) {
                                            getData().get(mProPosition).setField(AddressField.SELECTED, 0);
                                            notifyItemChanged(mProPosition);
                                            item.setField(AddressField.SELECTED, 1);
                                            notifyItemChanged(currentPosition);
                                            mProPosition = currentPosition;
                                            //TODO 选中默认的地址之后，修改数据库中默认的配送地址
                                            final Integer id = item.getField(MultipleFields.ID);
                                            //updateSelectAddress(id);
                                        }
                                    }
                                })
                                .build()
                                .put();
                    }
                });

                if (selected == 1) {//选中
                    selectedView.setTextColor(ContextCompat.getColor(DELEGATE.getContext(), R.color.app_title));
                } else {
                    selectedView.setTextColor(Color.GRAY);
                }
                break;
            default:
                break;
        }
    }

    private void updateSelectAddress(Integer shoppingId) {

    }

    //请求删除地址
    private void deleteAddress(final MultipleRecyclerViewHolder helper, Integer id) {
        RestClient.builder()
                .url("app/shopping/remove")
                .params("userId", USER_ID)
                .params("shoppingId", id)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        remove(helper.getLayoutPosition());
                    }
                })
                .build()
                .delete();
    }
}
