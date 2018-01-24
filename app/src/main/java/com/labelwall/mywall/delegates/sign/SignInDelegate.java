package com.labelwall.mywall.delegates.sign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.util.log.WallLogger;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-01-05.
 * 登录
 */

public class SignInDelegate extends WallDelegate {

    @BindView(R2.id.edit_sign_in_name)
    TextInputEditText mTextName = null;
    @BindView(R2.id.edit_sign_in_password)
    TextInputEditText mTextPassword = null;
    @BindView(R2.id.btn_sign_in)
    AppCompatButton mSignInBtn;
    @BindView(R2.id.tc_link_sign_up)
    AppCompatTextView mSingUpLink;

    private ISignListener mISignListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ISignListener){
            mISignListener = (ISignListener) activity;
        }
    }

    @OnClick(R2.id.btn_sign_in)
    void onClickSignIn() {
        if (checkForm()) {
            RestClient.builder()
                    .url("user/login")
                    .params("username", mTextName.getText().toString())
                    .params("password", mTextPassword.getText().toString())
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            WallLogger.json("User_profire", response);
                            Toast.makeText(getContext(), "返回结果：" + response, Toast.LENGTH_SHORT).show();
                            SignHelper.onSignIn(response,mISignListener);
                        }
                    })
                    .build()
                    .post();
        }
    }

    @OnClick(R2.id.tc_link_sign_up)
    void onClickSignUpLink() {
        getSupportDelegate().start(new SignUpDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_in;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    private boolean checkForm() {
        final String name = mTextName.getText().toString();
        final String password = mTextPassword.getText().toString();

        boolean isCheckPass = true;

        if (name.isEmpty()) {
            mTextName.setError("请输入用户名或邮箱地址");
            isCheckPass = false;
        } else {
            mTextName.setError(null);
        }
        if (password.isEmpty() || password.length() < 6) {
            mTextPassword.setError("请填写密码至少6位");
            isCheckPass = false;
        } else {
            mTextPassword.setError(null);
        }
        return isCheckPass;
    }
}
