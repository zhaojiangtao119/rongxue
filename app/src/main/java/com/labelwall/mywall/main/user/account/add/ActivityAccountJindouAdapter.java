package com.labelwall.mywall.main.user.account.add;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;

import java.util.List;

/**
 * Created by Administrator on 2018-02-23.
 */

public class ActivityAccountJindouAdapter extends BaseAdapter {

    private final WallDelegate DELEGATE;
    private final List<String> DATA;
    private final LayoutInflater INFLATER;
    private int mTemp = -1;

    public ActivityAccountJindouAdapter(WallDelegate delegate, List<String> data) {
        this.DELEGATE = delegate;
        this.DATA = data;
        INFLATER = LayoutInflater.from(delegate.getContext());
    }

    @Override
    public int getCount() {
        return DATA.size();
    }

    @Override
    public Object getItem(int position) {
        return DATA.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = INFLATER.inflate(R.layout.item_account_jindou, null);
            viewHolder = new ViewHolder();
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.tv_jindou_count);
            viewHolder.cbChecked = (CheckBox) convertView.findViewById(R.id.tv_jindou_checked);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvCount.setText(DATA.get(position));
        viewHolder.cbChecked.setId(position);
        viewHolder.cbChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mTemp != -1) {
                        CheckBox tempCheckBox = (CheckBox) DELEGATE.getActivity().findViewById(mTemp);
                        if (tempCheckBox != null) {
                            tempCheckBox.setChecked(false);
                        }
                        if (mCheckedListener != null) {
                            mCheckedListener.selected(position);
                        }
                    }
                    mTemp = buttonView.getId();
                }
            }
        });
        if (position == mTemp) {
            viewHolder.cbChecked.setChecked(true);
        } else {
            viewHolder.cbChecked.setChecked(false);
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView tvCount;
        CheckBox cbChecked;
    }

    private CheckedListener mCheckedListener;

    public void setCheckedListener(CheckedListener listener) {
        this.mCheckedListener = listener;
    }

    //监听哪一个被选中了
    public interface CheckedListener {
        void selected(int postion);
    }
}
