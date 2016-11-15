package net.lzzy.mymemo.dataPersist;


import net.lzzy.sqlitelib.DbPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 007 on 2016/5/4.
 * 静态常量键
 */
public final class DbConstants {
    private DbConstants() {
    }

    public static final String DB_NAME = "Memos.db";
    /**
     * 数据库版本
     */
    public static final int DB_VERSION = 1;
    /**
     * 创建表头语句
     **/
    public static final String CREATE_TABLE_HEAD = "create table ";
    /**
     * 数据库字段类型
     */
    private static final String TYPE_INT = " integer";
    private static final String TYPE_STRING = " text";
    private static final String TYPE_REAL = " real";

    public static final String BOOK_VERIFY = "verify";
    private static final String RESTRAIN_AUTOINCREMENT = " primary key autoincrement";

    /**
     * table memo
     **/
    public static final String MEMO_TABLE_NAME = "memo";
    public static final String MEMO_ID = "id";
    public static final String MEMO_TITLE = "title";
    public static final String MEMO_CONTENT = "content";
    public static final String MEMO_UPDATE_TIME = "updateTime";
    public static final StringBuilder MEMO_TABLE_SQL = new StringBuilder();


    public static final List<String> CREATE_TABLE_SQLS = new ArrayList<>();
    public static final List<String> UPDATE_TABLE_SQLS = new ArrayList<>();
    public static DbPackage dbPackage;

    static {
        MEMO_TABLE_SQL.append(CREATE_TABLE_HEAD)
                .append(MEMO_TABLE_NAME).append("(")
                .append(MEMO_ID).append(TYPE_STRING).append(",")
                .append(MEMO_TITLE).append(TYPE_STRING).append(",")
                .append(MEMO_CONTENT).append(TYPE_STRING).append(",")
                .append(MEMO_UPDATE_TIME).append(TYPE_REAL).append(")");
        CREATE_TABLE_SQLS.add(MEMO_TABLE_SQL.toString());
        dbPackage = DbPackage.getInstance(DB_NAME, DB_VERSION, CREATE_TABLE_SQLS, UPDATE_TABLE_SQLS);
    }
}
