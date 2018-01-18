package com.labelwall.mywall.util.net.interceptor;

import java.util.LinkedHashMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by Administrator on 2018-01-04.
 * 网络请求拦截器
 */

public abstract class BaseInterceptor implements Interceptor {

    /**
     * 获取url中的请求参数
     *
     * @param chain
     * @return
     */
    protected LinkedHashMap<String, String> getUrlParameters(Chain chain) {
        final HttpUrl url = chain.request().url();
        int size = url.querySize();//获取请求参数的个数
        final LinkedHashMap<String, String> params = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) {
            params.put(url.queryParameterName(i), url.queryParameterValue(i));
        }
        return params;
    }

    protected String getUrlParameters(Chain chain, String key) {
        final Request request = chain.request();
        return request.url().queryParameter(key);
    }

    /**
     * 获取请求体中的请求参数
     *
     * @param chain
     * @return
     */
    protected LinkedHashMap<String, String> getBodyParameters(Chain chain) {
        final FormBody formBody = (FormBody) chain.request().body();
        final LinkedHashMap<String, String> params = new LinkedHashMap<>();
        int size = formBody.size();
        for (int i = 0; i < size; i++) {
            params.put(formBody.name(i), formBody.value(i));
        }
        return params;
    }

    protected String getBodyParamsters(Chain chain, String key) {
        return getBodyParameters(chain).get(key);
    }
}
