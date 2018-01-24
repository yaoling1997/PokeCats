package com.example.acer_pc.pokecats.gamedemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;

import java.util.Timer;

/**
 * Created by acer-pc on 2018/1/23.
 */

public class StartActivity extends Activity {
    public static Timer timer;
    RelativeLayout layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        timer= new Timer();
        Log.i("yaoling1997","startActivity");
        GameView gameView= new GameView(this);
        Log.i("yaoling1997","before initGame");
        gameView.initGame();
        Log.i("yaoling1997","before afterInitGame");
        layout= (RelativeLayout)findViewById(R.id.startLayout);
        layout.addView(gameView);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
