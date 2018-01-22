package com.labelwall.mywall.main.sort.content;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.refresh.PagingBean;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-01-20.
 */

public class ContentRefreshHandler implements SwipeRefreshLayout.OnRefreshListener {

    private final WallDelegate DELEGATE;
    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final RecyclerView RECYCLER_VIEW;
    private ContentAdapter mAdapter = null;
    private final ContentDataConverter CONVERTER;
    private final PagingBean BEAN;
    private final int CONTENT_ID;
    private String KEYWORD;

    private ContentRefreshHandler(WallDelegate delegate, SwipeRefreshLayout refreshLayout, RecyclerView recyclerView,
                                  ContentDataConverter converter, PagingBean bean,
                                  int contentId, String keyword) {
        this.DELEGATE = delegate;
        this.REFRESH_LAYOUT = refreshLayout;
        this.RECYCLER_VIEW = recyclerView;
        this.CONVERTER = converter;
        this.BEAN = bean;
        this.CONTENT_ID = contentId;
        this.KEYWORD = keyword;
        REFRESH_LAYOUT.setOnRefreshListener(this);
    }

    public static ContentRefreshHandler create(WallDelegate delegate, SwipeRefreshLayout refreshLayout, RecyclerView recyclerView,
                                               ContentDataConverter converter, int contentId, String keyowrd) {
        return new ContentRefreshHandler(delegate, refreshLayout, recyclerView,
                converter, new PagingBean(), contentId, keyowrd);
    }


    @Override
    public void onRefresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        uploadContentProductMore();
    }

    public void uploadContentProductFirst() {
        if (KEYWORD == null) {
            KEYWORD = "";
        }
        RestClient.builder()
                .url("product/get_product_list/" + BEAN.getPageIndex() + "/6")
                .params("categoryId", CONTENT_ID)
                .params("keyword", KEYWORD)
                .refreshLayout(REFRESH_LAYOUT)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter = ContentAdapter.create(CONVERTER.setJsonData(response), DELEGATE);
                        RECYCLER_VIEW.setAdapter(mAdapter);
                        BEAN.addIndex();
                    }
                })
                .build()
                .post();
    }

    private void uploadContentProductMore() {
        RestClient.builder()
                .url("product/get_product_list/" + BEAN.getPageIndex() + "/6")
                .refreshLayout(REFRESH_LAYOUT)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject pageInfo = JSON.parseObject(response).getJSONObject("data");
                        List<MultipleItemEntity> newData = converterData(response);
                        CONVERTER.clearData();
                        mAdapter.addData(newData);
                        BEAN.setPageSize(pageInfo.getInteger("pageSize"));
                        BEAN.setCurrentCount(mAdapter.getData().size());
                        BEAN.addIndex();
                        Log.e("数量：", mAdapter.getItemCount() + "");
                    }
                })
                .build()
                .post();
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
