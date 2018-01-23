package com.example.acer_pc.pokecats.gamedemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by acer-pc on 2018/1/23.
 */

public class SettingActivity extends Activity {
    Button btnBgMusic,btnSounds;
    SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);

        bindViews();

        btnBgMusic.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                prefs=getSharedPreferences(Macro.PREFS_FILE,MODE_PRIVATE);
                if (prefs.getString(Macro.BG_MUSIC,Macro.CLOSE).equals(Macro.CLOSE)){//close->open
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(Macro.BG_MUSIC,Macro.OPEN);
                    editor.commit();
                    applyPrefs();
                }else {//open->close
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(Macro.BG_MUSIC,Macro.CLOSE);
                    editor.commit();
                    applyPrefs();
                }
            }
        });
    }
    @SuppressLint("NewApi")
    private void bindViews(){
        btnBgMusic= (Button)findViewById(R.id.btnBgMusic);
        btnSounds= (Button)findViewById(R.id.btnSounds);
        applyPrefs();
    }
    @SuppressLint("NewApi")
    private void applyPrefs(){
        prefs=getSharedPreferences(Macro.PREFS_FILE,MODE_PRIVATE);
        Intent intent= new Intent(this,MusicService.class);
        if (prefs.getString(Macro.BG_MUSIC,Macro.CLOSE).equals(Macro.OPEN)) {
            btnBgMusic.setBackgroundDrawable(getDrawable(R.drawable.btn_on));
            intent.putExtra(Macro.BG_MUSIC,prefs.getString(Macro.BG_MUSIC,Macro.OPEN));//open or close
        }else {
            btnBgMusic.setBackgroundDrawable(getDrawable(R.drawable.btn_off));
            intent.putExtra(Macro.BG_MUSIC,prefs.getString(Macro.BG_MUSIC,Macro.CLOSE));//open or close
        }
        btnBgMusic.invalidate();
        startService(intent);
    }
}
