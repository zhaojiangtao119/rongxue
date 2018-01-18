package com.labelwall.mywall.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Administrator on 2018-01-05.
 * greenDao，DatabaseOpenHelper数据库帮助类
 */

public class ReleaseOpenHelper extends DaoMaster.OpenHelper {

    //name:就是要创建或要打开的数据库的名称
    public ReleaseOpenHelper(Context context, String name) {
        super(context, name);
    }

    /**
     * 创建或打开DB
     * @param db
     */
    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }
}
