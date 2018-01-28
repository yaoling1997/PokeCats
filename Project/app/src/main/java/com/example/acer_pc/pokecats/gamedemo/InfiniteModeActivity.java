package com.example.acer_pc.pokecats.gamedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by acer-pc on 2018/1/28.
 */

public class InfiniteModeActivity extends Activity {
    Button btnStart,btnScoreboard;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_infinite_mode);
        bindViews();
    }
    private void bindViews(){
        btnStart = (Button)findViewById(R.id.btnStart);
        btnScoreboard= (Button)findViewById(R.id.btnScoreboard);
        MainActivity.addBtnAnimation(btnStart);
        MainActivity.addBtnAnimation(btnScoreboard);
    }

    public void onClick(View view){
        if (view.getId()==R.id.btnStart){
                Intent intent= new Intent();
                intent.putExtra(Macro.GAME_MODE,0);//0代表无限模式
                intent.setAction(Macro.ACTION_START_ACTIVITY);
                startActivity(intent);
        }else if (view.getId()==R.id.btnScoreboard){
            Intent intent= new Intent();
            intent.setAction(Macro.ACTION_SCOREBOARD_ACTIVITY);
            startActivity(intent);
        }
    }

}
