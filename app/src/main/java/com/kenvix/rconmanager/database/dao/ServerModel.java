package com.kenvix.rconmanager.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.kenvix.rconmanager.rcon.meta.RconServer;

public class ServerModel extends BaseModel {
    public static final String FieldSid  = "sid";
    public static final String FieldName = "name";
    public static final String FieldHost = "host";
    public static final String FieldPort = "port";
    public static final String FieldPassword = "password";

    public ServerModel(Context context) {
        super(context);
    }

    public Cursor getAll() {
        return select(null, null);
    }

    public void add(String name, String host, int port, String password) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FieldName, name);
        contentValues.put(FieldHost, host);
        contentValues.put(FieldPort, port);
        contentValues.put(FieldPassword, password);

        insert(contentValues);
    }

    public void add(RconServer server) {
        insert(getContentValuesByRconServer(server));
    }

    public int updateBySid(int sid, RconServer server) {
        return update(getContentValuesByRconServer(server), "sid = ?", getSignleWhereValue(sid));
    }

    public int updateBySid(int sid, String name, String host, int port, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FieldName, name);
        contentValues.put(FieldHost, host);
        contentValues.put(FieldPort, port);
        contentValues.put(FieldPassword, password);

        return update(contentValues, "sid = ?", getSignleWhereValue(sid));
    }

    public static ContentValues getContentValuesByRconServer(RconServer server) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FieldName, server.getName());
        contentValues.put(FieldHost, server.getHost());
        contentValues.put(FieldPort, server.getPort());
        contentValues.put(FieldPassword, server.getPassword());

        return contentValues;
    }

    public Cursor getBySid(int sid) {
        return selectOne("sid = ?", getSignleWhereValue(sid));
    }

    public void deleteBySid(int sid) throws SQLException {
        delete("sid = ?", getSignleWhereValue(sid));
    }

    public void updateBySid(int sid, ContentValues values) throws SQLException {
        update(values, "sid = ?", getSignleWhereValue(sid));
    }
}
