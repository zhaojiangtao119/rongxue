package com.labelwall.mywall.main.compass;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.refresh.PagingBean;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;
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
        uploadMoreActivity();
    }


    public void firshActivityPage() {
        //加载首页数据
        RestClient.builder()
                .url("activity/query/" + BEAN.getPageIndex() + "/10")
                .params("userId", USER_ID)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final List<MultipleItemEntity> data = CONVERTER.setJsonData(response).convert();
                        final JSONObject pageInfo = JSON.parseObject(response).getJSONObject("data");
                        //设置分页的总记录数，分页标准
                        BEAN.setTotal(pageInfo.getInteger("total"))
                                .setPageSize(pageInfo.getInteger("pageSize"))
                                .setCurrentCount(pageInfo.getInteger("size"));
                        mAdapter = new ActivityAdapter(data, DELEGATE);
                        RECYCLER_VIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .build()
                .post();
    }

    private void uploadMoreActivity() {
        final int pageSize = BEAN.getPageSize();
        final int currentCount = BEAN.getCurrentCount();
        final int total = BEAN.getTotal();
        final int index = BEAN.getPageIndex();
        if (mAdapter.getItemCount() < pageSize || currentCount >= total) {
            REFRESH_LAYOUT.setRefreshing(false);
        } else {
            RestClient.builder()
                    .url("activity/query/" + BEAN.getPageIndex() + "/10")
                    .refreshLayout(REFRESH_LAYOUT)
                    .params("userId", USER_ID)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            //TODO
                            final JSONObject pageInfo = JSON.parseObject(response).getJSONObject("data");
                            List<MultipleItemEntity> newData = converterData(response);
                            CONVERTER.clearData();
                            mAdapter.addData(newData);
                            BEAN.setPageSize(pageInfo.getInteger("pageSize"));
                            BEAN.setCurrentCount(mAdapter.getData().size());
                            BEAN.addIndex();
                        }
                    })
                    .build()
                    .post();
        }
    }

    private List<MultipleItemEntity> converterData(String response) {
        final JSONObject pageInfo = JSON.parseObject(response).getJSONObject("data");
        List<MultipleItemEntity> oldData = new ArrayList<>();
        oldData.addAll(CONVERTER.getItemList());
        CONVERTER.clearData();
        List<MultipleItemEntity> newData = new ArrayList<>();
        newData.addAll(CONVERTER.setJsonData(response).convert());
        //将下拉刷新的新数据添加到与adapter关联的List集合的最前边
        oldData.addAll(0, newData);
        return oldData;
    }
}
