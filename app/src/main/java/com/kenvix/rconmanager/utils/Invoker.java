package com.kenvix.rconmanager.utils;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kenvix.rconmanager.R;
import com.kenvix.utils.PreprocessorName;

public final class Invoker {
    private final static ClassLoader classLoader = Invoker.class.getClassLoader();
    private static Class<?> formChecker;
    private static Class<?> viewToolset;

    static {
        try {
            formChecker = classLoader.loadClass("com.kenvix.rconmanager.generated.FormChecker");
            viewToolset = classLoader.loadClass("com.kenvix.rconmanager.generated.ViewToolset");
        } catch (ClassNotFoundException ex) {
            Log.e("Invoker", "Unexpected Invoker ClassNotFoundException. You may write some shit codes.");
        }
    }

    public static void invokeViewAutoLoader(Object targetRaw) {
        try {
            viewToolset.getMethod(PreprocessorName.getViewAutoLoaderMethodName(targetRaw.getClass().getCanonicalName()), Object.class)
                    .invoke(null, targetRaw);
        } catch (NoSuchMethodException ex) {

        } catch (Exception ex) {
            Log.w("Invoker", "No such view auto loader generated: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static <T extends AppCompatActivity> void invokeFormChecker(String formTag, T target) {
        try {
            formChecker.getMethod(PreprocessorName.getFormCheckerMethodName(formTag), String.class, AppCompatActivity.class)
                    .invoke(null, target.getString(R.string.error_field_required), target);
        } catch (Exception ex) {
            Log.w("Invoker", "No such form checker generated (for AppCompatActivity): " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static <T extends View> void invokeFormChecker(String formTag, T target) {
        try {
            formChecker.getMethod(PreprocessorName.getFormCheckerMethodName(formTag), String.class, View.class)
                    .invoke(null, target.getContext().getString(R.string.error_field_required), target);
        } catch (Exception ex) {
            Log.w("Invoker", "No such form checker generated (for View): " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
