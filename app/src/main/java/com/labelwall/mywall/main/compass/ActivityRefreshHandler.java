package com.labelwall.mywall.main.compass;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.refresh.PagingBean;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.List;

/**
 * Created by Administrator on 2018-02-06.
 */

public class ActivityRefreshHandler implements SwipeRefreshLayout.OnRefreshListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final RecyclerView RECYCLER_VIEW;
    private final ActivityDataConverter CONVERTER;
    private final PagingBean BEAN;
    private final WallDelegate DELEGATE;
    private ActivityAdapter mAdapter = null;
    private static final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    private ActivityRefreshHandler(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView,
                                   ActivityDataConverter converter, PagingBean bean, WallDelegate delegate) {
        this.REFRESH_LAYOUT = refreshLayout;
        this.RECYCLER_VIEW = recyclerView;
        this.CONVERTER = converter;
        this.BEAN = bean;
        this.DELEGATE = delegate;
        REFRESH_LAYOUT.setOnRefreshListener(this);
    }

    public static ActivityRefreshHandler create(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView,
                                                ActivityDataConverter converter, WallDelegate delegate) {
        return new ActivityRefreshHandler(refreshLayout, recyclerView, converter, new PagingBean(), delegate);
    }

    @Override
    public void onRefresh() {

    }

    public void firshActivityPage() {
        //加载首页数据
        RestClient.builder()
                .url("activity/query/1/10")
                .params("userId", USER_ID)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final List<MultipleItemEntity> data = CONVERTER.setJsonData(response).convert();
                        mAdapter = new ActivityAdapter(data, DELEGATE);
                        RECYCLER_VIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .build()
                .post();
    }
}
