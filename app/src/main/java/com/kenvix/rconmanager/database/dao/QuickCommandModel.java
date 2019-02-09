package com.kenvix.rconmanager.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.kenvix.rconmanager.meta.QuickCommand;

public class QuickCommandModel extends BaseModel {
    public static final String FieldCid   = "cid";
    public static final String FieldName  = "name";
    public static final String FieldValue = "value";

    public QuickCommandModel(Context context) {
        super(context);
    }

    public Cursor getAll() {
        return select(null, null);
    }

    public void add(String name, String value) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FieldName, name);
        contentValues.put(FieldValue, value);

        insert(contentValues);
    }

    public void add(QuickCommand quickCommand) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FieldName, quickCommand.getName());
        contentValues.put(FieldValue, quickCommand.getValue());

        insert(contentValues);
    }


    public int updateByCid(int cid, String name, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FieldName, name);
        contentValues.put(FieldValue, value);

        return update(contentValues, "cid = ?", getSignleWhereValue(cid));
    }

    public Cursor getByCid(int cid) {
        return selectOne("cid = ?", getSignleWhereValue(cid));
    }

    public void deleteByCid(int cid) throws SQLException {
        delete("cid = ?", getSignleWhereValue(cid));
    }

    public void updateByCid(int cid, ContentValues values) throws SQLException {
        update(values, "cid = ?", getSignleWhereValue(cid));
    }
}
