package com.example.jyunyan.screenshot;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.jyunyan.screenshot.MainActivity.getMainDirectoryName;
import static com.example.jyunyan.screenshot.MainActivity.store;
import static com.example.jyunyan.screenshot.MainActivity.takeScreenShot;
import static com.example.jyunyan.screenshot.MainActivity.takeScreenShot2;

public class BackgroundService extends Service {

    public final String TAG = "BackgroundService";
    public String fileName = "NA";

    File saveFile = getMainDirectoryName();
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MainActivity activity = MainActivity.instance;

        if(activity != null)
            activity.startTimer();
        else
            Log.e(TAG,"instance is null");

        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        stopTimer();
    }

//    public void startTimer(){
//        if (mTimer == null) {
//            mTimer = new Timer();
//        }
//
//        if (mTimerTask == null) {
//            mTimerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
//
//                    String str = formatter.format(curDate);
//                    fileName = str.replaceAll("\\D+", "");
////                Log.e(LOG_TAG, "Time: " + fileName + ".jpg");
//
//
//
////
//                    Bitmap b = takeScreenShot(((Activity)mContext).getWindow().getDecorView());  //instant Screen view
////
//                    if (b != null) {
//
////                        File saveFile = getMainDirectoryName();//get the path to save screenshot
//
//                        store(b, fileName + ".jpg", saveFile);//save the screenshot to selected path
//                        Log.e(TAG, "Save " + fileName + ".jpg");
//
//                    } else
//                        //If bitmap is null show toast message
//                        Toast.makeText(BackgroundService.this, "screenshot_take_failed", Toast.LENGTH_SHORT).show();
//                }
//            };
//        }
//        if(mTimer != null && mTimerTask != null )
//            mTimer.schedule(mTimerTask, 0, 5000);
//
//
//    }

//    private void stopTimer(){
//
//        if (mTimer != null) {
//            mTimer.cancel();
//            mTimer = null;
//        }
//
//        if (mTimerTask != null) {
//            mTimerTask.cancel();
//            mTimerTask = null;
//        }
//
//    }
}
