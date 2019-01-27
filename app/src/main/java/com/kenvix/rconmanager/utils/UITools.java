package com.kenvix.rconmanager.utils;

import android.support.annotation.NonNull;
import android.util.Log;
import com.kenvix.rconmanager.R;
import com.kenvix.utils.StringTools;
import com.kenvix.utils.annotation.ViewAutoLoad;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class UITools {

    //TODO: Cache reflect query result.
    public static void initializeViewFields(@NonNull Object target) {
        Class<?> targetClass =  target.getClass();

        for(Field field: targetClass.getDeclaredFields()) {
            if(field != null) {
                if(field.isAnnotationPresent(ViewAutoLoad.class)) {
                    try {
                        Field RField;

                        try {
                            RField = R.id.class.getField(StringTools.convertUppercaseLetterToUnderlinedLowercaseLetter(field.getName()));
                        } catch (NoSuchFieldException ex) {
                            RField = R.id.class.getDeclaredField(field.getName());
                        }

                        Class<?> RFieldType = RField.getClass();

                        if(field.getClass().isAssignableFrom(RFieldType)) {
                            int RViewId = RField.getInt(R.id.class);

                            try {
                                Object RView = targetClass.getMethod("findViewById", int.class).invoke(target, RViewId);

                                field.setAccessible(true);
                                field.set(target, RView);
                            } catch (NoSuchMethodException ex) {
                                throw new IllegalArgumentException("Invalid input object: " + targetClass.getName() + " . There no method findViewById()");
                            } catch (InvocationTargetException ex) {
                                throw new IllegalArgumentException("Invalid input object: " + targetClass.getName() + " . method findViewById() is not callable");
                            }

                        } else {
                            throw new IllegalArgumentException("Invalid field. stupid");
                        }

                    } catch (NoSuchFieldException ex) {
                        Log.w("UI Field Initializer", String.format("No such UI Field [%s] or [%s] : %s", StringTools.convertUppercaseLetterToUnderlinedLowercaseLetter(field.getName()), field.getName(), ex.getMessage()));
                    } catch (IllegalAccessException ex) {
                        Log.w("UI Field Initializer", "Invalid ViewAutoLoad Field: (Access Denied) " + ex.getMessage());
                    }
                }
            }
        }
    }



}
