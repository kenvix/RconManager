package com.kenvix.rconmanager.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;

public class ServerModel extends BaseModel {

    public ServerModel(Context context) {
        super(context);
    }

    public void getAll() {

    }

    public void add(String name, String host, int port, String password) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("host", host);
        contentValues.put("port", port);
        contentValues.put("password", password);

        insert(contentValues);
    }

    public void deleteBySID(int sid) throws SQLException {
        delete("sid = ?", new String[]{ String.valueOf(sid) });
    }
}
