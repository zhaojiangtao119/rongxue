package com.labelwall.mywall.ui.date;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.labelwall.mywall.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2018-01-17.
 */

public class DateDialogUtil {

    public void showDialog(Context context) {

        final LinearLayout ll = new LinearLayout(context);
        final DatePicker picker = new DatePicker(context);
        final LinearLayout.LayoutParams lp = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        picker.setLayoutParams(lp);
        //picker.setBackgroundColor(Color.GREEN);
        picker.init(1985, 1, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                final String date = format.format(calendar.getTime());
                if (mIDatelistener != null) {
                    mIDatelistener.onDateChange(date);
                }
            }
        });
        ll.addView(picker);

        new AlertDialog.Builder(context)
                .setTitle("选择日期")
                .setView(ll)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public interface IDateListener {
        void onDateChange(String date);
    }

    private IDateListener mIDatelistener = null;

    public void setDataListener(IDateListener listener) {
        this.mIDatelistener = listener;
    }
}
