package com.example.acer_pc.pokecats.gamedemo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

/**
 * Created by acer-pc on 2018/1/24.
 */

public class ScoreboardActivity extends Activity {//查看积分界面
    public static final int NoNum=3;
    SharedPreferences prefs;
    StrokeTextView []tvNo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scoreboard);
        bindViews();
    }
    private void bindViews(){
        tvNo= new StrokeTextView[NoNum];
        tvNo[0]=(StrokeTextView)findViewById(R.id.tvNo1);
        tvNo[1]=(StrokeTextView)findViewById(R.id.tvNo2);
        tvNo[2]=(StrokeTextView)findViewById(R.id.tvNo3);
        prefs=getSharedPreferences(Macro.PREFS_FILE,MODE_PRIVATE);
        for (int i=0;i<NoNum;i++) {
            int score = prefs.getInt(Macro.NO[i], 0);
            tvNo[i].setText(Macro.NO[i] + ":  " + score);
        }
    }

}
