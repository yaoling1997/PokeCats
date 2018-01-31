package com.example.acer_pc.pokecats.gamedemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by acer-pc on 2018/1/22.
 */
public class MainActivity extends Activity {
    SharedPreferences prefs;
    RelativeLayout mainLayout;
    Button btnPassMode,btnInfiniteMode,btnSetting,btnIntroduction,btnExit;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        bindViews();

        ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        Log.i("yaoling1997","最大内存：" + activityManager.getMemoryClass());
        prefs=getSharedPreferences(Macro.PREFS_FILE,MODE_PRIVATE);

//        SharedPreferences.Editor editor = prefs.edit();//清空用户保存的数据
//        editor.clear();
//        editor.commit();

        unlockLevel();//解锁关卡
        initBackgroundMusicAndSoundsPrefs();//第一次玩，默认音效和背景音乐打开


        Intent intent= new Intent(this,MusicService.class);
        intent.putExtra(Macro.BG_MUSIC,prefs.getString(Macro.BG_MUSIC,Macro.CLOSE));//open or close
        startService(intent);
    }
    void initBackgroundMusicAndSoundsPrefs(){
        if (prefs.getString(Macro.BG_MUSIC,"").equals("")&&prefs.getString(Macro.SOUNDS,"").equals("")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Macro.BG_MUSIC, Macro.OPEN);
            editor.putString(Macro.SOUNDS, Macro.OPEN);
            editor.commit();
        }
    }
    private void unlockLevel(){
        SharedPreferences.Editor editor = prefs.edit();
        for (int i=1;i<=1;i++){
            editor.putString(Macro.LEVEL+i,Macro.OPEN);
        }
        editor.commit();
    }
    public static void addBtnAnimation(final Button btn){
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {//返回false是为了能够调用onclick事件
                //Log.i("yaoling1997","This is onTouch: "+motionEvent.getAction());
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    btn.setScaleX((float)0.8);
                    btn.setScaleY((float)0.8);
                    btn.invalidate();
                    return false;
                }
                if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                    btn.setScaleX(1);
                    btn.setScaleY(1);
                    btn.invalidate();
                    return false;
                }

                return false;
            }
        });
    }

    private void bindViews(){
        mainLayout= (RelativeLayout)findViewById(R.id.mainLayout);
        btnPassMode = (Button)findViewById(R.id.btnPassMode);
        btnInfiniteMode = (Button)findViewById(R.id.btnInfiniteMode);
        btnSetting= (Button)findViewById(R.id.btnSetting);
        btnIntroduction= (Button)findViewById(R.id.btnIntroduction);
        btnExit= (Button)findViewById(R.id.btnExit);
        addBtnAnimation(btnPassMode);
        addBtnAnimation(btnInfiniteMode);
        addBtnAnimation(btnSetting);
        addBtnAnimation(btnIntroduction);
        addBtnAnimation(btnExit);
        //mainLayout.setBackground(getDrawable(R.drawable.background2));
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent= new Intent(this,MusicService.class);
        stopService(intent);
    }

    public void onClick(View view){
        if (view.getId()==R.id.btnPassMode){
            Intent intent= new Intent();
            intent.setAction(Macro.ACTION_PASS_MODE_ACTIVITY);
            startActivity(intent);
        }else if (view.getId()==R.id.btnInfiniteMode){
            Intent intent= new Intent();
            intent.setAction(Macro.ACTION_INFINITE_MODE_ACTIVITY);
            startActivity(intent);
        }else if (view.getId()==R.id.btnSetting){
            Intent intent= new Intent();
            intent.setAction(Macro.ACTION_SETTING_ACTIVITY);
            startActivity(intent);
        }else if (view.getId()==R.id.btnIntroduction){
            Intent intent= new Intent();
            intent.setAction(Macro.ACTION_INTRODUCTION_ACTIVITY);
            startActivity(intent);
        }else if (view.getId()==R.id.btnExit){
            this.finish();
        }
    }
}
