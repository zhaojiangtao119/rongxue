package com.labelwall.mywall.main.compass.add;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.labelwall.mywall.R;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.ui.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Administrator on 2018-02-10.
 */

public class ActivityCreateClickListener extends SimpleClickListener {

    private CustomDatePicker customDatePicker = null;
    private final WallDelegate DELEGATE;

    public ActivityCreateClickListener(WallDelegate delegate) {
        this.DELEGATE = delegate;
    }

    public static ActivityCreateClickListener create(WallDelegate delegate) {
        return new ActivityCreateClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);//adapter中活动点击的item
        final int id = bean.getId();
        TextView valueView = (TextView) view.findViewById(R.id.tv_arrow_value);

        selectDateTime(valueView);
        switch (id) {
            case ActivityCreateInfoItem.APPLY_START_TIME:
                customDatePicker.show(valueView.getText().toString());
                break;
            case ActivityCreateInfoItem.APPLY_END_TIME:
                customDatePicker.show(valueView.getText().toString());
                break;
            case ActivityCreateInfoItem.ACTIVITY_START_TIME:
                customDatePicker.show(valueView.getText().toString());
                break;
            case ActivityCreateInfoItem.ACTIVITY_END_TIME:
                customDatePicker.show(valueView.getText().toString());
                break;
            default:
                break;
        }
    }
    //选择时间
    private void selectDateTime(final TextView valueView) {
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        final String currentTime = sdf.format(data);
        valueView.setText(currentTime);
        //限制的时间是当前时间+1个月
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(data);
        calendar.add(calendar.MONTH, 1);
        data = calendar.getTime();
        final String endTime = sdf.format(data);
        customDatePicker = new CustomDatePicker(DELEGATE.getContext(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                valueView.setText(time);
            }
        }, currentTime, endTime); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(false); // 允许循环滚动
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
