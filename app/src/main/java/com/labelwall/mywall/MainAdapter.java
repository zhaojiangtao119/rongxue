package com.labelwall.mywall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-01-11.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private List<String> mList;
    private Context mContext;
    private List<Integer> mHeights;

    public MainAdapter(List<String> list, Context context) {
        this.mList = list;
        this.mContext = context;
        mHeights = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            mHeights.add((int) (Math.random() * 300));
        }
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_item, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {
        holder.tv.setText(mList.get(position));
        if (mOnItemClickListener != null) {
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, pos);
                }
            });
            holder.tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(v, pos);
                    return false;
                }
            });
        }
        ViewGroup.LayoutParams params = holder.tv.getLayoutParams();
        params.height = mHeights.get(position);
        holder.tv.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //长按删除Item
    public void removeItem(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;

        public MainViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_main_text);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
