package me.wztc.util;

import android.util.Log;

public class Logger {

    private static final String TAG = "playParking";
    // Ant modified data start
    private static boolean OUTPUT = true;
    // Ant modified data end

    //获取log打点 的类名
    private static StringBuilder getLogBase() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        sb.append(ste[6].getClassName());
        sb.append('.');
        sb.append(ste[6].getMethodName());
        sb.append("\t-\t");
        return sb;
    }

    public static void logV(String tag, String message) {
        if (OUTPUT) {
            if (AppUtils.isEmpty(tag))
                tag = TAG;
            Log.v(tag, getLogBase().append(message).toString());
        }
    }

    public static void logD(String tag, String message) {
        if (OUTPUT) {
            if (AppUtils.isEmpty(tag))
                tag = TAG;
            Log.d(tag, getLogBase().append(message).toString());
        }
    }

    public static void logI(String tag, String message) {
        if (OUTPUT) {
            if (AppUtils.isEmpty(tag))
                tag = TAG;
            Log.i(tag, getLogBase().append(message).toString());
        }
    }

    public static void logW(String tag, String message) {
        if (OUTPUT) {
            if (AppUtils.isEmpty(tag))
                tag = TAG;
            Log.w(tag, getLogBase().append(message).toString());
        }
    }

    public static void logE(String tag, String message) {
        if (OUTPUT) {
            if (AppUtils.isEmpty(tag))
                tag = TAG;
            Log.e(tag, getLogBase().append(message).toString());
        }
    }

}
