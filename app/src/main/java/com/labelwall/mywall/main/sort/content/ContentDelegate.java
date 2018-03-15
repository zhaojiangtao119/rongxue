package com.labelwall.mywall.main.sort.content;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.ui.recycler.BaseDecoration;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-01-19.
 */

public class ContentDelegate extends WallDelegate {

    @BindView(R2.id.rv_cintent_list)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.srl_content_product)
    SwipeRefreshLayout mRefreshLayout = null;

    private static final String ARG_CONTENT_ID = "CONTENT_ID";
    private static final String ARG_PRODUCT_KEYWORD = "KEYWORD";
    private int mContentId = -1;
    private String mKeyword = null;

    private ContentAdapter mAdapter = null;
    private ContentDataConverter mConverter = new ContentDataConverter();
    private ContentRefreshHandler mRefreshHandler = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mContentId = args.getInt(ARG_CONTENT_ID);
            mKeyword = args.getString(ARG_PRODUCT_KEYWORD);
        }
    }

    //存储信息CategoryId与keyword在Bundle中
    public static ContentDelegate newInstance(int countId, String keyword) {
        final Bundle args = new Bundle();//使用bundle存储contentId
        args.putInt(ARG_CONTENT_ID, countId);
        args.putString(ARG_PRODUCT_KEYWORD, keyword);
        final ContentDelegate contentDelegate = new ContentDelegate();
        contentDelegate.setArguments(args);//设置contentId
        return contentDelegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sort_content;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        final WallDelegate wallDelegate = getParentDelegate();
        mRefreshHandler = ContentRefreshHandler
                .create(wallDelegate, mRefreshLayout, mRecyclerView, new ContentDataConverter(), mContentId, mKeyword);
        initRefreshLayout();
        initRecyclerView();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mRefreshHandler.uploadContentProductFirst();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshLayout.setProgressViewOffset(true, 80, 100);
    }

    private void initRecyclerView() {
        //定义瀑布流，垂直方向吗，每行2个
        final StaggeredGridLayoutManager manager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        //设置分割线
    }
}
