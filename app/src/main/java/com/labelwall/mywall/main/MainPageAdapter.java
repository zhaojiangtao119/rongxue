package com.labelwall.mywall.main;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.labelwall.mywall.R;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-09.
 */

public class MainPageAdapter extends MultipleRecyclerViewAdapter {

    private List<Integer> mTextViewWidth = new ArrayList<>();

    public MainPageAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(100, R.layout.item_main_page);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()) {
            case 100:
                final TextView itemView = helper.getView(R.id.tv_main_item);
                final String itemStr = item.getField(MultipleFields.TEXT);
                int postion = helper.getAdapterPosition();
                if (mTextViewWidth.size() <= postion) {
                    int viewWidth = (int) (Math.random() * 100 + 300);
                    mTextViewWidth.add(viewWidth);
                }
                itemView.setLayoutParams(new LinearLayout.LayoutParams(mTextViewWidth.get(postion), 150));
                itemView.setText(itemStr);
                break;
        }

    }
}
