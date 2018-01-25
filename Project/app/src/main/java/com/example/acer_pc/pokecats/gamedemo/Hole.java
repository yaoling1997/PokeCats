package com.example.acer_pc.pokecats.gamedemo;

import android.graphics.Bitmap;

/**
 * Created by acer-pc on 2018/1/23.
 */

public class Hole {
    public static final int BEGIN=0;//喵喵开始钻出
    public static final int EMPTY=-1;//洞洞为空
    public static final int POKED=-4;//喵喵被打了，(EMPTY-POKED)*period为敲击画面持续的时间
    public static Bitmap [][]animationOutAndIn;
    public static Bitmap poked;//poked的图片
    public static Bitmap stubBack;//整个树桩
    public static Bitmap stubFront;//只保留树桩的前部分

    private int status=EMPTY;//洞洞的状态
    private int id=0;//喵喵的id
    private float x,y;

    public Hole(float x, float y) {
        this.x = x;
        this.y = y;
        status=EMPTY;
        id=0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


    public int getStatus() {
        return status;
    }

    public void begin(int id){//开始钻出
        status= BEGIN;
        this.id=id;
    }
    public boolean next(){//下一个状态，返回值表示是否掉血
        if (status>=BEGIN){
            status++;
            if (status>=animationOutAndIn[id].length){
                //回到洞洞里，掉血
                status=EMPTY;
                return false;
            }
        }
        if (status<EMPTY){
            status++;
        }
        return true;
    }
    public boolean poke(){//敲击喵喵事件
        if (status>=BEGIN){
            status=POKED;
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }
}
