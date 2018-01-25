package com.labelwall.mywall.main.sort.search;

import android.support.v7.widget.AppCompatTextView;
import android.widget.Switch;

import com.labelwall.mywall.R;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018-01-25.
 */

public class SearchAdapter extends MultipleRecyclerViewAdapter {

    public SearchAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(SearchItemType.SEARCH_ITEM, R.layout.item_search);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()) {
            case SearchItemType.SEARCH_ITEM:
                final AppCompatTextView tvSearchItem = helper.getView(R.id.tv_search_item);
                final String history = item.getField(MultipleFields.TEXT);
                tvSearchItem.setText(history);
                break;
            default:
                break;
        }

    }
}
