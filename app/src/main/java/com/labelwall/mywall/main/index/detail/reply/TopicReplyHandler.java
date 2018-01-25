package com.labelwall.mywall.main.index.detail.reply;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatEditText;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;

/**
 * Created by Administrator on 2018-01-16.
 * 回复的dialog
 */

public class TopicReplyHandler implements View.OnClickListener {

    private final Dialog DIALOG;
    //private final WallDelegate DELEGATE;
    private AppCompatEditText mTopicReplyContent;

    public TopicReplyHandler(WallDelegate delegate) {
        // this.DELEGATE = delegate;
        this.DIALOG = new AlertDialog.Builder(delegate.getContext()).create();
    }

    /*setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);*/
    public final void beginTopicReplyDialog() {
        DIALOG.show();
        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_create_topic_reply);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            //设置对话框属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;

            window.findViewById(R.id.topic_reply_submit).setOnClickListener(this);
            window.findViewById(R.id.topic_reply_cancel).setOnClickListener(this);

            mTopicReplyContent = (AppCompatEditText) window.findViewById(R.id.topic_reply_content);
            //使点击editText可以弹出键盘
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.topic_reply_submit) {
            String content = mTopicReplyContent.getText().toString();
            if (content != null && !content.equals("")) {
                if (mISubmitTopicReplyListener != null) {
                    mISubmitTopicReplyListener.submitTopicReply(content);
                }
            }
            DIALOG.cancel();
        } else if (viewId == R.id.topic_reply_cancel) {
            DIALOG.cancel();
        }
    }

    public ISubmitTopicReplyListener mISubmitTopicReplyListener;

    public void setSubmitTopicReplyListener(ISubmitTopicReplyListener listener) {
        this.mISubmitTopicReplyListener = listener;
    }

    public interface ISubmitTopicReplyListener {
        void submitTopicReply(String content);
    }
}
