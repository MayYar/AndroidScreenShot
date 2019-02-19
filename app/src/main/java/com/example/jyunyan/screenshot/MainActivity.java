package com.example.jyunyan.screenshot;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MainActivity";
    public String fileName = "NA";

    private Button shot, stop;
    File saveFile = getMainDirectoryName();
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    static MainActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screenshot);

        shot = (Button)findViewById(R.id.btn_shot);
        stop = (Button)findViewById(R.id.btn_stop);
        isStoragePermissionGranted();

        shot.setOnClickListener(doClick);
        stop.setOnClickListener(doClick);
        instance = this;
//        Intent myIntent = new Intent(getBaseContext(),BackgroundService.class);
//        startService(myIntent);
//        Intent intent = new Intent(getBaseContext(), BackgroundService.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("Main", this);
//        intent.putExtras(bundle);
//        startService(intent);
//        startTimer();

    }


    private Button.OnClickListener doClick = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_shot) {
                Log.e(LOG_TAG, "Click ScreenShot");

                startService(new Intent(MainActivity.this, BackgroundService.class));
//                startTimer();
            } else if (view.getId() == R.id.btn_stop) {
                Log.e(LOG_TAG, "Stop the process");
                stopService(new Intent(MainActivity.this, BackgroundService.class));
//                stopTimer();
            }
        }
    };

    public void startTimer(){
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

                    String str = formatter.format(curDate);
                    fileName = str.replaceAll("\\D+", "");
//                Log.e(LOG_TAG, "Time: " + fileName + ".jpg");

//                    Bitmap b = takeScreenShot(getWindow().getDecorView().findViewById(android.R.id.content));  //instant Screen view
                    Bitmap b = takeScreenShot(getWindow().getDecorView());  //instant Screen view

                    if (b != null) {

//                        File saveFile = getMainDirectoryName();//get the path to save screenshot

                        store(b, fileName + ".jpg", saveFile);//save the screenshot to selected path
                        Log.e(LOG_TAG, "Save " + fileName + ".jpg into" + saveFile);

                    } else
                        //If bitmap is null show toast message
                        Toast.makeText(MainActivity.this, "screenshot_take_failed", Toast.LENGTH_SHORT).show();
                }
            };
        }
        if(mTimer != null && mTimerTask != null )
            mTimer.schedule(mTimerTask, 0, 5000);


    }

    private void stopTimer(){

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

    }


    public static Bitmap takeScreenShot(View v){
        View screenView = v.getRootView();  //Finds the topmost view in the current view hierarchy.

        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;

    }

    /*  Create Directory where screenshot will save for sharing screenshot  */
    public static File getMainDirectoryName() {
        //Here we will use getExternalFilesDir and inside that we will make our Demo folder
        //benefit of getExternalFilesDir is that whenever the app uninstalls the images will get deleted automatically.
        File mainDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Demo");

        Log.e(LOG_TAG, "Demo File is presented at " + mainDir );
//        mainDir.mkdirs();
//        //If File is not present create directory
        if (!mainDir.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return mainDir;
    }

    /*  Store taken screenshot into above created path  */
    public static void store(Bitmap bm, String fileName, File saveFilePath) {

        File file = new File(saveFilePath.getAbsolutePath(), fileName);

        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Bitmap takeScreenShot2(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        //Find the screen dimensions to create bitmap in the same size.
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }




    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG_TAG,"Permission is granted");
                return true;
            } else {

                Log.v(LOG_TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOG_TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(LOG_TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

}
