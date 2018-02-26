package com.labelwall.mywall.util.net.rx;

import android.content.Context;

import com.labelwall.mywall.ui.loader.LoaderStyle;
import com.labelwall.mywall.ui.loader.WallLoader;
import com.labelwall.mywall.util.net.HttpMethod;
import com.labelwall.mywall.util.net.RestCreator;
import com.labelwall.mywall.util.net.RestService;
import com.labelwall.mywall.util.net.callback.IError;
import com.labelwall.mywall.util.net.callback.IFailure;
import com.labelwall.mywall.util.net.callback.IRequest;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.net.callback.RequestCallbacks;

import java.io.File;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Administrator on 2018-02-26.
 */

public class RxRestClient {

    private final String URL;
    private final WeakHashMap<String, Object> PARAMS;
    private final ResponseBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final Context CONTEXT;

    public RxRestClient(String url, WeakHashMap<String, Object> params,
                        ResponseBody body, LoaderStyle loaderStyle,
                        Context context) {
        this.URL = url;
        this.PARAMS = params;
        this.BODY = body;
        this.LOADER_STYLE = loaderStyle;
        this.CONTEXT = context;
    }

    public static RxRestClientBuilder builder() {
        return new RxRestClientBuilder();
    }


    private Observable<String> request(HttpMethod method) {
        //获取Service对象
        final RxRestService rxRestService = RestCreator.getRxRestService();
        Observable<String> observable = null;

        //加载Loader
        if (LOADER_STYLE != null) {
            WallLoader.showLoading(CONTEXT, LOADER_STYLE);
        }
        switch (method) {
            case GET:
                observable = rxRestService.get(URL, PARAMS);
                break;
            case POST:
                observable = rxRestService.post(URL, PARAMS);
                break;

            case PUT:
                observable = rxRestService.put(URL, PARAMS);
                break;
            case DELETE:
                observable = rxRestService.delete(URL, PARAMS);
                break;
            default:
                break;
        }
        return observable;
    }

    public final Observable<String> get() {
        return request(HttpMethod.GET);
    }

    public final Observable<String> post() {
        if (BODY == null) {
            return request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {//使用原始数据的post，其PARAMS要为null
                throw new RuntimeException("params must be null");
            }
            return request(HttpMethod.POST_RAW);
        }
    }

    public final Observable<String> put() {
        if (BODY == null) {
            return request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {//使用原始数据PARAMS要为null
                throw new RuntimeException("params must be null");
            }
            return request(HttpMethod.PUT_RAW);
        }
    }

    public final Observable<String> delete() {
        return request(HttpMethod.DELETE);
    }

}
