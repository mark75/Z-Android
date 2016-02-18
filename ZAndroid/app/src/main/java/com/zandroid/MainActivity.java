package com.zandroid;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zandroid.utils.ZLogUtil;
import com.zandroid.utils.ZToastUtil;

public class MainActivity extends Activity {

    public static final String Tag = "MainActivity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().getDecorView().post(new Runnable() {
//            @Override
//            public void run() {
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                ZLogUtil.i(Tag + width + "===" + height);
//            }
//        });

        //获得App各种缓存文件
//        getAndroidCacheFile();

        //网络框架使用

        //Handler Looper MessageQueue
//        doHandler();

        //使用PopupWindow
        usePopupWindow();
    }

    private void usePopupWindow() {
        initPopupWindow();
    }

    private PopupWindow mPopupWindow;
    private Button mShowPopBt, mPopBt1, mPopBt2;

    private void initPopupWindow() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_button, null);
        mPopupWindow = new PopupWindow(popView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置出现的动画
//        mPopupWindow.setAnimationStyle(R.style.pop);
        mShowPopBt = (Button) findViewById(R.id.button3);
        mShowPopBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(mShowPopBt, 0, 0, 0);
            }
        });

        initPopupView(popView);
    }

    /**
     * 显示PopupWindow
     */
    public void showPop(View parent, int x, int y, int position) {
//        mPopupWindow.showAsDropDown(parent);
        //获取popwindow焦点
        mPopupWindow.setFocusable(true);
        //设置PopupWindow如果点击外面区域  便关闭
        mPopupWindow.setOutsideTouchable(true);

        int[] location = new int[2];
        parent.getLocationOnScreen(location);
//        //在pop点右边显示
//        mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY,location[0]+parent.getWidth(),location[1]);
        //在pop点左边显示
        mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - parent.getWidth(), 0);
    }

    private void initPopupView(View popView) {
        mPopBt1 = (Button) popView.findViewById(R.id.button);
        mPopBt2 = (Button) popView.findViewById(R.id.button2);

        mPopBt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZToastUtil.show("点击Pop1");
            }
        });

        mPopBt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZToastUtil.show("点击Pop2");
            }
        });

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    private Handler threadHandler;

    private void doHandler() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //想要在子线程中执行耗时任务，是一种轮询机制 则需要在子线程中仍然调用Looper循环
                Looper.prepare();
                threadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 1) {

                            threadHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //执行耗时任务
                                }
                            }, 1000);
                        }
                    }
                };
                threadHandler.sendEmptyMessage(1000);
                Looper.loop();

            }
        }).start();

    }

    private void getAndroidCacheFile() {
        //Internal Storage 内部存储
        String isCacheName = getCacheDir().getAbsolutePath();
        String isFileName = getFilesDir().getAbsolutePath();

        //External Storage 外部存储
        String esCacheName = getExternalCacheDir().getAbsolutePath();
        String esFileName = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        String esPublicName = Environment.getExternalStorageDirectory().getAbsolutePath();
        String esPublicTypeName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getAbsolutePath();

        ZLogUtil.i(Tag + "isCacheName" + isCacheName);
        ZLogUtil.i(Tag + "isFileName" + isFileName);
        ZLogUtil.i(Tag + "esCacheName" + esCacheName);
        ZLogUtil.i(Tag + "esFileName" + esFileName);
        ZLogUtil.i(Tag + "esPublicName" + esPublicName);
        ZLogUtil.i(Tag + "esPublicTypeName" + esPublicTypeName);

    }


}
