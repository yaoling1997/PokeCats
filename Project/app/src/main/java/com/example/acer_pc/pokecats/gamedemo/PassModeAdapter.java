package com.example.acer_pc.pokecats.gamedemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

/**
 * Created by acer-pc on 2018/1/28.
 */

public class PassModeAdapter extends BaseAdapter {
    public static final int colNum=3;//每行显示多少关
    private List<Integer> myData;
    private Context myContext;

    public PassModeAdapter(List<Integer> myData,Context myContext) {
        this.myData = myData;
        this.myContext= myContext;
    }

    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private void setBtn(Button btn, final int level){
        MainActivity.addBtnAnimation(btn);
        SharedPreferences prefs= myContext.getSharedPreferences(Macro.PREFS_FILE,Context.MODE_PRIVATE);
        if (prefs.getString(Macro.LEVEL+level,Macro.CLOSE).equals(Macro.OPEN)) {//true代表关卡已解锁
            btn.setText("" + (level));
            btn.setBackground(myContext.getDrawable(R.drawable.pass_mode_unlock));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent();
                    intent.putExtra(Macro.GAME_MODE,level);//0代表无限模式
                    intent.setAction(Macro.ACTION_START_ACTIVITY);
                    myContext.startActivity(intent);
                }
            });
        }else {
            btn.setText("");
            btn.setBackground(myContext.getDrawable(R.drawable.pass_mode_lock));
        }
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int rowId= myData.get(i);
        int level1=rowId*3+1,level2=rowId*3+2,level3=rowId*3+3;
        view= LayoutInflater.from(myContext).inflate(R.layout.pass_mode_adapter,viewGroup,false);
        Button btn1= view.findViewById(R.id.btn1);
        Button btn2= view.findViewById(R.id.btn2);
        Button btn3= view.findViewById(R.id.btn3);
        setBtn(btn1,level1);
        setBtn(btn2,level2);
        setBtn(btn3,level3);
        return view;
    }
}
