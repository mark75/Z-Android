package com.zandroid.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * Created by prin on 2016/2/15.
 * （1）Service与Task实现对各种后台任务管理
 * （2）注册Receiver实现广播的监听来实现对Task的控制
 *
 */
public class ZAppService extends Service{

    public static final String TAG="ZAppService:";
    private static ZAppService zAppService;
    private static TaskManager mTaskManager;
    private boolean taskInit=false;

    public static ZAppService getInstance(){
        return zAppService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        zAppService=this;
        startTask();

        //系统自带发布的的每分钟系统时间监听广播
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver,filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //非粘性的，如果在onStartCommand后服务被异常kill掉，不会自动重启
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)){
                checkTask();
            }
        }
    };

    //检查任务是否超时  超时重新启动任务
    public void checkTask(){
        if (taskInit){
            mTaskManager.checkAllTasks();
        }
    }

    private void startTask() {
        mTaskManager=new TaskManager();

        //开启任务一
        mTaskManager.addTask(new TaskData(TaskType.Task1,1000,60));

        //开启任务2  每4秒执行一次
        mTaskManager.addTask(new TaskData(TaskType.Task2,4*1000,5));

        taskInit=true;
    }





}
