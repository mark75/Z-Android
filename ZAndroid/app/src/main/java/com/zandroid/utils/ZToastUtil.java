package com.zandroid.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by prin on 2016/2/16.
 * Toast显示工具类
 *
 * 在Application中进行初始化
 *
 */
public class ZToastUtil {

    private static Context mContext;

    public static void init(Context context){
        mContext=context;
    }

    public static void show(int resId) {
        show(mContext, mContext.getResources().getText(resId),
                Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration) {
        show(mContext, mContext.getResources().getText(resId), duration);
    }

    public static void show(int resId, Object... args) {
        show(mContext,
                String.format(mContext.getResources().getString(resId), args),
                Toast.LENGTH_SHORT);
    }

    public static void show(int duration, int resId, Object... args) {
        show(mContext,
                String.format(mContext.getResources().getString(resId), args),
                duration);
    }

    public static void show(CharSequence text) {
        show(mContext, text, Toast.LENGTH_SHORT);
    }

    public static void show(CharSequence text, int duration) {
        show(mContext, text, duration);
    }

    public static void show(String format, Object... args) {
        show(mContext, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(int duration, String format, Object... args) {
        show(mContext, String.format(format, args), duration);
    }

    public static void show(Context context,CharSequence text,int duration){
        if (context!=null){
            Toast.makeText(context,text,duration).show();
        }
    }

}
