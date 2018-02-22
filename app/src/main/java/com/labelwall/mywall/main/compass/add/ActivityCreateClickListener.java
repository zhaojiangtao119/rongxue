package com.labelwall.mywall.main.compass.add;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.main.user.profile.location.JsonBean;
import com.labelwall.mywall.ui.widget.CustomDatePicker;
import com.labelwall.mywall.util.date.DateTimeUtil;
import com.labelwall.mywall.util.log.WallLogger;
import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.ISuccess;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2018-02-10.
 */

public class ActivityCreateClickListener extends SimpleClickListener {

    private CustomDatePicker mCustomDatePicker = null;
    private final WallDelegate DELEGATE;

    private String[] mTypeArray = null;
    private String[] mStyleArray = null;
    private String[] mActivityFree = new String[]{"免费", "收费"};
    private String[] mSchoolName = null;
    private String[] mAmount = new String[]{"5", "10", "20", "50", "100"};
    private String[] mUserNum = new String[]{"5", "10", "20", "50", "100"};

    private static Map<String, Object> mActivityInfo = new HashMap<>();

    private final ArrayList<JsonBean> OPTION1;
    private final ArrayList<ArrayList<String>> OPTION2;
    private final ArrayList<ArrayList<ArrayList<String>>> OPTION3;
    private String mProvinceName = null;
    private String mActivityLocation = null;
    private String mSelectDate = null;


    public ActivityCreateClickListener(WallDelegate delegate, ArrayList<JsonBean> option1,
                                       ArrayList<ArrayList<String>> option2,
                                       ArrayList<ArrayList<ArrayList<String>>> option3) {
        this.DELEGATE = delegate;
        this.OPTION1 = option1;
        this.OPTION2 = option2;
        this.OPTION3 = option3;
        initStyleData();
        initTypeData();
        mActivityInfo.clear();
    }

    public static ActivityCreateClickListener create(WallDelegate delegate, ArrayList<JsonBean> option1,
                                                     ArrayList<ArrayList<String>> option2,
                                                     ArrayList<ArrayList<ArrayList<String>>> option3) {
        return new ActivityCreateClickListener(delegate, option1, option2, option3);

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);//adapter中活动点击的item
        final int id = bean.getId();
        final TextView valueView = (TextView) view.findViewById(R.id.tv_arrow_value);
        switch (id) {
            case ActivityCreateInfoItem.APPLY_START_TIME:
                TimePickerView timePickerView1 = new TimePickerView.Builder(DELEGATE.getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        valueView.setText(DateTimeUtil.dateToStr(date));
                        if (mActivityInfo.get(ActivityCreateInfoItem.APPLY_START_TIME_PARAM) != null) {
                            mActivityInfo.remove(ActivityCreateInfoItem.APPLY_START_TIME_PARAM);
                        }
                        mActivityInfo.put(ActivityCreateInfoItem.APPLY_START_TIME_PARAM, DateTimeUtil.dateToStr(date));
                    }
                })
                        .setDate(Calendar.getInstance())
                        .isCyclic(false)
                        .setRange(2018, 2018)
                        .build();
                timePickerView1.show();
                break;
            case ActivityCreateInfoItem.APPLY_END_TIME:
                TimePickerView timePickerView2 = new TimePickerView.Builder(DELEGATE.getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        valueView.setText(DateTimeUtil.dateToStr(date));
                        if (mActivityInfo.get(ActivityCreateInfoItem.APPLY_END_TIME_PARAM) != null) {
                            mActivityInfo.remove(ActivityCreateInfoItem.APPLY_END_TIME_PARAM);
                        }
                        mActivityInfo.put(ActivityCreateInfoItem.APPLY_END_TIME_PARAM, DateTimeUtil.dateToStr(date));
                    }
                })
                        .setDate(Calendar.getInstance())
                        .isCyclic(false)
                        .setRange(2018, 2018)
                        .build();
                timePickerView2.show();
                break;
            case ActivityCreateInfoItem.ACTIVITY_START_TIME:
                TimePickerView timePickerView3 = new TimePickerView.Builder(DELEGATE.getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        valueView.setText(DateTimeUtil.dateToStr(date));
                        if (mActivityInfo.get(ActivityCreateInfoItem.ACTIVITY_START_TIME_PARAM) != null) {
                            mActivityInfo.remove(ActivityCreateInfoItem.ACTIVITY_START_TIME_PARAM);
                        }
                        mActivityInfo.put(ActivityCreateInfoItem.ACTIVITY_START_TIME_PARAM, DateTimeUtil.dateToStr(date));
                    }
                })
                        .setDate(Calendar.getInstance())
                        .isCyclic(false)
                        .setRange(2018, 2018)
                        .build();
                timePickerView3.show();
                break;
            case ActivityCreateInfoItem.ACTIVITY_END_TIME:
                TimePickerView timePickerView4 = new TimePickerView.Builder(DELEGATE.getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        valueView.setText(DateTimeUtil.dateToStr(date));
                        if (mActivityInfo.get(ActivityCreateInfoItem.ACTIVITY_END_TIME_PARAM) != null) {
                            mActivityInfo.remove(ActivityCreateInfoItem.ACTIVITY_END_TIME_PARAM);
                        }
                        mActivityInfo.put(ActivityCreateInfoItem.ACTIVITY_END_TIME_PARAM, DateTimeUtil.dateToStr(date));
                    }
                })
                        .setDate(Calendar.getInstance())
                        .isCyclic(false)
                        .setRange(2018, 2018)
                        .build();
                timePickerView4.show();
                break;
            case ActivityCreateInfoItem.ACTIVITY_FREE:
                getDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //选择的回调
                        valueView.setText(mActivityFree[which]);
                        dialog.cancel();
                        int free = 0;
                        if (mActivityFree[which].equals("收费")) {
                            free = 1;
                        } else {
                            free = 0;
                        }
                        if (mActivityInfo.get(ActivityCreateInfoItem.ACTIVITY_FREE_PARAM) != null) {
                            mActivityInfo.remove(ActivityCreateInfoItem.ACTIVITY_FREE_PARAM);
                        }
                        mActivityInfo.put(ActivityCreateInfoItem.ACTIVITY_FREE_PARAM, free);
                    }
                }, mActivityFree);
                break;
            case ActivityCreateInfoItem.ACTIVITY_STYLE:
                getDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //选择的回调
                        valueView.setText(mStyleArray[which]);
                        dialog.cancel();
                        if (mActivityInfo.get(ActivityCreateInfoItem.ACTIVITY_STYLE_PARAM) != null) {
                            mActivityInfo.remove(ActivityCreateInfoItem.ACTIVITY_STYLE_PARAM);
                        }
                        mActivityInfo.put(ActivityCreateInfoItem.ACTIVITY_STYLE_PARAM, mStyleArray[which]);
                    }
                }, mStyleArray);
                break;
            case ActivityCreateInfoItem.ACTIVITY_TYPE:
                getDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //选择的回调
                        valueView.setText(mTypeArray[which]);
                        dialog.cancel();
                        if (mActivityInfo.get(ActivityCreateInfoItem.ACTIVITY_TYPE_PARAM) != null) {
                            mActivityInfo.remove(ActivityCreateInfoItem.ACTIVITY_TYPE_PARAM);
                        }
                        mActivityInfo.put(ActivityCreateInfoItem.ACTIVITY_TYPE_PARAM, mTypeArray[which]);
                    }
                }, mTypeArray);
                break;
            case ActivityCreateInfoItem.ACTIVITY_LOCATION:
                showLocationPickerView(valueView);
                break;
            case ActivityCreateInfoItem.ACTIVITY_SCHOOL:
                if (mSchoolName == null) {
                    Toast.makeText(DELEGATE.getContext(), "请先选择位置，后选择学校", Toast.LENGTH_SHORT).show();
                } else {
                    getDialog(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            valueView.setText(mSchoolName[which]);
                            dialog.cancel();
                            if (mActivityInfo.get(ActivityCreateInfoItem.ACTIVITY_SCHOOL_PARAM) != null) {
                                mActivityInfo.remove(ActivityCreateInfoItem.ACTIVITY_SCHOOL_PARAM);
                            }
                            mActivityInfo.put(ActivityCreateInfoItem.ACTIVITY_SCHOOL_PARAM, mSchoolName[which]);
                        }
                    }, mSchoolName);
                }
                break;
            case ActivityCreateInfoItem.ACTIVITY_AMOUNT:
                getDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        valueView.setText(mAmount[which]);
                        dialog.cancel();
                        if (mActivityInfo.get(ActivityCreateInfoItem.ACTIVITY_AMOUNT_PARAM) != null) {
                            mActivityInfo.remove(ActivityCreateInfoItem.ACTIVITY_AMOUNT_PARAM);
                        }
                        mActivityInfo.put(ActivityCreateInfoItem.ACTIVITY_AMOUNT_PARAM, mAmount[which]);
                    }
                }, mAmount);
                break;
            case ActivityCreateInfoItem.ACTIVITY_USER_NUM:
                getDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        valueView.setText(mUserNum[which]);
                        dialog.cancel();
                        if (mActivityInfo.get(ActivityCreateInfoItem.ACTIVITY_USER_NUM_PARAM) != null) {
                            mActivityInfo.remove(ActivityCreateInfoItem.ACTIVITY_USER_NUM_PARAM);
                        }
                        mActivityInfo.put(ActivityCreateInfoItem.ACTIVITY_USER_NUM_PARAM, mUserNum[which]);
                    }
                }, mUserNum);
                break;
            default:
                break;
        }
    }

    //是否收费
    private void getDialog(DialogInterface.OnClickListener listener, String[] data) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DELEGATE.getContext());
        builder.setSingleChoiceItems(data, 0, listener);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //style
    private void initStyleData() {
        RestClient.builder()
                .url("activity/styleList")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONArray styleArray = JSON.parseObject(response).getJSONArray("data");
                        final int size = styleArray.size();
                        mStyleArray = new String[size];
                        for (int i = 0; i < size; i++) {
                            final JSONObject style = styleArray.getJSONObject(i);
                            final String name = style.getString("conment");
                            mStyleArray[i] = name;
                        }
                    }
                })
                .build()
                .get();
    }

    //type
    private void initTypeData() {
        RestClient.builder()
                .url("activity/typeList")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONArray typeArray = JSON.parseObject(response).getJSONArray("data");
                        final int size = typeArray.size();
                        mTypeArray = new String[size];
                        for (int i = 0; i < size; i++) {
                            final JSONObject type = typeArray.getJSONObject(i);
                            final String name = type.getString("conment");
                            mTypeArray[i] = name;
                        }
                    }
                })
                .build()
                .get();
    }

    //location
    private void showLocationPickerView(final TextView view) {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(DELEGATE.getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //获取选择的省份

                //确定选择的回调监听
                //返回的分别是三个级别的选中位置
                String province = OPTION1.get(options1).getPickerViewText();
                String city = OPTION2.get(options1).get(options2);
                String county = OPTION3.get(options1).get(options2).get(options3);
                String location = null;
                if (province.equals(city)) {
                    location = province + "-" + county;
                } else {
                    location = province + "-" + city + "-" + county;
                }
                view.setText(location);
                //mActivityLocation，将location数据缓存到MAP集合中
                if (mActivityInfo.get(ActivityCreateInfoItem.ACTIVITY_PROVINCE) != null) {
                    mActivityInfo.remove(ActivityCreateInfoItem.ACTIVITY_PROVINCE);
                }
                mActivityInfo.put(ActivityCreateInfoItem.ACTIVITY_PROVINCE_PARAM, province);
                if (mActivityInfo.get(ActivityCreateInfoItem.ACTIVITY_CITY) != null) {
                    mActivityInfo.remove(ActivityCreateInfoItem.ACTIVITY_CITY);
                }
                mActivityInfo.put(ActivityCreateInfoItem.ACTIVITY_CITY_PARAM, city);
                if (mActivityInfo.get(ActivityCreateInfoItem.ACTIVITY_COUNTY) != null) {
                    mActivityInfo.remove(ActivityCreateInfoItem.ACTIVITY_COUNTY);
                }
                mActivityInfo.put(ActivityCreateInfoItem.ACTIVITY_COUNTY_PARAM, county);
                mProvinceName = province;
                //初始化该省份下的学校信息
                initProvinceSchoolData();
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

    //school
    private void initProvinceSchoolData() {
        if (mProvinceName != null) {
            RestClient.builder()
                    .url("activity/school")
                    .params("provinceName", mProvinceName)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            final JSONArray schoolArray = JSON.parseObject(response).getJSONArray("data");
                            final int size = schoolArray.size();
                            mSchoolName = new String[size];
                            for (int i = 0; i < size; i++) {
                                final JSONObject school = schoolArray.getJSONObject(i);
                                final String name = school.getString("name");
                                mSchoolName[i] = name;
                            }
                        }
                    })
                    .build()
                    .get();
        }
    }

    public static Map<String, Object> getActivityInfo() {
        return mActivityInfo;
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
