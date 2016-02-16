package com.zandroid.utils;

import android.content.Context;
import android.util.Log;

import java.util.Locale;

/**
 * Created by prin on 2016/2/15.
 *
 * 打印Log的日志工具类
 *
 */
public class ZLogUtil {
    private static final String DEFAULT_TAG = "ZLogUtil";
    public static boolean D = true;
    public static boolean I = true;
    public static boolean W = true;
    public static boolean E = true;

    public static void d(String tag, String message) {
        if (D)
            Log.d(tag, message);
    }

    public static void d(String message) {
        d(DEFAULT_TAG, message);
    }

    public static void d(String format, Object... args) {
        d(DEFAULT_TAG, buildMessage(format, args));
    }

    public static void d(Context context, String message) {
        String tag = context.getClass().getSimpleName();
        d(tag, message);
    }

    public static void d(Class<?> clazz, String message) {
        String tag = clazz.getSimpleName();
        d(tag, message);
    }

    public static void d(Context context, String format, Object... args) {
        String tag = context.getClass().getSimpleName();
        d(tag, buildMessage(format, args));
    }

    public static void d(Class<?> clazz, String format, Object... args) {
        String tag = clazz.getSimpleName();
        d(tag, buildMessage(format, args));
    }

    public static void i(String tag, String message) {
        if (I)
            Log.i(tag, message);
    }

    public static void i(String message) {
        i(DEFAULT_TAG, message);
    }

    public static void i(String format, Object... args) {
        i(DEFAULT_TAG, buildMessage(format, args));
    }

    public static void i(Context context, String message) {
        String tag = context.getClass().getSimpleName();
        i(tag, message);
    }

    public static void i(Class<?> clazz, String message) {
        String tag = clazz.getSimpleName();
        i(tag, message);
    }

    public static void i(Context context, String format, Object... args) {
        String tag = context.getClass().getSimpleName();
        i(tag, buildMessage(format, args));
    }

    public static void i(Class<?> clazz, String format, Object... args) {
        String tag = clazz.getSimpleName();
        i(tag, buildMessage(format, args));
    }

    public static void w(String tag, String message) {
        if (W)
            Log.e(tag, message);
    }

    public static void w(String message) {
        w(DEFAULT_TAG, message);
    }

    public static void w(String format, Object... args) {
        w(DEFAULT_TAG, buildMessage(format, args));
    }

    public static void w(Context context, String message) {
        String tag = context.getClass().getSimpleName();
        w(tag, message);
    }

    public static void w(Class<?> clazz, String message) {
        String tag = clazz.getSimpleName();
        w(tag, message);
    }

    public static void w(Context context, String format, Object... args) {
        String tag = context.getClass().getSimpleName();
        w(tag, buildMessage(format, args));
    }

    public static void w(Class<?> clazz, String format, Object... args) {
        String tag = clazz.getSimpleName();
        w(tag, buildMessage(format, args));
    }

    public static void e(String tag, String message) {
        if (E)
            Log.e(tag, message);
    }

    public static void e(String message) {
        e(DEFAULT_TAG, message);
    }

    public static void e(String format, Object... args) {
        e(DEFAULT_TAG, buildMessage(format, args));
    }

    public static void e(Context context, String message) {
        String tag = context.getClass().getSimpleName();
        e(tag, message);
    }

    public static void e(Class<?> clazz, String message) {
        String tag = clazz.getSimpleName();
        e(tag, message);
    }

    public static void e(Context context, String format, Object... args) {
        String tag = context.getClass().getSimpleName();
        e(tag, buildMessage(format, args));
    }

    public static void e(Class<?> clazz, String format, Object... args) {
        String tag = clazz.getSimpleName();
        e(tag, buildMessage(format, args));
    }

    public static void logException(Throwable e) {
        if (E) {
            e.printStackTrace();
        }
    }

    public static void debug(boolean d) {
        D = d;
    }

    public static void info(boolean i) {
        I = i;
    }

    public static void warn(boolean w) {
        W = w;
    }

    public static void error(boolean e) {
        E = e;
    }

    public static void setVerbose(boolean d, boolean i, boolean w, boolean e) {
        D = d;
        I = i;
        W = w;
        E = e;
    }

    public static void openAll() {
        D = true;
        I = true;
        W = true;
        E = true;
    }

    public static void closeAll() {
        D = false;
        I = false;
        W = false;
        E = false;
    }

    private static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format,
                args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace()
                .getStackTrace();
        String caller = "<unknown>";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(ZLogUtil.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass
                        .lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass
                        .lastIndexOf('$') + 1);
                caller = callingClass + "." + trace[i].getMethodName();
                caller = callingClass + ":" + trace[i].getLineNumber();
                break;
            }
        }
        return String.format(Locale.US, "[%d] [%s]: %s", Thread.currentThread()
                .getId(), caller, msg);
    }

}
