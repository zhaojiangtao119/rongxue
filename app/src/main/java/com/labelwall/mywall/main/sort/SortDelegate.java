package com.labelwall.mywall.main.sort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.main.sort.content.ContentDelegate;
import com.labelwall.mywall.main.sort.list.VerticalListDelegate;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-01-04.
 */

public class SortDelegate extends BottomItemDelegate {

    @BindView(R2.id.product_keyword)
    AppCompatEditText mProductKeyword = null;

    @OnClick(R2.id.product_keyword_search)
    void onClickProductSearch() {

    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_sort;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final VerticalListDelegate verticalListDelegate = new VerticalListDelegate();
        getSupportDelegate().loadRootFragment(R.id.vertical_list_container, verticalListDelegate);
        getSupportDelegate().loadRootFragment(R.id.sort_content_container, ContentDelegate.newInstance(1));
    }
}
