package com.labelwall.mywall.main.index;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.labelwall.mywall.delegates.base.WallDelegate;

import java.util.List;

/**
 * Created by Administrator on 2018-01-11.
 */

public class IndexListDelegateAdapter extends FragmentStatePagerAdapter {

    private final List<WallDelegate> DELEGATES;
    private final List<String> TITLES;

    protected IndexListDelegateAdapter(FragmentManager fm, List<WallDelegate> delegates, List<String> titles) {
        super(fm);
        this.DELEGATES = delegates;
        this.TITLES = titles;
    }

    public static IndexListDelegateAdapter create(FragmentManager fm, List<WallDelegate> delegates, List<String> titles) {
        return new IndexListDelegateAdapter(fm, delegates, titles);
    }

    @Override
    public Fragment getItem(int position) {
        return DELEGATES.get(position);
    }

    @Override
    public int getCount() {
        return DELEGATES.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES.get(position);
    }
}
