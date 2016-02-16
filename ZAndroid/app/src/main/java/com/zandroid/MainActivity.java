package com.zandroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.zandroid.utils.ZLogUtil;

public class MainActivity extends Activity {

    public static final String Tag="MainActivity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获得App各种缓存文件
        getAndroidCacheFile();
    }

    private void getAndroidCacheFile() {
        //Internal Storage 内部存储
        String isCacheName=getCacheDir().getAbsolutePath();
        String isFileName=getFilesDir().getAbsolutePath();

        //External Storage 外部存储
        String esCacheName=getExternalCacheDir().getAbsolutePath();
        String esFileName=getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        String esPublicName=Environment.getExternalStorageDirectory().getAbsolutePath();
        String esPublicTypeName=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getAbsolutePath();

        ZLogUtil.i(Tag + "isCacheName" + isCacheName);
        ZLogUtil.i(Tag + "isFileName" + isFileName);
        ZLogUtil.i(Tag + "esCacheName" + esCacheName);
        ZLogUtil.i(Tag + "esFileName" + esFileName);
        ZLogUtil.i(Tag + "esPublicName" + esPublicName);
        ZLogUtil.i(Tag + "esPublicTypeName" + esPublicTypeName);

    }


}
