package com.labelwall.mywall.util.callback;

/**
 * Created by Administrator on 2018-01-13.
 * 泛型接口
 */

public interface IGlobalCallback<T> {

    void executeCallback(T args);

}
