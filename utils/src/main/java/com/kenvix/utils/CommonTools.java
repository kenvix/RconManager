package com.kenvix.utils;

public class CommonTools {

    /**
     * Sleep
     * @param millis time
     * @return false if sleep is interrupted
     */
    public static boolean sleep(long millis) {
        try {
            Thread.sleep(millis, 0);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
