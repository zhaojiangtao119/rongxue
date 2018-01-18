package com.labelwall.mywall.util.timer;

import java.util.TimerTask;

/**
 * Created by Administrator on 2018-01-04.
 */

public class BaseTimerTask extends TimerTask {

    private ITimerLisenter mITimerLisenter;

    public BaseTimerTask(ITimerLisenter timerLisenter) {
        this.mITimerLisenter = timerLisenter;
    }

    @Override
    public void run() {
        if (mITimerLisenter != null) {
            mITimerLisenter.onTimer();
        }
    }
}
