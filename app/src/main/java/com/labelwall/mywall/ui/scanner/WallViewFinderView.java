package com.labelwall.mywall.ui.scanner;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.labelwall.mywall.R;

import me.dm7.barcodescanner.core.ViewFinderView;

/**
 * Created by Administrator on 2018-01-25.
 */

public class WallViewFinderView extends ViewFinderView {

    public WallViewFinderView(Context context) {
        this(context, null);
    }

    public WallViewFinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mSquareViewFinder = true;//扫描框是正方形
        mBorderPaint.setColor(ContextCompat.getColor(getContext(), R.color.app_title));
        mLaserPaint.setColor(ContextCompat.getColor(getContext(), R.color.app_title));
    }
}
