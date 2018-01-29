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
    public StrokeTextView tvLevel,tvHP,tvScore;

    private int level;//玩家选择的关卡，0表示无限模式

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
        if (level>0)
            updateLevel(level);
        timer= new Timer();
        Log.i("yaoling1997","startActivity");
        gameView= new GameView(this);
        Log.i("yaoling1997","before initGame");
        gameView.initGame();
        Log.i("yaoling1997","before afterInitGame");
        layout.addView(gameView);
    }
    private void bindViews(){
        layout= (RelativeLayout)findViewById(R.id.startLayout);
        tvLevel= (StrokeTextView)findViewById(R.id.tvLevel);
        tvHP= (StrokeTextView)findViewById(R.id.tvHP);
        tvScore= (StrokeTextView)findViewById(R.id.tvScore);
        Log.i("yaoling1997","R.id.tvHP:"+R.id.tvHP);
    }
    public int getLevel() {
        return level;
    }
    public void updateLevel(int newLevel){
        Log.i("yaoling1997","updateLevel: "+newLevel);
        level=newLevel;
        tvLevel.mySetText("关卡 "+level+" ");
        if (level>=30){//关卡>=30换背景
            layout.setBackground(getDrawable(R.drawable.start_background2));
            Log.i("yaoling1997","change startBackground");
        }
    }
    @Override
    protected void onDestroy() {
        timer.cancel();
        if (gameView.soundPool!=null)
            gameView.soundPool.release();
        super.onDestroy();
    }
}
