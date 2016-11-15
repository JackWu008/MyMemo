package net.lzzy.mymemo.models;

import android.database.Cursor;

import net.lzzy.mymemo.dataPersist.DbConstants;
import net.lzzy.sqlitelib.ISqlitable;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Administrator on 2016/9/20.
 */
public class Memo implements ISqlitable {
    private UUID id;
    private String title;//显示部分内容
    private String content;//内容
    private Date updateTime;//日期

    public Memo() {
        id = java.util.UUID.randomUUID();
        updateTime = new Date();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        if (content.length() > 20) {
            title = content.substring(0, 20).concat("...");
        } else {
            title = content;
        }
    }

    public String getTitle() {
        return title;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public String getTableName() {
        return DbConstants.MEMO_TABLE_NAME;
    }

    @Override
    public String getPKName() {
        return DbConstants.MEMO_ID;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public HashMap<String, Object> getInsertCols() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DbConstants.MEMO_ID, id.toString());
        map.put(DbConstants.MEMO_TITLE, title);
        map.put(DbConstants.MEMO_CONTENT, content);
        map.put(DbConstants.MEMO_UPDATE_TIME, updateTime.getTime());
        return map;
    }

    @Override
    public HashMap<String, Object> getUpdateCols() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DbConstants.MEMO_TITLE, title);
        map.put(DbConstants.MEMO_CONTENT, content);
        map.put(DbConstants.MEMO_UPDATE_TIME, updateTime.getTime());
        return map;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        id = UUID.fromString(cursor.getString(cursor.getColumnIndex(DbConstants.MEMO_ID)));
        content = cursor.getString(cursor.getColumnIndex(DbConstants.MEMO_CONTENT));
        title = cursor.getString(cursor.getColumnIndex(DbConstants.MEMO_TITLE));
        updateTime = new Date(cursor.getLong(cursor.getColumnIndex(DbConstants.MEMO_UPDATE_TIME)));
    }
}
