package com.labelwall.mywall.ui.recycler;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2018-01-25.
 */

public class MultipleRecyclerViewHolder extends BaseViewHolder {

    private MultipleRecyclerViewHolder(View view) {
        super(view);
    }

    public static MultipleRecyclerViewHolder create(View view) {
        return new MultipleRecyclerViewHolder(view);
    }
}
