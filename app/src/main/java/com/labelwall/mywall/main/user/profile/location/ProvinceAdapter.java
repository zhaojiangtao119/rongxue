package com.labelwall.mywall.main.user.profile.location;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.labelwall.mywall.R;

import java.util.List;

/**
 * Created by Administrator on 2018-01-19.
 */

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder> {

    private final List<ProvinceBean> PROVINCE_LIST;
    private final Context CONTENT;
    private final LayoutInflater INFLATER;

    private ProvinceAdapter(List<ProvinceBean> provinceList, Context context) {
        this.PROVINCE_LIST = provinceList;
        this.CONTENT = context;
        this.INFLATER = LayoutInflater.from(context);
    }

    public static ProvinceAdapter create(List<ProvinceBean> provinceList, Context context) {
        return new ProvinceAdapter(provinceList, context);
    }

    @Override
    public ProvinceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = INFLATER.inflate(R.layout.item_province, parent, false);
        ProvinceViewHolder provinceViewHolder = new ProvinceViewHolder(view);
        return provinceViewHolder;
    }

    @Override
    public void onBindViewHolder(ProvinceViewHolder holder, final int position) {
        final ProvinceBean provinceBean = PROVINCE_LIST.get(position);
        holder.mProvinceName.setText(provinceBean.getProvinceName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CONTENT, "点击了城市：" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return PROVINCE_LIST != null ? PROVINCE_LIST.size() : 0;
    }

    class ProvinceViewHolder extends RecyclerView.ViewHolder {
        TextView mProvinceName;

        public ProvinceViewHolder(View itemView) {
            super(itemView);
            mProvinceName = (TextView) itemView.findViewById(R.id.tv_province);
        }
    }
}
