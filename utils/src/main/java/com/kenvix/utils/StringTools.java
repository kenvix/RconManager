// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.utils;

import java.util.Locale;

public class StringTools {
    public static String format(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }

    public static String makeFirstLetterUppercase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String makeFirstLetterLowercase(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    /**
     * Convert Uppercase Letter To Underlined Lowercase Letter
     * @param name string to convert
     * @return result
     */
    public static String convertUppercaseLetterToUnderlinedLowercaseLetter(String name) {
        char[] chars = name.toCharArray();
        StringBuilder stringBuilder= new StringBuilder();
        for (char c: chars){
            if(c >= 'A' && c <='Z') {
                stringBuilder.append("_").append((char) (c + (char) 32));
            } else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    public static String convertPackageNameToUppercaseLetter(String name) {
        char[] chars = name.toCharArray();
        StringBuilder stringBuilder= new StringBuilder();
        for (int i = 0; i < chars.length; i++){
            if(chars[i] == '.') {
                if(i < chars.length-1) {
                    if(chars[i+1] <= 'z' && chars[i+1] >= 'a') {
                        stringBuilder.append((char) (chars[i+1] - (char) 32));
                    } else {
                        stringBuilder.append((char) (chars[i+1]));
                    }
                    i++;
                    continue;
                }
            }

            stringBuilder.append(chars[i]);
        }
        return stringBuilder.toString();
    }
}
