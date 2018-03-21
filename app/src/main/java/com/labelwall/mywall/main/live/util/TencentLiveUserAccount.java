package com.labelwall.mywall.main.live.util;

import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

/**
 * Created by Administrator on 2018-03-17.
 */

public class TencentLiveUserAccount {

    private static final long USER_ID = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());

    public static final String getTencentIdentifier() {
        return "MyWallUser" + String.valueOf(USER_ID);
    }

    public static final String getTencentPassword() {
        return "AppUserPass" + String.valueOf(USER_ID);
    }

}
