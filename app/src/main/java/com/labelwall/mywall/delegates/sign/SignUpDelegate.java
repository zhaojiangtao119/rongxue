package com.labelwall.mywall.delegates.sign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Patterns;
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
 * Created by Administrator on 2018-01-04.
 * 注册的delegate
 */

public class SignUpDelegate extends WallDelegate {

    @BindView(R2.id.edit_sign_up_name)
    TextInputEditText mTextName = null;
    @BindView(R2.id.edit_sign_up_email)
    TextInputEditText mTextEmail = null;
    @BindView(R2.id.edit_sign_up_password)
    TextInputEditText mTextPassword = null;
    @BindView(R2.id.edit_sign_up_re_password)
    TextInputEditText mTextRePassword = null;
    @BindView(R2.id.btn_sign_up)
    AppCompatButton mSignUpBtn;
    @BindView(R2.id.tc_link_sign_in)
    AppCompatTextView mSingInLink;

    private ISignListener mISignListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ISignListener){
            mISignListener = (ISignListener) activity;
        }
    }

    @OnClick(R2.id.btn_sign_up)
    void onClickSignUp() {
        if (checkForm()) {
            RestClient.builder()
                    .url("user/register")
                    .params("username", mTextName.getText().toString())
                    .params("email", mTextEmail.getText().toString())
                    .params("password", mTextPassword.getText().toString())
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            WallLogger.json("User_Profile", response);
                            Toast.makeText(getContext(), "注册返回结果：" + response, Toast.LENGTH_SHORT).show();
                            SignHelper.onSignUp(response,mISignListener);
                        }
                    })
                    .build()
                    .post();
        }
    }

    @OnClick(R2.id.tc_link_sign_in)
    void onClickSignInLink() {
        getSupportDelegate().start(new SignInDelegate());
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_up;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    private boolean checkForm() {
        final String name = mTextName.getText().toString();
        final String email = mTextEmail.getText().toString();
        final String password = mTextPassword.getText().toString();
        final String rePassword = mTextRePassword.getText().toString();

        boolean isCheckPass = true;

        if (name.isEmpty()) {
            mTextName.setError("请输入用户名");
            isCheckPass = false;
        } else {
            mTextName.setError(null);
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mTextEmail.setError("邮箱格式错误");
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
        if (rePassword.isEmpty() || password.length() < 6 || !rePassword.equals(password)) {
            mTextRePassword.setError("密码验证错误");
            isCheckPass = false;
        } else {
            mTextRePassword.setError(null);
        }
        return isCheckPass;
    }
}
