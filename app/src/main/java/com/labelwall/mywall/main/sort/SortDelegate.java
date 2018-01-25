package com.labelwall.mywall.main.sort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.bottom.BottomItemDelegate;
import com.labelwall.mywall.main.sort.content.ContentDelegate;
import com.labelwall.mywall.main.sort.list.VerticalListDelegate;
import com.labelwall.mywall.main.sort.search.SearchDelegate;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-01-04.
 */

public class SortDelegate extends BottomItemDelegate implements View.OnFocusChangeListener {

    @BindView(R2.id.product_keyword)
    AppCompatEditText mProductKeyword = null;

    private int mContentId = -1;
    private String mKeyword = null;

    @OnClick(R2.id.icon_sort_scan)
    void onClickScanQrCode() {//启动二维码扫描
        startScanWithCheck(this.getParentDelegate());
    }

    @OnClick(R2.id.product_keyword_search)
    void onClickProductSearch() {
        final CallbackManager manager = CallbackManager
                .getInstance()
                .addCallback(CallbackType.CONTENT_PRODUCT_ID, new IGlobalCallback<Integer>() {
                    @Override
                    public void executeCallback(Integer contentId) {
                        mContentId = contentId;
                    }
                });
        mKeyword = mProductKeyword.getText().toString();
        Log.e("关键字：", mKeyword);
        //加载数据搜索后的数据
        uploadSearchData();
    }

    private void uploadSearchData() {
        if (mContentId == -1) {
            mContentId = 0;
        }
        getSupportDelegate().loadRootFragment(R.id.sort_content_container,
                ContentDelegate.newInstance(mContentId, mKeyword));
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sort;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        //二维码扫描结果的回调
        CallbackManager.getInstance().addCallback(CallbackType.ON_SCAN, new IGlobalCallback<String>() {
            @Override
            public void executeCallback(String args) {
                Toast.makeText(_mActivity, "扫描结果：" + args, Toast.LENGTH_SHORT).show();
            }
        });
        //searchView的点击事件
        mProductKeyword.setOnFocusChangeListener(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final VerticalListDelegate verticalListDelegate = new VerticalListDelegate();
        getSupportDelegate().loadRootFragment(R.id.vertical_list_container, verticalListDelegate);
        getSupportDelegate().loadRootFragment(R.id.sort_content_container, ContentDelegate.newInstance(0, mKeyword));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        /*if (hasFocus) {//搜索框已经进入焦点模式
            //开启searchDelegate
            getParentDelegate().getSupportDelegate().start(new SearchDelegate());
        }*/
    }
}
