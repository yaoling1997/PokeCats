package com.example.acer_pc.pokecats.gamedemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by acer-pc on 2018/1/22.
 */
public class MainActivity extends Activity {
    SharedPreferences prefs;
    Button btnStart,btnSetting,btnScoreboard,btnExit;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        bindViews();
        prefs=getSharedPreferences(Macro.PREFS_FILE,MODE_PRIVATE);

//        SharedPreferences.Editor editor = prefs.edit();//清空用户保存的数据
//        editor.clear();
//        editor.commit();
        Intent intent= new Intent(this,MusicService.class);
        intent.putExtra(Macro.BG_MUSIC,prefs.getString(Macro.BG_MUSIC,Macro.CLOSE));//open or close
        startService(intent);
    }
    private void addBtnAnimation(final Button btn){
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {//返回false是为了能够调用onclick事件
                //Log.i("yaoling1997","This is onTouch: "+motionEvent.getAction());
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    //Log.i("yaoling1997","ACTION_BUTTON_PRESS");
                    //btn.setTextSize(btn.getTextSize()+10);
                    btn.setScaleX((float)1.2);
                    btn.setScaleY((float)1.2);
//                    btn.setWidth(btn.getWidth()+20);
//                    btn.setHeight(btn.getWidth()+80);
                    btn.invalidate();
                    return false;
                }
                if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                    //Log.i("yaoling1997","ACTION_BUTTON_RELEASE");
//                    btn.setTextSize(btn.getTextSize()-10);
                    btn.setScaleX(1);
                    btn.setScaleY(1);
//                    btn.setWidth(btn.getWidth()-20);
//                    btn.setHeight(btn.getHeight()-80);
                    btn.invalidate();
                    return false;
                }

                return false;
            }
        });
    }
    private void bindViews(){
        btnStart= (Button)findViewById(R.id.btnStart);
        btnSetting= (Button)findViewById(R.id.btnSetting);
        btnScoreboard= (Button)findViewById(R.id.btnScoreboard);
        btnExit= (Button)findViewById(R.id.btnExit);
        addBtnAnimation(btnStart);
        addBtnAnimation(btnSetting);
        addBtnAnimation(btnScoreboard);
        addBtnAnimation(btnExit);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent= new Intent(this,MusicService.class);
        stopService(intent);
    }

    public void onClick(View view){
        if (view.getId()==R.id.btnStart){
            Intent intent= new Intent();
            intent.setAction(Macro.ACTION_START_ACTIVITY);
            startActivity(intent);
        }else if (view.getId()==R.id.btnSetting){
            Intent intent= new Intent();
            intent.setAction(Macro.ACTION_SETTING_ACTIVITY);
            startActivity(intent);
        }else if (view.getId()==R.id.btnScoreboard){
            Intent intent= new Intent();
            intent.setAction(Macro.ACTION_SCOREBOARD_ACTIVITY);
            startActivity(intent);
        }else if (view.getId()==R.id.btnExit){
            this.finish();
        }
    }
}
