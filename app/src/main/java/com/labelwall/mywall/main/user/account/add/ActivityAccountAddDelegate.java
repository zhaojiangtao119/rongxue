package com.labelwall.mywall.main.user.account.add;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018-02-23.
 */

public class ActivityAccountAddDelegate extends WallDelegate implements AdapterView.OnItemClickListener {

    @BindView(R2.id.lv_jindou_count)
    ListView mListView = null;
   /* @BindView(R2.id.tv_account_add_price)
    TextView mTextViewPrice = null;*/

    @OnClick(R2.id.btn_submit_account_add)
    void onClickSubmitAdd() {
        if (mCountIndex != -1) {
            createAccountOrder(mCountIndex);
        } else {
            Toast.makeText(_mActivity, "请选择充值的数额", Toast.LENGTH_SHORT).show();
        }
    }

    private final Integer ACCOUNT_ID;
    private final long USER_ID =
            WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private List<String> mCount = new ArrayList<>();
    private ActivityAccountJindouAdapter mAdapter = null;
    private int mCountIndex = -1;


    public ActivityAccountAddDelegate(Integer accountId) {
        this.ACCOUNT_ID = accountId;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_activity_account_add;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initListView();
    }

    private void initListView() {
        mCount.add("50(￥5.0)");
        mCount.add("100(￥10.0)");
        mCount.add("200(￥20.0)");
        mCount.add("500(￥50.0)");
        mCount.add("1000(￥100.0)");
        mAdapter = new ActivityAccountJindouAdapter(this, mCount);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mAdapter.setCheckedListener(new ActivityAccountJindouAdapter.CheckedListener() {
            @Override
            public void selected(int postion) {
                Log.e("选中的是几号：", String.valueOf(postion));
                //数据库中生成订单
                mCountIndex = postion;
            }
        });
    }

    private void createAccountOrder(int postion) {
        String selected = mCount.get(postion);
        if (!StringUtils.isEmpty(selected)) {
            //获取金豆数量与价格
            String countStr = selected.substring(0, selected.indexOf("("));
            Integer count = Integer.valueOf(countStr);
            double price = count / 10;
            RestClient.builder()
                    .url("activity/account/trade/add")
                    .params("userId", USER_ID)
                    .params("accountId", ACCOUNT_ID)
                    .params("orderPrice", price)
                    .params("jindouCount", count)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            final JSONObject jsonResponse = JSON.parseObject(response);
                            final JSONObject data = jsonResponse.getJSONObject("data");
                            final int status = jsonResponse.getInteger("status");
                            final String message = jsonResponse.getString("msg");
                            if (status == 0) {
                                //插入成功，跳转到订单详情页面，进行支付，或不支付
                                getSupportDelegate().startWithPop(
                                        new ActivityAccountAddOrderDetailDelegate(data));
                            } else {
                                Toast.makeText(_mActivity, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .build()
                    .post();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
        ActivityAccountJindouAdapter.ViewHolder viewHolder =
                (ActivityAccountJindouAdapter.ViewHolder) view.getTag();
        // 把CheckBox的选中状态改为当前状态的反,gridview确保是单一选中
        viewHolder.cbChecked.setChecked(true);
    }
}
