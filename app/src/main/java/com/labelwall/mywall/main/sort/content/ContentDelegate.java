package com.labelwall.mywall.main.sort.content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import butterknife.BindView;

/**
 * Created by Administrator on 2018-01-19.
 */

public class ContentDelegate extends WallDelegate {

    @BindView(R2.id.rv_cintent_list)
    RecyclerView mRecyclerView = null;

    private static final String ARG_CONTENT_ID = "CONTENT_ID";
    private int mContentId = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mContentId = args.getInt(ARG_CONTENT_ID);
        }
    }

    public static ContentDelegate newInstance(int countId) {
        final Bundle args = new Bundle();//使用bundle存储contentId
        args.putInt(ARG_CONTENT_ID, countId);
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
        //定义瀑布流，垂直方向吗，每行2个
        final StaggeredGridLayoutManager manager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
    }

    private void initData() {
        RestClient.builder()
                .url("")
                .params("", "")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                    }
                })
                .build()
                .post();
    }
}
