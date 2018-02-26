package com.labelwall.mywall.util.net.callback;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.labelwall.mywall.app.ConfigKeys;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.ui.loader.LoaderStyle;
import com.labelwall.mywall.ui.loader.WallLoader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018-01-03.
 * call执行请求的callback实现类
 */

public class RequestCallbacks implements Callback<String> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IError ERROR;
    private final IFailure FAILURE;
    private final LoaderStyle LOADER_STYLE;
    //一般使用handler时，则声明为static类型，避免内存泄漏
    private static final Handler HANDLER = new Handler();

    private final SwipeRefreshLayout REFRESH_LAYOUT;


    public RequestCallbacks(IRequest request, ISuccess success,
                            IError error, IFailure failure, LoaderStyle loaderStyle, SwipeRefreshLayout refreshLayout) {
        this.REQUEST = request;
        this.SUCCESS = success;
        this.ERROR = error;
        this.FAILURE = failure;
        this.LOADER_STYLE = loaderStyle;
        this.REFRESH_LAYOUT = refreshLayout;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
            if (call.isExecuted()) {
                if (SUCCESS != null) {
                    SUCCESS.onSuccess(response.body());
                }
            }
        } else {
            if (ERROR != null) {
                ERROR.onError(response.code(), response.message());
            }
        }
        stopLoading();
        if (REFRESH_LAYOUT != null) {
            REFRESH_LAYOUT.setRefreshing(false);//停止加载
        }
        //onRequestFinish();
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        if (FAILURE != null) {
            FAILURE.onFailure();
        }
        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }
        stopLoading();
    }

    private void stopLoading() {
        //取消loader的显示,取消dialog
        if (LOADER_STYLE != null) {
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    WallLoader.stopLoading();
                }
            }, 1000);
        }
    }

    /*private void onRequestFinish() {
        final long delayed = MyWall.getConfiguration(ConfigKeys.LOADER_DELAYED);
        if (LOADER_STYLE != null) {
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LatteLoader.stopLoading();
                }
            }, delayed);
        }
    }*/
}
