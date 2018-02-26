package com.labelwall.mywall.util.net.rx;

import android.content.Context;

import com.labelwall.mywall.ui.loader.LoaderStyle;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.RestClientBuilder;
import com.labelwall.mywall.util.net.callback.IError;
import com.labelwall.mywall.util.net.callback.IFailure;
import com.labelwall.mywall.util.net.callback.IRequest;
import com.labelwall.mywall.util.net.callback.ISuccess;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018-02-26.
 */

public class RxRestClientBuilder {

    private String mUrl;
    private final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    private ResponseBody mBody;
    private LoaderStyle mLoaderStyle;
    private Context mContext;

    RxRestClientBuilder() {

    }

    public final RxRestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RxRestClientBuilder loader(Context context, LoaderStyle style) {
        this.mContext = context;
        this.mLoaderStyle = style;
        return this;
    }

    public final RxRestClientBuilder loader(Context context) {
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }

    public final RxRestClientBuilder params(Map<String, Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final RxRestClientBuilder params(String key, Object value) {
        this.PARAMS.put(key, value);
        return this;
    }

    public final RxRestClient build() {
        return new RxRestClient(mUrl, PARAMS, mBody,
                mLoaderStyle, mContext);
    }
}
