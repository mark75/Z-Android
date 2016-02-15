package com.zandroid.service;

import com.zandroid.utils.ZLogUtils;

import java.util.TimerTask;

/**
 * Created by prin on 2016/2/15.
 */
public class Task2 extends TimerTask {

    public static final String Tag="Task2:";
    private TaskData taskData;

    public Task2(TaskData taskData){
        this.taskData=taskData;
    }

    @Override
    public void run() {
        taskData.setLastRunning(System.currentTimeMillis());
        ZLogUtils.i(Tag+Thread.currentThread().getId());
        //子线程中周期性地执行耗时的后台任务
    }
}
