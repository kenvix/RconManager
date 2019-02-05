package com.kenvix.rconmanager.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.kenvix.rconmanager.database.GlobalDatabaseHelper;
import com.kenvix.utils.ReflectTools;
import com.kenvix.utils.StringTools;

public abstract class BaseModel {
    private GlobalDatabaseHelper helper;
    private String tableName;
    private boolean isTransactionActive = false;

    private SQLiteDatabase transactionWriteModeDatabase = null;
    private SQLiteDatabase transactionReadModeDatabase = null;

    public BaseModel(Context context) {
        helper = new GlobalDatabaseHelper(context);

        String className = this.getClass().getSimpleName();
        tableName = StringTools.convertUppercaseLetterToUnderlinedLowercaseLetter(className.substring(0, className.indexOf("Model")));
    }

    long insert(ContentValues values, String nullColumnHack) throws SQLException {
        return getWritableDatabase().insertOrThrow(tableName, nullColumnHack, values);
    }

    long insert(ContentValues values) throws SQLException {
        return insert(values, null);
    }

    int update(ContentValues values, String whereClause, String[] whereArgs) throws SQLException {
        return getWritableDatabase().update(tableName, values, whereClause, whereArgs);
    }

    Cursor select(String whereClause, String[] whereArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy, @Nullable String[] columns) {
        return getReadableDatabase().query(tableName, columns, whereClause, whereArgs, groupBy, having, orderBy);
    }

    Cursor select(String whereClause, String[] whereArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy) {
        return select( whereClause, whereArgs, groupBy, having, orderBy, null);
    }

    Cursor select(String whereClause, String[] whereArgs, @Nullable String orderBy) {
        return select( whereClause, whereArgs, null, null, orderBy, null);
    }

    Cursor select(String whereClause, String[] whereArgs) {
        return select( whereClause, whereArgs, null, null, null, null);
    }

    Cursor selectOne(String whereClause, String[] whereArgs, @Nullable String[] columns) {
        Cursor cursor = getReadableDatabase().query(tableName, columns, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    Cursor selectOne(String whereClause, String[] whereArgs) {
        return selectOne(whereClause, whereArgs, null);
    }

    int delete(String whereClause, String[] whereArgs) {
        return getWritableDatabase().delete(tableName, whereClause, whereArgs);
    }

    GlobalDatabaseHelper getHelper() {
        return helper;
    }

    String getTableName() {
        return tableName;
    }

    SQLiteDatabase getWritableDatabase() {
        return isTransactionActive ? transactionWriteModeDatabase : helper.getWritableDatabase();
    }

    SQLiteDatabase getReadableDatabase() {
        return isTransactionActive ? transactionReadModeDatabase : helper.getReadableDatabase();
    }

    void startTransaction() {
        if(isTransactionActive)
            throw new IllegalStateException("Another Transaction already started.");

        transactionWriteModeDatabase = getWritableDatabase();
        transactionReadModeDatabase = getReadableDatabase();
        isTransactionActive = true;

        transactionWriteModeDatabase.beginTransaction();
    }

    void commitTransaction() {
        if(!isTransactionActive)
            throw new IllegalStateException("Commit not allowed: No active Transaction");

        transactionWriteModeDatabase.setTransactionSuccessful();
        rollbackTransaction();
    }

    void rollbackTransaction() {
        if(!isTransactionActive)
            throw new IllegalStateException("No active Transaction");

        isTransactionActive = false;
        transactionWriteModeDatabase = null;
        transactionReadModeDatabase = null;

        transactionWriteModeDatabase.endTransaction();
    }

    String[] getSignleWhereValue(String value) {
        return new String[] {value};
    }

    String[] getSignleWhereValue(Object value) {
        return new String[] {String.valueOf(value)};
    }
}
