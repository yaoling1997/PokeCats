package com.example.acer_pc.pokecats.gamedemo;

import android.graphics.Bitmap;

/**
 * Created by acer-pc on 2018/1/23.
 */

public class Hole {
    public static final int BEGIN=0;//喵喵开始钻出
    public static final int EMPTY=-1;//洞洞为空
    public static final int POKED=-4;//喵喵被打了，(EMPTY-POKED)*period为敲击画面持续的时间
    public static final int INIT_ID=-2;//初始id
    public static Bitmap [][]animationOutAndIn;//喵喵的进出动画
    public static Bitmap []animationBomb;//炸弹的动画（）
    public static Bitmap poked;//poked的图片
    public static Bitmap stubBack;//整个树桩
    public static Bitmap stubFront;//只保留树桩的前部分
    public static int preId=INIT_ID;//前一个被敲的东东的id
    public static int sameNum=0;//连续敲击同一种东东多少次

    private float x,y;//洞洞左上角坐标
    private int status=EMPTY;//洞洞的状态
    private int id=0;//喵喵或炸弹的id

    public Hole(float x, float y) {
        this.x = x;
        this.y = y;
        status=EMPTY;
        id=0;//0,1,2代表喵，-1代表炸弹
        preId=INIT_ID;
        sameNum=0;
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
    public boolean next(){//下一个状态，返回值表示为false表示掉血
        if (status>=BEGIN){
            status++;
            if (id>=0) {//是喵喵
                if (status >= animationOutAndIn[id].length) {
                    //回到洞洞里，掉血
                    status = EMPTY;
                    return false;
                }
            }else {//是炸弹
                if (status>=animationBomb.length){
                    //回到洞洞里，不掉血
                    status = EMPTY;
                    return true;
                }
            }
        }
        if (status<EMPTY){
            status++;
        }
        return true;
    }
    public boolean poke(){//敲击事件，true表示敲击到东西了
        if (status>=BEGIN){
            status=POKED;
            if (preId==id){
                sameNum++;
            }else {
                sameNum=1;
                preId=id;
            }
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

}
