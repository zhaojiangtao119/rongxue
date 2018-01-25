package com.labelwall.mywall.main.sort.search;

import com.labelwall.mywall.R;
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

    }
}
