package com.example.acer_pc.pokecats.gamedemo;

import android.graphics.Bitmap;

/**
 * Created by acer-pc on 2018/1/23.
 */

public class Hole {
    public static final int EMPTY=-1;//洞洞为空
    public static final int BEGIN=0;//喵喵开始钻出
    public static final int POKED=-2;//喵喵被打了
    public static Bitmap []animationOutAndIn;
    public static Bitmap emptyHole;
    public static Bitmap poked;

    private int status=EMPTY;
    private float x,y;

    public Hole(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


    public void init(){
        status=EMPTY;
    }

    public int getStatus() {
        return status;
    }

    public void begin(){//开始钻出
        status= BEGIN;
    }
    public boolean next(){//下一个状态，返回值表示是否掉血
        if (status>=0){
            status++;
            if (status>=animationOutAndIn.length){
                //回到洞洞里，掉血
                status=-1;
                return false;
            }
        }
        if (status==-2){
            status=-1;
        }
        return true;
    }
    public boolean poke(){//敲击喵喵事件
        if (status>=0){
            status=-2;
            return true;
        }
        return false;
    }
}
