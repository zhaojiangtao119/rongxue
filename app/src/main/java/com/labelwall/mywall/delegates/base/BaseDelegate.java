package com.labelwall.mywall.delegates.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labelwall.mywall.activities.ProxyActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by Administrator on 2018-01-03.
 * 基础delegate:实现fragment与View视图的绑定，使用butterknife
 */

public abstract class BaseDelegate extends SwipeBackFragment {

    @SuppressWarnings("SpellCheckingInspection")
    private Unbinder mUnbinder = null;

    public abstract Object setLayout();//子类来设置布局 可以是view或是layout的id

    public abstract void onBindView(@Nullable Bundle savedInstanceState,
                                    View rootView);//进行View操作

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View rootView;
        if (setLayout() instanceof Integer) {
            rootView = inflater.inflate((Integer) setLayout(), container, false);
        } else if (setLayout() instanceof View) {
            rootView = (View) setLayout();
        } else {
            throw new ClassCastException("type of setLayout() must be int or View!");
        }
        //绑定视图资源 fragment与view之间
        mUnbinder = ButterKnife.bind(this, rootView);
        onBindView(savedInstanceState, rootView);
        return rootView;
    }

    public final ProxyActivity getProxyActivity() {
        return (ProxyActivity) _mActivity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();//解除绑定
        }
    }
}
