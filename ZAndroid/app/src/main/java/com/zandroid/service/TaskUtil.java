package com.zandroid.service;

import java.util.Timer;

/**
 * Created by prin on 2016/2/15.
 */
public class TaskUtil {

    public static void startTask(TaskData data){
        data.setTimer(new Timer());
        if (data.getType()==TaskType.Task1){
            data.setTask(new Task1(data));
        }else if (data.getType()==TaskType.Task2){
            data.setTask(new Task2(data));
        }

        data.getTimer().schedule(data.getTask(),1000,data.getPeriod());
        data.setLastRunning(System.currentTimeMillis());
    }

    public static void checkTask(TaskData data){
        if (System.currentTimeMillis()-data.getLastRunning()>data.getPeriod()*data.getTimeout()){
            cleanTimerTask(data);
            startTask(data);
        }
    }

    public static void cleanTimerTask(TaskData data)
    {
        if (data.getTask() != null) {
            data.getTask().cancel();
            data.setTask(null);
        }
        if (data.getTimer() != null) {
            data.getTimer().cancel();
            data.getTimer().purge();
            data.setTimer(null);
        }
    }



}
