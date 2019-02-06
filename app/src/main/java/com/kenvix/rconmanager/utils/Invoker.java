package com.kenvix.rconmanager.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kenvix.rconmanager.R;
import com.kenvix.utils.PreprocessorName;

import java.lang.reflect.InvocationTargetException;

public final class Invoker {
    private static AppCompatActivity baseActivityInvocation = null;
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
            Log.w("Invoker for Activity", "Invoker can't detect loader method, may cause NullPointerException: " + ex.getMessage());
        } catch (InvocationTargetException ex) {
            Log.e("Invoker for Activity", "Target Loader throws a unexpected exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            Log.e("Invoker for Activity", "No such view auto loader generated: " + targetRaw.getClass().getCanonicalName() + " : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void invokeViewAutoLoader(Object targetRaw, View targetView) {
        try {
            viewToolset.getMethod(PreprocessorName.getViewAutoLoaderMethodName(targetRaw.getClass().getCanonicalName()), Object.class, View.class)
                    .invoke(null, targetRaw, targetView);
        } catch (NoSuchMethodException ex) {
            Log.w("Invoker for View", "Invoker can't detect loader method, may cause NullPointerException: " + ex.getMessage());
        } catch (InvocationTargetException ex) {
            Log.e("Invoker for View", "Target Loader throws a unexpected exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            Log.e("Invoker for View", "No such view auto loader generated: " + targetRaw.getClass().getCanonicalName() + " : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static boolean invokeFormChecker(Object targetRaw) {
        try {
            return (boolean) formChecker.getMethod(PreprocessorName.getFormEmptyCheckerMethodName(targetRaw.getClass().getCanonicalName()), String.class, Object.class)
                    .invoke(null, getString(R.string.error_field_required), targetRaw);
        } catch (Exception ex) {
            Log.e("Invoker", "No such form checker generated: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static AppCompatActivity getBaseActivityInvocation() {
        return baseActivityInvocation;
    }

    public static void setBaseActivityInvocation(AppCompatActivity baseActivityInvocation) {
        Invoker.baseActivityInvocation = baseActivityInvocation;
    }

    public static String getString(int id, Object... formatArgs) {
        return baseActivityInvocation.getString(id, formatArgs);
    }

    public static String getString(int id) {
        return baseActivityInvocation.getString(id);
    }

    public static int getColor(int id) {
        return baseActivityInvocation.getColor(id);
    }

    public static Drawable getDrawable(int id) {
        return baseActivityInvocation.getDrawable(id);
    }

    public static Resources getResources() {
        return baseActivityInvocation.getResources();
    }

    public static <T extends View> T findViewById(int id) {
        return baseActivityInvocation.findViewById(id);
    }
}
