package com.labelwall.mywall.main.index.usertopic;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.labelwall.mywall.ui.recycler.DataConverter;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.test.DataCon;

import java.util.List;

/**
 * Created by Administrator on 2018-01-11.
 */

public class UserTopicAdapter extends BaseMultiItemQuickAdapter<MultipleItemEntity, UserTopicViewHolder> {

    protected UserTopicAdapter(List<MultipleItemEntity> data) {
        super(data);
        init();
    }

    private void init() {

    }

    public static UserTopicAdapter create(List<MultipleItemEntity> data) {
        return new UserTopicAdapter(data);
    }

    public static UserTopicAdapter create(DataConverter dataConverter) {
        return new UserTopicAdapter(dataConverter.convert());
    }

    @Override
    protected void convert(UserTopicViewHolder helper, MultipleItemEntity item) {

    }
}
