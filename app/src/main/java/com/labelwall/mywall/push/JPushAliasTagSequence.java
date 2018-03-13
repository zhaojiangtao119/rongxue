package com.labelwall.mywall.push;

/**
 * Created by Administrator on 2018-03-13.
 * JPush的alias和Tag操作的标识符
 */

public class JPushAliasTagSequence {

    /**
     * 增加别名
     */
    public static final int ACTION_ALIAS_ADD = 11;
    /**
     * 覆盖别名
     */
    public static final int ACTION_ALIAS_SET = 12;
    /**
     * 删除部分别名
     */
    public static final int ACTION_ALIAS_DELETE = 13;
    /**
     * 删除所有别名
     */
    public static final int ACTION_ALIAS_CLEAN = 14;
    /**
     * 查询别名
     */
    public static final int ACTION_ALIAS_GET = 15;

    /**
     * 增加Tag
     */
    public static final int ACTION_TAG_ADD = 21;
    /**
     * 覆盖Tag
     */
    public static final int ACTION_TAG_SET = 22;
    /**
     * 删除部分指定的Tag
     */
    public static final int ACTION_TAG_DELETE = 23;
    /**
     * 删除所有Tag
     */
    public static final int ACTION_TAG_CLEAN = 24;
    /**
     * 查询Tag
     */
    public static final int ACTION_TAG_GET = 25;

}
