package com.example.acer_pc.pokecats.gamedemo;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by acer-pc on 2018/1/29.
 */

public class PassAllLevelsActivity extends Activity {
    private ImageView ivPlayWithCats;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pass_all_levels);
        bindViews();
    }
    private void bindViews(){
        ivPlayWithCats= (ImageView)findViewById(R.id.ivPlayWithCats);
        AnimationDrawable ad= (AnimationDrawable)ivPlayWithCats.getBackground();
        ad.start();
    }
}
