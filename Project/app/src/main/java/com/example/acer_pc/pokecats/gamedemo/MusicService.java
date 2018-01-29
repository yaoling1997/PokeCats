package com.example.acer_pc.pokecats.gamedemo;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by acer-pc on 2018/1/22.
 */

public class MusicService extends Service {
    MediaPlayer player=new MediaPlayer();

    @Override
    public void onCreate() {
        Log.i("yaoling1997","This is onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null) {
            String bgMusic = intent.getStringExtra(Macro.BG_MUSIC);
            if (bgMusic.equals(Macro.OPEN)) {
                play();
            } else {
                stop();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void play(){
        Log.i("yaoling1997","This is play");
        try {
            AssetFileDescriptor fileDescriptor=getAssets().openFd("backgroundMusic.mp3");
            player.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            player.setLooping(true);
            player.prepare();
            player.start();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    private void stop(){
        player.stop();
        player.reset();
    }
    @Override
    public void onDestroy() {
        stop();
        Log.i("yaoling1997","This is MusicService's onDestroy");
        super.onDestroy();
    }
}
