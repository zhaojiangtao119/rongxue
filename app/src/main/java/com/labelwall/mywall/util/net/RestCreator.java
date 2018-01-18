package com.labelwall.mywall.util.net;

import com.labelwall.mywall.app.ConfigKeys;
import com.labelwall.mywall.app.MyWall;

import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2018-01-03.
 */

public class RestCreator {

    /**
     * 构建请求参数的Map集合
     */
    private static final class ParamsHolder {
        public static final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    }

    public static WeakHashMap<String, Object> getParams() {
        return ParamsHolder.PARAMS;
    }

    /**
     * 构建全局的Retrofit客户端(构建retrofit对象)
     */
    private static final class RetrofitHolder {
        //TODO applicaiton中初始化base_url
        private static final String BASE_URL = MyWall.getConfiguration(ConfigKeys.API_HOST);
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())//转换器
                .build();
    }

    /**
     * 构建OkHttpClient对象，可以增加interceptor
     */
    private static final class OKHttpHolder {
        private static final int TIME_OUT = 60;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static final ArrayList<Interceptor> INTERCEPTORS = MyWall.getConfiguration(ConfigKeys.INTERCEPTOR);
        private static OkHttpClient.Builder addInterceptor(){
            if(INTERCEPTORS != null && !INTERCEPTORS.isEmpty()){
                for(Interceptor interceptor : INTERCEPTORS){
                    BUILDER.addInterceptor(interceptor);
                }
            }
            return BUILDER;
        }
        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 构建RestService对象
     */
    private static final class RestServiceHolder {
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }

    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }
}
