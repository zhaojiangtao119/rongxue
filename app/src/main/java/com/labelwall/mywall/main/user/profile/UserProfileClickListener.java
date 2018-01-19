package com.labelwall.mywall.main.user.profile;

import android.content.DialogInterface;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.google.gson.Gson;
import com.labelwall.mywall.R;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.main.user.profile.location.JsonBean;
import com.labelwall.mywall.main.user.profile.location.LocationJsonReader;
import com.labelwall.mywall.ui.date.DateDialogUtil;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018-01-17.
 */

public class UserProfileClickListener extends SimpleClickListener {

    private final WallDelegate DELEGATE;
    private String[] mGenders = new String[]{"男", "女", "保密"};
    private long mUserId = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    private final ArrayList<JsonBean> OPTION1;
    private final ArrayList<ArrayList<String>> OPTION2;
    private final ArrayList<ArrayList<ArrayList<String>>> OPTION3;

    public UserProfileClickListener(WallDelegate delegate, ArrayList<JsonBean> option1,
                                    ArrayList<ArrayList<String>> option2,
                                    ArrayList<ArrayList<ArrayList<String>>> option3) {
        this.DELEGATE = delegate;
        this.OPTION1 = option1;
        this.OPTION2 = option2;
        this.OPTION3 = option3;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, final View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);
        int id = bean.getId();
        final TextView tv = (TextView) view.findViewById(R.id.tv_arrow_value);
        switch (id) {
            /*case 1:
                break;*/
            case UserProfileSettingItem.GENDER:
                getGenderDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv.setText(mGenders[which]);
                        dialog.cancel();
                        //修改greendDao数据库信息

                    }
                });
                break;
            case UserProfileSettingItem.BIRTHDAY:
                final DateDialogUtil dateDialogUtil = new DateDialogUtil();
                dateDialogUtil.setDataListener(new DateDialogUtil.IDateListener() {
                    @Override
                    public void onDateChange(String date) {
                        tv.setText(date);
                        //修改greendDao数据库信息
                        updateGreenDaoUserProfile("BIRTHDAY", date);
                    }
                });
                dateDialogUtil.showDialog(DELEGATE.getContext());
                break;
            case UserProfileSettingItem.PHONE:
                setUserProfile(tv, "phone");
                break;
            case UserProfileSettingItem.EMAIL:
                setUserProfile(tv, "email");
                break;
            case UserProfileSettingItem.LOCATION:
                showPickerView(tv);
                break;
            case 7:
                DELEGATE.getSupportDelegate().start(bean.getDelegate());
                break;
            default:
                break;
        }
    }

    private void getGenderDialog(DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DELEGATE.getContext());
        builder.setSingleChoiceItems(mGenders, 0, listener);
        builder.show();
    }

    private void setUserProfile(final TextView view, final String type) {
        final UserProfileNomalDialog dialog = new UserProfileNomalDialog(DELEGATE);
        dialog.setSubmitUserProfileListener(new UserProfileNomalDialog.ISubmitUserProfileListener() {
            @Override
            public void onSubmitProfile(String content) {
                view.setText(content);
                //修改greendDao数据库信息
                if (type.equals("phone")) {
                    updateGreenDaoUserProfile("PHONE", content);
                } else if (type.equals("email")) {
                    updateGreenDaoUserProfile("EMAIL", content);
                } else {
                    throw new RuntimeException("User type error");
                }
            }
        });
        dialog.beginProfileDialog();
    }

    private void showPickerView(final TextView view) {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(DELEGATE.getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //确定选择的回调监听
                //返回的分别是三个级别的选中位置
                String location;
                if (OPTION1.get(options1).getPickerViewText().equals(OPTION2.get(options1).get(options2))) {
                    location = OPTION1.get(options1).getPickerViewText() + "-" +
                            OPTION3.get(options1).get(options2).get(options3);
                } else {
                    location = OPTION1.get(options1).getPickerViewText() + "-" +
                            OPTION2.get(options1).get(options2) + "-" +
                            OPTION3.get(options1).get(options2).get(options3);
                }
                view.setText(location);
                //TODO 修改Greendao数据库
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();
        pvOptions.setPicker(OPTION1, OPTION2, OPTION3);
        pvOptions.show();
    }

    private void updateGreenDaoUserProfile(String feild, String userInfo) {
        if (mUserId != 0 && feild != null &&
                !feild.equals("") && userInfo != null &&
                !userInfo.equals("")) {
            String tableName = DataBaseManager.getInstance().getDao().getTablename();
            String sql = "UPDATE " + tableName + " SET" + " " + feild + "= '" + userInfo + "' WHERE _id =" + mUserId;
            //String sql = "UPDATE " + tableName + " SET " + feild + "=? WHERE _id=?";
            DataBaseManager
                    .getInstance()
                    .getDaoSession()
                    .getDatabase()
                    .execSQL(sql);
            //.rawQuery(sql, new String[]{userInfo, String.valueOf(mUserId)});
        }
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
