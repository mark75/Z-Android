package com.zandroid;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.zandroid.service.ZAppService;
import com.zandroid.utils.ZLogUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by prin on 2016/2/15.
 */
public class ZAppApplication extends Application {
    private static final String Tag = "ZAppApplication:";
    private static ZAppApplication application;
    public static Context globalContext;

    public ExecutorService executorService; //使用线程池对Task线程任务进行管理

    @Override
    public void onCreate() {
        super.onCreate();
        ZLogUtils.i(Tag + "onCreate()");
        application = this;
        globalContext = this.getBaseContext();

        executorService = Executors.newScheduledThreadPool(5);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //开启后台服务
                if (ZAppService.getInstance() == null) {
                    startService(new Intent(ZAppApplication.this, ZAppService.class));
                }
            }
        });
    }

    //退出应用
    public void exitApp(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
              //关闭后台服务
                if (ZAppService.getInstance()!=null){
                    ZAppService.getInstance().stopSelf();
                }
            }
        });

        if (application!=null){
            android.os.Process.killProcess(android.os.Process.myPid());
            application=null;
        }
    }


}
