package com.labelwall.mywall.delegates.base;

/**
 * Created by Administrator on 2018-01-03.
 */

public abstract class WallDelegate extends PermissionCheckerDelegate {

    public <T extends WallDelegate> T getParentDelegate(){
        return (T) getParentFragment();
    }

}
