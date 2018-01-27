package com.labelwall.mywall.main.user;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.main.user.profile.UserProfileDelegate;
import com.labelwall.mywall.main.user.settings.SettingsDelegate;

import retrofit2.http.DELETE;

/**
 * Created by Administrator on 2018-01-17.
 * userDelegate处理Item单击事件
 */

public class UserClickListener extends SimpleClickListener {

    private final WallDelegate DELEGATE;

    public UserClickListener(WallDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);
        int id = bean.getId();
        switch (id) {
            case UserSettingItem.USER_PROFILE:
                DELEGATE.getParentDelegate().getSupportDelegate().start(bean.getDelegate());
                break;
            case UserSettingItem.ADDRESS:
                DELEGATE.getParentDelegate().getSupportDelegate().start(bean.getDelegate());
                break;
            case UserSettingItem.SYSTEM:
                DELEGATE.getParentDelegate().getSupportDelegate().start(bean.getDelegate());
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
