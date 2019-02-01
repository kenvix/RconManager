package com.kenvix.rconmanager.utils;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kenvix.rconmanager.R;

public final class Invoker {
    private final static ClassLoader classLoader = Invoker.class.getClassLoader();
    private static Class<?> formChecker;

    static {
        try {
            formChecker = classLoader.loadClass("com.kenvix.rconmanager.FormChecker");
        } catch (ClassNotFoundException ex) {
            Log.e("Invoker", "Unexpected Invoker ClassNotFoundException. You may write some shit codes.");
        }
    }

    public static <T extends AppCompatActivity> void invokeFormChecker(String formTag, T target) {
        try {
            formChecker.getMethod(formTag, String.class, AppCompatActivity.class).invoke(null, target.getString(R.string.error_field_required), target);
        } catch (Exception ex) {
            Log.w("Invoker", "No such form checker generated (for AppCompatActivity): " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static <T extends View> void invokeFormChecker(String formTag, T target) {
        try {
            formChecker.getMethod(formTag, String.class, View.class).invoke(null, target.getContext().getString(R.string.error_field_required), target);
        } catch (Exception ex) {
            Log.w("Invoker", "No such form checker generated (for View): " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
