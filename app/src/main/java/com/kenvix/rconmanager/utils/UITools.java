package com.kenvix.rconmanager.utils;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kenvix.rconmanager.R;

import java.lang.reflect.Field;

public class UITools {

    public static <T extends AppCompatActivity> void initializeViewFields(T target) {
        for(Field field: target.getClass().getDeclaredFields()) {
            if(field != null) {
                if(field.isAnnotationPresent(ViewAutoLoad.class)) {
                    try {
                        Field RField;
                        try {
                            RField = R.id.class.getField(convertName(field.getName()));
                        } catch (NoSuchFieldException ex) {
                            RField = R.id.class.getDeclaredField(field.getName());
                        }
                        Class<?> RFieldType = RField.getClass();
                        if(field.getClass().isAssignableFrom(RFieldType)) {
                            int RViewId = RField.getInt(R.id.class);
                            Object RView = target.findViewById(RViewId);
                            field.setAccessible(true);
                            field.set(target, RView);
                        } else {
                            throw new IllegalArgumentException("are u kidding me?");
                        }
                    } catch (NoSuchFieldException ex) {
                        Log.w("UI Field Initializer", "Invalid ViewAutoLoad Field: (Field Not Found) " + ex.getMessage());
                    } catch (IllegalAccessException ex) {
                        Log.w("UI Field Initializer", "Invalid ViewAutoLoad Field: (Access Denied) " + ex.getMessage());
                    }
                }
            }
        }
    }

    private static String convertName(String name) {
        char[] chars = name.toCharArray();
        StringBuilder stringBuilder= new StringBuilder();
        for (char c: chars){
            if(c >= 'A' && c <='Z') {
                stringBuilder.append("_").append(c + 32);
            } else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}
