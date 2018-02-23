package com.labelwall.mywall.main.user.account.add;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.labelwall.mywall.R;
import com.labelwall.mywall.app.AccountManager;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.util.log.WallLogger;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.pay.FastPay;
import com.labelwall.mywall.util.pay.IAIPayResultListener;
import com.labelwall.mywall.util.pay.PayAsyncTask;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

/**
 * Created by Administrator on 2018-02-23.
 */

public class AccountAddPay implements View.OnClickListener {

    private IAIPayResultListener mIAIPayResultListener = null;
    private Activity mActivity = null;

    private AlertDialog mDialog = null;
    private Long mOrderNo = null;
    private final long USER_ID =
            WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    private AccountAddPay(WallDelegate delegate) {
        this.mActivity = delegate.getProxyActivity();
        this.mDialog = new AlertDialog.Builder(delegate.getContext()).create();
    }

    public static AccountAddPay create(WallDelegate delegate) {
        return new AccountAddPay(delegate);
    }

    public AccountAddPay setPayResultListener(IAIPayResultListener listener) {
        this.mIAIPayResultListener = listener;
        return this;
    }

    public AccountAddPay setOrderNo(Long orderNo) {
        this.mOrderNo = orderNo;
        return this;
    }

    public void beginPayDialog() {
        mDialog.show();
        final Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_pay_panel);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);

            window.findViewById(R.id.btn_dialog_pay_alpay).setOnClickListener(this);
            window.findViewById(R.id.btn_dialog_pay_wechat).setOnClickListener(this);
            window.findViewById(R.id.btn_dialog_pay_cancel).setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dialog_pay_alpay) {
            accountPay(mOrderNo);
            mDialog.cancel();
        } else if (id == R.id.btn_dialog_pay_wechat) {
            mDialog.cancel();
        } else if (id == R.id.btn_dialog_pay_cancel) {
            mDialog.cancel();
        }
    }

    public final void accountPay(Long orderId) {
        //通过订单号+userId，获取订单签名字符串
        RestClient.builder()
                .url("activity/account/trade/sign")
                .params("orderNo", mOrderNo)
                .params("userId", USER_ID)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //服务端返回的订单签名的字符串
                        final String paySign = JSON.parseObject(response).getString("data");
                        //必须是异步的调用客户端支付接口
                        final PayAsyncTask payAsyncTask = new PayAsyncTask(mActivity, mIAIPayResultListener);
                        payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paySign);
                    }
                })
                .build()
                .post();
    }
}
