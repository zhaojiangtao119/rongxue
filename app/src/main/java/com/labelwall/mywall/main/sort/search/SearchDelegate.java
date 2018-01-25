package com.labelwall.mywall.main.sort.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;

/**
 * Created by Administrator on 2018-01-25.
 */

public class SearchDelegate extends WallDelegate {

    @Override
    public Object setLayout() {
        return R.layout.delegate_search_product;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
