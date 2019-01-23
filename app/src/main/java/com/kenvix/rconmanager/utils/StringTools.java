// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.utils;

public class StringTools {
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
}
