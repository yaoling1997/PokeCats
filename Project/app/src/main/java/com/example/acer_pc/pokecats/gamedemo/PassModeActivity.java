package com.example.acer_pc.pokecats.gamedemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by acer-pc on 2018/1/28.
 */

public class PassModeActivity extends Activity {
    private ListView lvPassMode;
    private List<Integer> myData;
    private PassModeAdapter passModeAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pass_mode);
        bindViews();
    }

    @Override
    protected void onRestart() {
        getPassModeAdapter();
        lvPassMode.setAdapter(passModeAdapter);
        super.onRestart();
    }

    private void getPassModeAdapter(){
        myData= new LinkedList<Integer>();
        for (int i=0;i<Macro.LEVEL_NUM/PassModeAdapter.colNum;i++)
            myData.add(i);
        passModeAdapter= new PassModeAdapter(myData,this);
    }
    private void bindViews(){
        lvPassMode= (ListView)findViewById(R.id.lvPassMode);
        getPassModeAdapter();
        lvPassMode.setAdapter(passModeAdapter);
    }
}
