package com.labelwall.mywall.main.user.profile;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;

/**
 * Created by Administrator on 2018-01-17.
 */

public class UserProfileNomalDialog implements View.OnClickListener {

    private final AlertDialog DIALOG;
    private AppCompatEditText mUserProfile;

    public UserProfileNomalDialog(WallDelegate delegate) {
        DIALOG = new AlertDialog.Builder(delegate.getContext()).create();
    }

    public final void beginProfileDialog() {
        DIALOG.show();
        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_setting_profile);
            window.setGravity(Gravity.TOP);
            window.setWindowAnimations(R.style.anim_panel_up_from_buttom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;

            mUserProfile = (AppCompatEditText) window.findViewById(R.id.et_user_profile);
            window.findViewById(R.id.user_profile_submit).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (mISubmitUserProfile != null) {
            mISubmitUserProfile.onSubmitProfile(mUserProfile.getText().toString());
        }
        DIALOG.cancel();
    }

    private ISubmitUserProfileListener mISubmitUserProfile;

    public void setSubmitUserProfileListener(ISubmitUserProfileListener listener) {
        this.mISubmitUserProfile = listener;
    }

    public interface ISubmitUserProfileListener {
        void onSubmitProfile(String content);
    }
}
