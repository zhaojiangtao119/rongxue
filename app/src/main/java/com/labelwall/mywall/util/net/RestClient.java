package com.labelwall.mywall.util.net;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.labelwall.mywall.ui.loader.LoaderStyle;
import com.labelwall.mywall.ui.loader.WallLoader;
import com.labelwall.mywall.util.net.callback.IError;
import com.labelwall.mywall.util.net.callback.IFailure;
import com.labelwall.mywall.util.net.callback.IRequest;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.net.callback.RequestCallbacks;
import com.labelwall.mywall.util.net.download.DownloadHandler;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Administrator on 2018-01-03.
 * 进行网络请求的实现类
 */

public class RestClient {

    private final String URL;
    private final WeakHashMap<String, Object> PARAMS;
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IError ERROR;
    private final IFailure FAILURE;
    private final RequestBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final Context CONTEXT;
    private final File FILE;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final SwipeRefreshLayout REFRESH_LAYOUT;

    public RestClient(String url, WeakHashMap<String, Object> params,
                      IRequest request, ISuccess success,
                      IError error, IFailure failure,
                      RequestBody body, LoaderStyle loaderStyle,
                      Context context, File file,
                      String dounloadDir, String extension, String name,
                      SwipeRefreshLayout refreshLayout) {
        this.URL = url;
        this.PARAMS = params;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.ERROR = error;
        this.FAILURE = failure;
        this.BODY = body;
        this.LOADER_STYLE = loaderStyle;
        this.CONTEXT = context;
        this.FILE = file;
        this.DOWNLOAD_DIR = dounloadDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.REFRESH_LAYOUT = refreshLayout;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private void request(HttpMethod method) {
        //获取Service对象
        final RestService restService = RestCreator.getRestService();
        Call<String> call = null;
        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }
        //加载Loader
        if (LOADER_STYLE != null) {
            WallLoader.showLoading(CONTEXT, LOADER_STYLE);
        }
        switch (method) {
            case GET:
                call = restService.get(URL, PARAMS);
                break;
            case POST:
                call = restService.post(URL, PARAMS);
                break;
            case POST_RAW:
                call = restService.postRaw(URL, BODY);
            case PUT:
                call = restService.put(URL, PARAMS);
                break;
            case PUT_RAW:
                call = restService.postRaw(URL, BODY);
                break;
            case DELETE:
                call = restService.delete(URL, PARAMS);
                break;
            case UPLOAD:
                //上传文件
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                call = restService.upload(URL, body);
                break;
            default:
                break;
        }
        if (call != null) {
            call.enqueue(getRequestCallback());
        }
    }

    private Callback<String> getRequestCallback() {
        return new RequestCallbacks(REQUEST, SUCCESS, ERROR, FAILURE, LOADER_STYLE, REFRESH_LAYOUT);
    }

    public final void get() {
        request(HttpMethod.GET);
    }

    public final void post() {
        if (BODY == null) {
            request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {//使用原始数据的post，其PARAMS要为null
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    public final void put() {
        if (BODY == null) {
            request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {//使用原始数据PARAMS要为null
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    public final void delete() {
        request(HttpMethod.DELETE);
    }

    public final void upload() {
        request(HttpMethod.UPLOAD);
    }

    public final void download() {
        new DownloadHandler(URL, PARAMS, REQUEST, SUCCESS, ERROR, FAILURE, DOWNLOAD_DIR, EXTENSION, NAME)
                .handleDownload();
    }
}
