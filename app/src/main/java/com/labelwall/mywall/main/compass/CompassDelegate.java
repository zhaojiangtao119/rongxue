package com.labelwall.mywall.main.compass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;

/**
 * Created by Administrator on 2018-01-04.
 */

public class CompassDelegate extends BottomItemDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_compass;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
