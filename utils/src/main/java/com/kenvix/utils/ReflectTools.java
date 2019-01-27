package com.kenvix.utils;

import com.kenvix.utils.annotation.Description;

import java.lang.reflect.Method;
import java.util.NoSuchElementException;

public class ReflectTools {
    public static String getMethodDescription(Method method) {
        if(!method.isAnnotationPresent(Description.class))
            throw new NoSuchElementException("No Description found.");
        return method.getAnnotation(Description.class).value();
    }

    public static String getMethodDescription(String className, String methodName) throws ClassNotFoundException, NoSuchMethodException {
        Method targetMethod = ReflectTools.class.getClassLoader().loadClass(className).getMethod(methodName);
        return getMethodDescription(targetMethod);
    }

    public static String getMethodDescription() throws ClassNotFoundException, NoSuchMethodException {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTraceElement[stackTraceElement.length-1];
        return getMethodDescription(element.getClassName(), element.getMethodName());
    }
}
