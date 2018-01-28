package com.example.acer_pc.pokecats.gamedemo;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;

import java.util.Timer;

/**
 * Created by acer-pc on 2018/1/23.
 */

public class StartActivity extends Activity {
    public static Timer timer;
    private RelativeLayout layout;
    private GameView gameView;
    public StrokeTextView tvHP,tvScore;
    public int level;//玩家选择的关卡，0表示无限模式

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        bindViews();
        level=getIntent().getIntExtra(Macro.GAME_MODE,-1);
        Log.i("yaoling1997","StartActivity,level: "+level);
        if (level<0)
            finish();
        timer= new Timer();
        Log.i("yaoling1997","startActivity");
        gameView= new GameView(this);
        Log.i("yaoling1997","before initGame");
        gameView.initGame();
        Log.i("yaoling1997","before afterInitGame");
        layout= (RelativeLayout)findViewById(R.id.startLayout);
        layout.addView(gameView);
    }
    private void bindViews(){
        tvHP= (StrokeTextView)findViewById(R.id.tvHP);
        tvScore= (StrokeTextView)findViewById(R.id.tvScore);
        Log.i("yaoling1997","R.id.tvHP:"+R.id.tvHP);
    }
    @Override
    protected void onDestroy() {
        timer.cancel();
        if (gameView.soundPool!=null)
            gameView.soundPool.release();
        super.onDestroy();
    }
}
