package com.labelwall.mywall.main;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.cart.ShopCartDelegate;
import com.labelwall.mywall.main.compass.ActivityDelegate;
import com.labelwall.mywall.main.index.IndexDelegate;
import com.labelwall.mywall.main.sort.SortDelegate;
import com.labelwall.mywall.main.user.UserDelegate;

/**
 * Created by Administrator on 2018-03-10.
 */

public class MainPageDialog implements View.OnClickListener {

    private final AlertDialog DIALOG;
    private final WallDelegate DELEGATE;
    private final LayoutInflater INFLATER;

    public MainPageDialog(WallDelegate delegate) {
        this.DELEGATE = delegate;
        this.DIALOG = new AlertDialog.Builder(delegate.getContext()).create();
        this.INFLATER = LayoutInflater.from(delegate.getContext());
    }

    public void initDialog() {
        DIALOG.show();
        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_main_type);
            window.setGravity(Gravity.CENTER);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            window.findViewById(R.id.tv_topic).setOnClickListener(this);
            window.findViewById(R.id.tv_product).setOnClickListener(this);
            window.findViewById(R.id.tv_shopcart).setOnClickListener(this);
            window.findViewById(R.id.tv_activity).setOnClickListener(this);
            window.findViewById(R.id.tv_user_profile).setOnClickListener(this);
            window.findViewById(R.id.tv_6).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.tv_topic:
                DIALOG.dismiss();
                DELEGATE.getSupportDelegate().start(new IndexDelegate());
                break;
            case R.id.tv_product:
                DELEGATE.getSupportDelegate().start(new SortDelegate());
                DIALOG.dismiss();
                break;
            case R.id.tv_shopcart:
                DELEGATE.getSupportDelegate().start(new ActivityDelegate());
                DIALOG.dismiss();
                break;
            case R.id.tv_activity:
                DELEGATE.getSupportDelegate().start(new ShopCartDelegate());
                DIALOG.dismiss();
                break;
            case R.id.tv_user_profile:
                DELEGATE.getSupportDelegate().start(new UserDelegate());
                DIALOG.dismiss();
                break;
            case R.id.tv_6:
                DELEGATE.getSupportDelegate().startWithPop(new MainPageDelegate());
                DIALOG.dismiss();
                break;
            default:
                break;
        }
    }
}
