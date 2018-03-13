package com.labelwall.mywall.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Administrator on 2018-01-05.
 * 数据库管理类
 */

public class DataBaseManager {

    private DaoSession mDaoSession = null;
    private UserProfileDao mDao = null;

    private DataBaseManager() {
    }

    private static final class Holder {
        private static final DataBaseManager INSTANCE = new DataBaseManager();
    }

    public static DataBaseManager getInstance() {
        return Holder.INSTANCE;
    }

    public DataBaseManager init(Context context) {
        initDao(context);
        return this;
    }

    //初始化数据库：1.创建数据库 2.创建表 3.初始化session 4.初始化dao
    private void initDao(Context context) {
        final ReleaseOpenHelper helper = new ReleaseOpenHelper(context, "mywall.db");
        final Database db = helper.getWritableDb();//创建数据库：mywall.db
        mDaoSession = new DaoMaster(db).newSession();
        mDao = mDaoSession.getUserProfileDao();
    }

    public final UserProfileDao getDao() {
        return mDao;
    }

    public final DaoSession getDaoSession(){
        return mDaoSession;
    }
}
