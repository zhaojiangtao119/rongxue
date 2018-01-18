package com.labelwall.mywall.delegates.bottom;

import android.widget.Toast;

import com.labelwall.mywall.R;
import com.labelwall.mywall.app.MyWall;
import com.labelwall.mywall.delegates.base.WallDelegate;

/**
 * Created by Administrator on 2018-01-04.
 * 每一个子页面的父类
 */

public abstract class BottomItemDelegate extends WallDelegate{

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, "再按一次退出" +
                    MyWall.getApplicationContext().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
