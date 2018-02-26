package com.labelwall.mywall.util.net;

import com.labelwall.mywall.app.ConfigKeys;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.util.callback.CallbackType;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2018-02-26.
 */

public class Test {

    //OkHttpClient对象
    private static final class OkHttpHolder {
        private static final int TIME_OUT = 60;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static final ArrayList<Interceptor> INTERCEPTORS =
                MyWall.getConfiguration(ConfigKeys.INTERCEPTOR);

        private static OkHttpClient.Builder addInterceptor() {
            if (INTERCEPTORS != null && !INTERCEPTORS.isEmpty()) {
                for (Interceptor interceptor : INTERCEPTORS) {
                    BUILDER.addInterceptor(interceptor);
                }
            }
            return BUILDER;
        }

        private static final OkHttpClient OK_HTTP_CLIENT =
                addInterceptor().connectTimeout(TIME_OUT, TimeUnit.SECONDS).build();

    }

    //Retorfit对象
    private static final class RetrofitHolder {
        private static final String BASE_URL = "";
        private static final Retrofit RETROFIT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    //service对象
    private static final class RestServiceHolder {
        private static final RestService SERVICE =
                RetrofitHolder.RETROFIT.create(RestService.class);
    }

    //获取Service对象
    public static RestService getRestService() {
        return RestServiceHolder.SERVICE;
    }
}
