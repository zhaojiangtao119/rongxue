package com.labelwall.mywall.main.index.detail;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.index.usertopic.UserTopicDelegate;

/**
 * Created by Administrator on 2018-01-11.
 */

public class TopicDetailClickListener extends SimpleClickListener {

    private WallDelegate DELEGATE;

    private TopicDetailClickListener(WallDelegate delegate) {
        this.DELEGATE = delegate;
    }

    public static TopicDetailClickListener create(WallDelegate delegate) {
        return new TopicDetailClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        int viewId = view.getId();
        if(viewId == R.id.user_info){
            final int userId = (int) view.getTag();
            final UserTopicDelegate userTopicDelegate = UserTopicDelegate.create(userId);
            DELEGATE.getSupportDelegate().start(userTopicDelegate);
        }
    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
