package com.kenvix.rconmanager.database;

import java.util.List;

public final class IntegratedSQLCommands {
    public static String getCreateServerListTableSQL() {
        return "CREATE TABLE \"main\".\"server\" IF NOT EXISTS (\n" +
                "  \"sid\" INTEGER NOT NULL ON CONFLICT FAIL PRIMARY KEY AUTOINCREMENT,\n" +
                "  \"name\" TEXT NOT NULL ON CONFLICT FAIL,\n" +
                "  \"host\" TEXT NOT NULL ON CONFLICT FAIL,\n" +
                "  \"port\" integer NOT NULL ON CONFLICT FAIL,\n" +
                "  \"password\" TEXT NOT NULL\n" +
                ");";
    }

}
