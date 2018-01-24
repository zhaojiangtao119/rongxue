package com.labelwall.mywall.main.index;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.joanzapata.iconify.widget.IconTextView;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.WallBottomDelegate;
import com.labelwall.mywall.main.index.detail.TopicDetailDelegate;
import com.labelwall.mywall.main.index.usertopic.UserTopicDelegate;

import butterknife.BindView;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2018-01-09.
 * 设置点击事件
 */

public class IndexItemClickListener extends SimpleClickListener {

    private final WallDelegate DELEGATE;

    private IndexItemClickListener(WallDelegate delegate) {
        this.DELEGATE = delegate;
    }

    public static SimpleClickListener create(WallDelegate delegate) {
        return new IndexItemClickListener(delegate);
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
        if (viewId == R.id.topic_content ||
                viewId == R.id.topic_title ||
                viewId == R.id.topic_comment ||
                viewId == R.id.topic_create_time) {
            int topicId = (int) view.getTag();
            final TopicDetailDelegate topicDetailDelegate = TopicDetailDelegate.create(topicId);
            DELEGATE.getSupportDelegate().start(topicDetailDelegate);
        } else if (viewId == R.id.topic_image) {
            //TODO
            int topicId = (int) ((LinearLayoutCompat) view.getParent()).getTag();
            final TopicDetailDelegate topicDetailDelegate = TopicDetailDelegate.create(topicId);
            DELEGATE.getSupportDelegate().start(topicDetailDelegate);
        } else if (viewId == R.id.user_info) {
            int userId = (int) view.getTag();
            if (DELEGATE instanceof WallBottomDelegate) {
                final UserTopicDelegate userTopicDelegate = UserTopicDelegate.create(userId);
                DELEGATE.getSupportDelegate().start(userTopicDelegate);
            }
        } else if (viewId == R.id.topic_like) {
            LinearLayoutCompat textView = (LinearLayoutCompat) view;
            IconTextView iconTextView = (IconTextView) textView.getChildAt(0);
            CharSequence thumb = iconTextView.getText().toString();
            if (true) {
                // TODO 请求后台点赞
                iconTextView.setText(R.string.topic_like_thumbs);
            }
        }

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
