package com.labelwall.mywall.util.net;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.labelwall.mywall.ui.loader.LoaderStyle;
import com.labelwall.mywall.util.net.callback.*;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018-01-03.
 * RestClient的建造者类，build
 */

public class RestClientBuilder {

    private String mUrl = null;
    private static final Map<String, Object> mParams = RestCreator.getParams();
    private IRequest mIRequest = null;
    private ISuccess mISuccess = null;
    private IError mIError = null;
    private IFailure mIFailure = null;
    private RequestBody mBody = null;
    private LoaderStyle mLoaderStyle = null;
    private Context mContext = null;
    private File mFile = null;
    private String mDownloadDir;
    private String mExtension;
    private String mName;
    private SwipeRefreshLayout mRefreshLayout;

    RestClientBuilder() {

    }

    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key, Object value) {
        this.mParams.put(key, value);
        return this;
    }

    public final RestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder request(IRequest iRequest) {
        this.mIRequest = iRequest;
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure) {
        this.mIFailure = iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }

    public final RestClientBuilder loader(Context context, LoaderStyle style) {
        this.mContext = context;
        this.mLoaderStyle = style;
        return this;
    }

    public final RestClientBuilder loader(Context context) {
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }

    public final RestClientBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder file(String filePath) {
        this.mFile = new File(filePath);
        return this;
    }

    public final RestClientBuilder downloadDir(String downloadDir) {
        this.mDownloadDir = downloadDir;
        return this;
    }

    public final RestClientBuilder extension(String extension) {
        this.mExtension = extension;
        return this;
    }

    public final RestClientBuilder name(String name) {
        this.mName = name;
        return this;
    }

    public final RestClientBuilder refreshLayout(SwipeRefreshLayout refreshLayout){
        this.mRefreshLayout = refreshLayout;
        return this;
    }
    /*private Map<String, Object> checkParams() {
        if (mParams == null) {
            return new WeakHashMap<>();
        }
        return mParams;
    }*/

    public final RestClient build() {
        return new RestClient(mUrl, mParams, mIRequest,
                mISuccess, mIError, mIFailure, mBody,
                mLoaderStyle, mContext, mFile,
                mDownloadDir, mExtension, mName,mRefreshLayout);
    }

}
