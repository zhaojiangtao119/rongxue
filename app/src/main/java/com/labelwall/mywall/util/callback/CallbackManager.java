package com.labelwall.mywall.util.callback;

import java.util.WeakHashMap;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018-01-13.
 * 全局的回调管理
 */

public class CallbackManager {

    private final WeakHashMap<Object, IGlobalCallback> CALLBACK = new WeakHashMap<>();

    public static class Holder {
        private static final CallbackManager INSTANCE = new CallbackManager();
    }

    public static CallbackManager getInstance() {
        return Holder.INSTANCE;
    }

    public CallbackManager addCallback(CallbackType tag, IGlobalCallback callback) {
        CALLBACK.put(tag, callback);
        return this;
    }

    public IGlobalCallback getCallback(CallbackType tag) {
        return CALLBACK.get(tag);
    }
}
