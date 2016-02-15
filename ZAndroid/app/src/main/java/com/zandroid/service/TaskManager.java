package com.zandroid.service;

import java.util.Vector;

/**
 * Created by prin on 2016/2/15.
 */
public class TaskManager {

    Vector<TaskData> vector=null;

    public TaskManager(){
        vector=new Vector<TaskData>();
    }

    public synchronized void addTask(TaskData task){
        if (vector==null){
            throw new NullPointerException();
        }
        if (!vector.contains(task)){
            vector.add(task);
            startTask(task);
        }
    }

    public synchronized void removeTask(TaskData task){
        vector.removeElement(task);
        clearTask(task);
    }

    public void checkAllTasks(){
        for (int i=vector.size()-1;i>=0;i--){
            TaskUtil.checkTask(vector.elementAt(i));
        }
    }

    public void clearAllTasks(){
        for (int i=vector.size()-1;i>=0;i--){
            removeTask(vector.elementAt(i));
        }
    }

    private void startTask(TaskData task){
        TaskUtil.startTask(task);
    }

    private void clearTask(TaskData task){
        TaskUtil.cleanTimerTask(task);
    }


}
