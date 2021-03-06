package com.labelwall.mywall.main.sort.list;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.labelwall.mywall.R;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.sort.SortDelegate;
import com.labelwall.mywall.main.sort.content.ContentDelegate;
import com.labelwall.mywall.ui.recycler.ItemType;
import com.labelwall.mywall.ui.recycler.MultipleFields;
import com.labelwall.mywall.ui.recycler.MultipleItemEntity;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewAdapter;
import com.labelwall.mywall.ui.recycler.MultipleRecyclerViewHolder;
import com.labelwall.mywall.util.callback.CallbackManager;
import com.labelwall.mywall.util.callback.CallbackType;
import com.labelwall.mywall.util.callback.IGlobalCallback;

import java.util.List;

import me.yokeyword.fragmentation.SupportHelper;

/**
 * Created by Administrator on 2018-01-19.
 */

public class CategoryAdapter extends MultipleRecyclerViewAdapter {

    private final SortDelegate DELEGATE;
    private AppCompatEditText mProductKeyword;
    private String mKeyword = null;

    private int mProPosition = 0;//上一个position

    public CategoryAdapter(List<MultipleItemEntity> data, SortDelegate delegate) {
        super(data);
        this.DELEGATE = delegate;

        //添加布局
        addItemType(ItemType.VERTICAL_MENU_LIST, R.layout.item_vertical_menu_list);
    }

    @Override
    protected void convert(MultipleRecyclerViewHolder holder, final MultipleItemEntity itemEntity) {
        super.convert(holder, itemEntity);
        switch (holder.getItemViewType()) {
            case ItemType.VERTICAL_MENU_LIST:
                final String name = itemEntity.getField(CategoryListField.NAME);
                final boolean isClicked = itemEntity.getField(MultipleFields.TAG);
                final AppCompatTextView nameView = holder.getView(R.id.tv_vertical_item_name);
                final View line = holder.getView(R.id.view_line);
                final View itemView = holder.itemView;

                //获得当前点击的item的position
                final int currentPosition = holder.getAdapterPosition();
                final int contentId = getData().get(currentPosition).getField(CategoryListField.ID);
                //建议使用匿名内部类的方式实现单击监听
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //获取搜索的关键字，通过SortDelegate
                        View view = DELEGATE.getView();
                        AppCompatEditText mProductKeyword = (AppCompatEditText) view.findViewById(R.id.product_keyword);
                        mKeyword = mProductKeyword.getText().toString();

                        //判断当前点击的item的position与上一个点击item的postion是否相等
                        if (mProPosition != currentPosition) {
                            //更新上一个点击的item的tag为false
                            getData().get(mProPosition).setField(MultipleFields.TAG, false);
                            //更新recyclerView
                            notifyItemChanged(mProPosition);
                            //更新选中的Item的tag为true
                            itemEntity.setField(MultipleFields.TAG, true);
                            notifyItemChanged(currentPosition);
                            //将当前的item的position赋值给mProPosition
                            mProPosition = currentPosition;
                            showContent(contentId);
                        }
                    }
                });
                //判断是否点击了该item，来设置Item的样式
                if (!isClicked) {
                    //没有点击该Item
                    line.setVisibility(View.INVISIBLE);
                    nameView.setTextColor(ContextCompat.getColor(mContext, R.color.we_chat_black));
                    itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.item_background));
                } else {
                    //点击了
                    line.setVisibility(View.VISIBLE);
                    nameView.setTextColor(ContextCompat.getColor(mContext, R.color.app_title));
                    line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.app_title));
                    itemView.setBackgroundColor(Color.WHITE);
                    showContent(contentId);
                    //将当前的categoryId存储到全局的回调中去。
                    final IGlobalCallback<Integer> callback = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.CONTENT_PRODUCT_ID);
                    if (callback != null) {
                        callback.executeCallback(contentId);
                    }
                }
                holder.setText(R.id.tv_vertical_item_name, name);
                break;
            default:
                break;
        }
    }

    private void showContent(int contentId) {
        final ContentDelegate delegate = ContentDelegate.newInstance(contentId, mKeyword);
        switchDelegate(delegate);
    }

    private void switchDelegate(ContentDelegate delegate) {
        final WallDelegate contentDelegtate =
                SupportHelper.findFragment(DELEGATE.getChildFragmentManager(), ContentDelegate.class);
        if (contentDelegtate != null) {
            contentDelegtate.getSupportDelegate().replaceFragment(delegate, false);
        }
    }
}
