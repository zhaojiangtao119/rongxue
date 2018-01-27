package com.labelwall.mywall.main.user.address;

import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

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

    public AddressAdapter(List<MultipleItemEntity> data, WallDelegate delegate) {
        super(data);
        this.DELEGATE = delegate;
        addItemType(ItemType.ADDRESS_LIST, R.layout.item_address_list);
    }

    @Override
    protected void convert(final MultipleRecyclerViewHolder helper, MultipleItemEntity item) {
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
                final AppCompatRadioButton selectedView = helper.getView(R.id.btn_address_selected);

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
                        deleteAddress(helper, id);
                    }
                });
                //选中按钮
                if (selected == 1) {
                    selectedView.setChecked(true);
                } else {
                    selectedView.setChecked(false);
                }
                selectedView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Toast.makeText(DELEGATE.getContext(), "ischecked:" + String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                break;
        }
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
