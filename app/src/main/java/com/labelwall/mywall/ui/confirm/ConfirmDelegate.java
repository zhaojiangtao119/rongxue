package com.labelwall.mywall.ui.confirm;

import android.support.v7.app.AlertDialog;

import com.labelwall.mywall.delegates.base.WallDelegate;

/**
 * Created by Administrator on 2018-01-27.
 */

public class ConfirmDelegate {

    private final WallDelegate DELEGATE;
    private final AlertDialog DIALOG;

    public ConfirmDelegate(WallDelegate delegate) {
        this.DELEGATE = delegate;
        this.DIALOG = new AlertDialog.Builder(DELEGATE.getContext()).create();
    }

    public void beginConfirmDialog() {

    }
}
