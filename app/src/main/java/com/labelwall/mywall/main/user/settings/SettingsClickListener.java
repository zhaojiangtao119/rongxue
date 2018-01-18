package com.labelwall.mywall.main.user.settings;

import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.main.user.profile.UserProfileSettingItem;
import com.labelwall.mywall.ui.date.DateDialogUtil;

/**
 * Created by Administrator on 2018-01-18.
 */

public class SettingsClickListener extends SimpleClickListener {

    private final WallDelegate DELEGATE;

    public SettingsClickListener(WallDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);
        int id = bean.getId();
        final TextView tv = (TextView) view.findViewById(R.id.tv_arrow_value);
        switch (id) {
            case 1:
                break;
            case 2:

                break;
            default:
                break;
        }
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
