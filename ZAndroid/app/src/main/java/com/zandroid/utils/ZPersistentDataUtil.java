package com.zandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zandroid.ZAppApplication;

/**
 * Created by prin on 2016/2/16.
 * SharePreference数据持久化的处理
 */
public class ZPersistentDataUtil {

    private static final String mAppPrefix = "zandroid";
    private static ZPersistentDataUtil mPersistentDataUtil;
    private SharedPreferences mSharedPreferences;
    private ZAppApplication mApplication = ZAppApplication.getInstance();

    private ZPersistentDataUtil() {
        mSharedPreferences = mApplication.getSharedPreferences("persistentData", Context.MODE_PRIVATE);
    }

    public static ZPersistentDataUtil getInstance(){
        if (mPersistentDataUtil==null){
            mPersistentDataUtil=new ZPersistentDataUtil();
        }
        return mPersistentDataUtil;
    }

    /**
     * 保存用户名
     */
    public void saveUserName(String userName){
        mSharedPreferences.edit().putString(mAppPrefix+"userName",userName).apply();
    }

    /**
     * 获得用户名
     */
    public void getUserName(){
        mSharedPreferences.getString(mAppPrefix+"userName","");
    }



}
