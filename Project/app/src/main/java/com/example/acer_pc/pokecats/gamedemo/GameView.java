package com.example.acer_pc.pokecats.gamedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.TimerTask;

/**
 * Created by acer-pc on 2018/1/23.
 */

public class GameView extends View {
    private static final int INIT_HP=5;//初始HP
    private static final int INIT_SCORE=0;//初始score
    private static final int catAuthorId=3;//代表作者的喵的id
    private static final int []bonus={1,3,5,10};//每打到一只喵喵增加的分数
    private static final int gridLength=380;//一个格子的长宽
    private static final int catKindNum=4;//喵喵种类
    private static final int putCatDelay =10;//延迟多久开始放喵，延迟时长为putCatDelay*putCatPeriod
    private static final int putCatPeriod =300;//放喵的周期
    private static final int framePeriod =100;//相邻两帧动画的时间间隔
    private static final int infiniteModeRowNum=3;//无限模式行数
    private static final int infiniteModeColNum=3;//无限模式列数
    private static final int []passScore={
            0,
            100,
            100,
            100,
            100,
            100,
            200,
            200,
            200,
            200,
            200,
            300,
            300,
            300,
            300,
            300,
            400,
            400,
            400,
            400,
            400,
            500,
            500,
            600,
            600,
            700,
            700,
            800,
            800,
            900,
            1000//level30
    };
//    private static final int []passScore={
//            0,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100,
//            100//level30
//    };

    private int rowNum=0;//多少行
    private int colNum=0;//多少列
    private StartActivity myContext;
    private int HP=INIT_HP;//生命值
    private int score=INIT_SCORE;//得分
    private boolean isUpdateing=false;
    private Random random= new Random();
    private Hole [][]holes;
    private boolean isOver=false;
    public SoundPool soundPool=null;
    private int catPokedSoundId =-1;
    private int bombPokedSoundId =-1;
    private int []catSoundId;
    private int restPutCatDelay=0;//还剩多久时间放喵

    private final Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x123&&!isUpdateing&&!isOver){//更新画布
                invalidate();
            }
            super.handleMessage(msg);
        }
    };//处理事件
    public class MyPoint{
        public float x,y;

        public MyPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public GameView(Context context) {
        super(context);
        myContext= (StartActivity)context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initSounds(){
        catPokedSoundId =-1;
        catSoundId=new int[catKindNum];
        for (int i=0;i<catKindNum;i++)
            catSoundId[i]=-1;
        SharedPreferences prefs= getContext().getSharedPreferences(Macro.PREFS_FILE,Context.MODE_PRIVATE);
        if (prefs.getString(Macro.SOUNDS,Macro.CLOSE).equals(Macro.OPEN)) {
            SoundPool.Builder spb = new SoundPool.Builder();
            spb.setMaxStreams(100);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            spb.setAudioAttributes(attrBuilder.build());    //转换音频格式
            soundPool = spb.build();      //创建SoundPool对象
            catPokedSoundId = soundPool.load(getContext(), R.raw.duang, 1);
            bombPokedSoundId= soundPool.load(getContext(),R.raw.boom,1);
            catSoundId[0] = soundPool.load(getContext(), R.raw.cat1, 1);
            catSoundId[1] = soundPool.load(getContext(), R.raw.cat2, 1);
            catSoundId[2] = soundPool.load(getContext(), R.raw.cat3, 1);
            catSoundId[3] = soundPool.load(getContext(), R.raw.cat4, 1);
        }
    }

    private void initAnimation0(){
        Hole.animationOutAndIn[0]= new Bitmap[20];
        Hole.animationOutAndIn[0][0]=getBitmap(R.drawable.cat1_1,1*gridLength/6);//1_1
        Hole.animationOutAndIn[0][1]=getBitmap(R.drawable.cat1_2,2*gridLength/6);//2_2
        Hole.animationOutAndIn[0][2]=getBitmap(R.drawable.cat1_3,3*gridLength/6);//3_3
        Hole.animationOutAndIn[0][3]=getBitmap(R.drawable.cat1_4,4*gridLength/6);//4_4
        Hole.animationOutAndIn[0][4]=getBitmap(R.drawable.cat1_5,5*gridLength/6);//5_5
        Hole.animationOutAndIn[0][5]=getBitmap(R.drawable.cat1_1,6*gridLength/6);//1_6
        Hole.animationOutAndIn[0][6]=getBitmap(R.drawable.cat1_2,6*gridLength/6);//2_6
        Hole.animationOutAndIn[0][7]=getBitmap(R.drawable.cat1_3,6*gridLength/6);//3_6
        Hole.animationOutAndIn[0][8]=getBitmap(R.drawable.cat1_4,6*gridLength/6);//4_6
        Hole.animationOutAndIn[0][9]=getBitmap(R.drawable.cat1_5,6*gridLength/6);//5_6
        Hole.animationOutAndIn[0][10]=Hole.animationOutAndIn[0][5];//1_6
        Hole.animationOutAndIn[0][11]=Hole.animationOutAndIn[0][6];//2_6
        Hole.animationOutAndIn[0][12]=Hole.animationOutAndIn[0][7];//3_6
        Hole.animationOutAndIn[0][13]=Hole.animationOutAndIn[0][8];//4_6
        Hole.animationOutAndIn[0][14]=Hole.animationOutAndIn[0][9];//5_6
        Hole.animationOutAndIn[0][15]=getBitmap(R.drawable.cat1_1,5*gridLength/6);//1_5
        Hole.animationOutAndIn[0][16]=getBitmap(R.drawable.cat1_2,4*gridLength/6);//2_4
        Hole.animationOutAndIn[0][17]=Hole.animationOutAndIn[0][2];//3_3
        Hole.animationOutAndIn[0][18]=getBitmap(R.drawable.cat1_4,2*gridLength/6);//4_2
        Hole.animationOutAndIn[0][19]=getBitmap(R.drawable.cat1_5,1*gridLength/6);//5_1
    }
    private void initAnimation1(){
        Hole.animationOutAndIn[1]= new Bitmap[16];
        Hole.animationOutAndIn[1][0]=getBitmap(R.drawable.cat2_1,1*gridLength/6);//1_1
        Hole.animationOutAndIn[1][1]=getBitmap(R.drawable.cat2_2,2*gridLength/6);//2_2
        Hole.animationOutAndIn[1][2]=getBitmap(R.drawable.cat2_3,3*gridLength/6);//3_3
        Hole.animationOutAndIn[1][3]=getBitmap(R.drawable.cat2_4,6*gridLength/6);//4_6
        Hole.animationOutAndIn[1][4]=getBitmap(R.drawable.cat2_1,6*gridLength/6);//1_6
        Hole.animationOutAndIn[1][5]=getBitmap(R.drawable.cat2_2,6*gridLength/6);//2_6
        Hole.animationOutAndIn[1][6]=getBitmap(R.drawable.cat2_3,6*gridLength/6);//3_6
        Hole.animationOutAndIn[1][7]=Hole.animationOutAndIn[1][3];//4_6
        Hole.animationOutAndIn[1][8]=Hole.animationOutAndIn[1][4];//1_6
        Hole.animationOutAndIn[1][9]=Hole.animationOutAndIn[1][5];//2_6
        Hole.animationOutAndIn[1][10]=Hole.animationOutAndIn[1][6];//3_6
        Hole.animationOutAndIn[1][11]=Hole.animationOutAndIn[1][3];//4_6
        Hole.animationOutAndIn[1][12]=Hole.animationOutAndIn[1][4];//1_6
        Hole.animationOutAndIn[1][13]=getBitmap(R.drawable.cat2_2,3*gridLength/6);//2_3
        Hole.animationOutAndIn[1][14]=getBitmap(R.drawable.cat2_3,2*gridLength/6);//3_2
        Hole.animationOutAndIn[1][15]=getBitmap(R.drawable.cat2_4,1*gridLength/6);//4_1
    }
    private void initAnimation2(){
        Hole.animationOutAndIn[2]= new Bitmap[10];
        Hole.animationOutAndIn[2][0]=getBitmap(R.drawable.cat3_1,1*gridLength/6);//1_1
        Hole.animationOutAndIn[2][1]=getBitmap(R.drawable.cat3_2,2*gridLength/6);//2_2
        Hole.animationOutAndIn[2][2]=getBitmap(R.drawable.cat3_3,3*gridLength/6);//3_3
        Hole.animationOutAndIn[2][3]=getBitmap(R.drawable.cat3_4,6*gridLength/6);//4_6
        Hole.animationOutAndIn[2][4]=getBitmap(R.drawable.cat3_5,6*gridLength/6);//5_6
        Hole.animationOutAndIn[2][5]=Hole.animationOutAndIn[2][3];//4_6
        Hole.animationOutAndIn[2][6]=Hole.animationOutAndIn[2][4];//5_6
        Hole.animationOutAndIn[2][7]=getBitmap(R.drawable.cat3_6,3*gridLength/6);//6_3
        Hole.animationOutAndIn[2][8]=getBitmap(R.drawable.cat3_7,2*gridLength/6);//7_2
        Hole.animationOutAndIn[2][9]=getBitmap(R.drawable.cat3_8,1*gridLength/6);//8_1
    }
    private void initAnimation3(){
        Hole.animationOutAndIn[3]= new Bitmap[20];
        Hole.animationOutAndIn[3][0]=getBitmap(R.drawable.cat4_1,1*gridLength/6);//1_1
        Hole.animationOutAndIn[3][1]=getBitmap(R.drawable.cat4_1,2*gridLength/6);//1_2
        Hole.animationOutAndIn[3][2]=getBitmap(R.drawable.cat4_1,3*gridLength/6);//1_3
        Hole.animationOutAndIn[3][3]=getBitmap(R.drawable.cat4_1,4*gridLength/6);//1_4
        Hole.animationOutAndIn[3][4]=getBitmap(R.drawable.cat4_1,5*gridLength/6);//1_5
        Hole.animationOutAndIn[3][5]=getBitmap(R.drawable.cat4_1,6*gridLength/6);//1_6
        Hole.animationOutAndIn[3][6]=getBitmap(R.drawable.cat4_2,6*gridLength/6);//2_6
        Hole.animationOutAndIn[3][7]=Hole.animationOutAndIn[3][6];//2_6
        Hole.animationOutAndIn[3][8]=Hole.animationOutAndIn[3][5];//1_6
        Hole.animationOutAndIn[3][9]=Hole.animationOutAndIn[3][5];//1_6
        Hole.animationOutAndIn[3][10]=Hole.animationOutAndIn[3][6];//2_6
        Hole.animationOutAndIn[3][11]=Hole.animationOutAndIn[3][6];//2_6
        Hole.animationOutAndIn[3][12]=Hole.animationOutAndIn[3][5];//1_6
        Hole.animationOutAndIn[3][13]=Hole.animationOutAndIn[3][5];//1_6
        Hole.animationOutAndIn[3][14]=Hole.animationOutAndIn[3][5];//1_6
        Hole.animationOutAndIn[3][15]=Hole.animationOutAndIn[3][4];//1_5
        Hole.animationOutAndIn[3][16]=Hole.animationOutAndIn[3][3];//1_4
        Hole.animationOutAndIn[3][17]=Hole.animationOutAndIn[3][2];//1_3
        Hole.animationOutAndIn[3][18]=Hole.animationOutAndIn[3][1];//1_2
        Hole.animationOutAndIn[3][19]=Hole.animationOutAndIn[3][0];//1_1
    }
    private void initAnimationBomb(){
        Hole.animationBomb= new Bitmap[20];
        Hole.animationBomb[0]=getBitmap(R.drawable.bomb,1*gridLength/6);//0_1
        Hole.animationBomb[1]=getBitmap(R.drawable.bomb,2*gridLength/6);//0_2
        Hole.animationBomb[2]=getBitmap(R.drawable.bomb,3*gridLength/6);//0_3
        Hole.animationBomb[3]=getBitmap(R.drawable.bomb,5*gridLength/6);//0_5
        Hole.animationBomb[4]=Hole.animationBomb[3];//0_5
        Hole.animationBomb[5]=Hole.animationBomb[3];//0_5
        Hole.animationBomb[6]=Hole.animationBomb[3];//0_5
        Hole.animationBomb[7]=Hole.animationBomb[3];//0_5
        Hole.animationBomb[8]=getBitmap(R.drawable.bomb,4*gridLength/6);//0_4
        Hole.animationBomb[9]=Hole.animationBomb[2];//0_3
        Hole.animationBomb[10]=Hole.animationBomb[2];//0_3
        Hole.animationBomb[11]=Hole.animationBomb[8];//0_4
        Hole.animationBomb[12]=Hole.animationBomb[3];//0_5
        Hole.animationBomb[13]=Hole.animationBomb[3];//0_5
        Hole.animationBomb[14]=Hole.animationBomb[3];//0_5
        Hole.animationBomb[15]=Hole.animationBomb[3];//0_5
        Hole.animationBomb[16]=Hole.animationBomb[3];//0_5
        Hole.animationBomb[17]=Hole.animationBomb[2];//0_3
        Hole.animationBomb[18]=Hole.animationBomb[1];//0_2
        Hole.animationBomb[19]=Hole.animationBomb[0];//0_1
    }
    private void initAnimation(){
        Hole.animationOutAndIn= new Bitmap[catKindNum][];
        initAnimation0();
        initAnimation1();
        initAnimation2();
        initAnimation3();
        initAnimationBomb();
    }
    private void initMatrixLayout(){//矩阵型的布局
        holes= new Hole[rowNum][colNum];
        WindowManager wm= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int gridSpace= 0;//格子间的间隔
        int firstX=(wm.getDefaultDisplay().getWidth()-gridSpace*(colNum-1)-colNum*gridLength)/2;
        int firstY= 50;//第一个格子的Y
        firstY= (wm.getDefaultDisplay().getHeight()-rowNum*gridLength)/2+firstY;
        for (int i=0;i<rowNum;i++)
            for (int j=0;j<colNum;j++)
                holes[i][j]=new Hole(firstX+j*(gridLength+gridSpace),firstY+i*(gridLength-50));
    }
    private void initRandomLayout(){//随机布局
        holes= new Hole[rowNum][colNum];
        WindowManager wm= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width= wm.getDefaultDisplay().getWidth();//屏幕宽
        int height= wm.getDefaultDisplay().getHeight();//屏幕高
        int gridSpace= 10;//格子间的间隔
        int firstX= gridSpace;//与左边沿的间距
        int firstY= 200;//与上边沿的间距
        ArrayList<MyPoint> pos= new ArrayList<MyPoint>();
        for (int i=0;i<rowNum*colNum;i++){
            float x,y;
            boolean flag=true;
            do {
                flag=false;
                x = random.nextInt(width - 2 * firstX - gridLength) + firstX;//随机一个坐标
                y = random.nextInt(height - firstY - 100-gridLength) + firstY;
                for (int j = 0; j <i&&!flag; j++) {
                    float xx=pos.get(j).x;
                    float yy=pos.get(j).y;
                    if (Math.abs(xx-x)<gridSpace||
                            Math.abs(yy-y)<gridSpace){
                        flag= true;
                        break;
                    }
                }
            }while (flag);
            pos.add(new MyPoint(x,y));
        }
        Collections.sort(pos, new Comparator<MyPoint>() {
            @Override
            public int compare(MyPoint a, MyPoint b) {
                return ((Float)a.y).compareTo((Float)b.y);
            }
        });
        for (int i=0;i<rowNum;i++)
            for (int j=0;j<colNum;j++) {
                //Log.i("yaoling1997","y:"+pos.get(i * rowNum + j).y);
                holes[i][j] = new Hole(pos.get(i * colNum + j).x, pos.get(i * colNum + j).y);
            }
    }
    private void initLayout(){//初始布局
        switch (myContext.getLevel()){
            case 0:
                rowNum=infiniteModeRowNum;
                colNum=infiniteModeColNum;
                holes= new Hole[rowNum][colNum];
                initRandomLayout();
                break;
            case 1://矩阵布局
                rowNum=1;
                colNum=1;
                initMatrixLayout();
                break;
            case 2://矩阵布局
                rowNum=1;
                colNum=2;
                initMatrixLayout();
                break;
            case 3://矩阵布局
                rowNum=3;
                colNum=1;
                initMatrixLayout();
                break;
            case 4://矩阵布局
                rowNum=2;
                colNum=2;
                initMatrixLayout();
                break;
            case 5://矩阵布局
                rowNum=2;
                colNum=2;
                initMatrixLayout();
                break;
            case 6://矩阵布局
                rowNum=2;
                colNum=2;
                initMatrixLayout();
                break;
            case 7://矩阵布局
                rowNum=2;
                colNum=2;
                initMatrixLayout();
                break;
            case 8://矩阵布局
                rowNum=2;
                colNum=2;
                initMatrixLayout();
                break;
            case 9://矩阵布局
                rowNum=4;
                colNum=1;
                initMatrixLayout();
                break;
            case 10://随机布局
                rowNum=5;
                colNum=1;
                initRandomLayout();
                break;
            case 11://矩阵布局
                rowNum=1;
                colNum=3;
                initMatrixLayout();
                break;
            case 12://随机布局
                rowNum=2;
                colNum=2;
                initMatrixLayout();
                break;
            case 13://矩阵布局
                rowNum=2;
                colNum=3;
                initMatrixLayout();
                break;
            case 14://随机布局
                rowNum=3;
                colNum=2;
                initRandomLayout();
                break;
            case 15://矩阵布局
                rowNum=2;
                colNum=3;
                initMatrixLayout();
                break;
            case 16://矩阵布局
                rowNum=4;
                colNum=2;
                initMatrixLayout();
                break;
            case 17://随机布局
                rowNum=1;
                colNum=8;
                initRandomLayout();
                break;
            case 18://随机布局
                rowNum=1;
                colNum=8;
                initRandomLayout();
                break;
            case 19://随机布局
                rowNum=1;
                colNum=8;
                initRandomLayout();
                break;
            case 20://矩阵布局
                rowNum=3;
                colNum=3;
                initMatrixLayout();
                break;
            case 21://矩阵布局
                rowNum=3;
                colNum=3;
                initMatrixLayout();
                break;
            case 22://随机布局
                rowNum=1;
                colNum=9;
                initRandomLayout();
                break;
            case 23://随机布局
                rowNum=1;
                colNum=9;
                initRandomLayout();
                break;
            case 24://随机布局
                rowNum=1;
                colNum=10;
                initRandomLayout();
                break;
            case 25://随机布局
                rowNum=1;
                colNum=10;
                initRandomLayout();
                break;
            case 26://随机布局
                rowNum=1;
                colNum=11;
                initRandomLayout();
                break;
            case 27://矩阵布局
                rowNum=4;
                colNum=3;
                initMatrixLayout();
                break;
            case 28://矩阵布局
                rowNum=4;
                colNum=3;
                initMatrixLayout();
                break;
            case 29://随机布局
                rowNum=1;
                colNum=12;
                initRandomLayout();
                break;
            case 30://随机布局
                rowNum=1;
                colNum=12;
                initRandomLayout();
                break;

        }
    }
    private void playSound(int id){
        if (id<0)
            return;
        soundPool.play(id, 1, 1, 0, 0, 1);
        Log.i("yaoling1997","play Sound Successfully id:"+id);
    }

    private void restart(){
        HP= INIT_HP;
        score=INIT_SCORE;
        isOver=false;
        initLayout();
        restPutCatDelay=putCatDelay;
    }
    private void loseHP(){
        HP = Math.max(0, HP - 1);
    }
    private int getNextId(){
        int cat4=100,cat3=90,cat2=60,cat1=10;
        int tmp = random.nextInt(100);
        switch (myContext.getLevel()){
            case 0://只有喵1、喵2、喵3、喵4和炸弹，无限模式
                cat4=99;
                cat3=90;
                cat2=60;
                cat1=10;
                break;
            case 1://只有喵1
                cat3=100;
                cat2=100;
                cat1=0;
                break;
            case 2://只有喵1
                cat3=100;
                cat2=100;
                cat1=0;
                break;
            case 3://只有喵1
                cat3=100;
                cat2=100;
                cat1=0;
                break;
            case 4://只有喵1，矩阵布局
                cat3=100;
                cat2=100;
                cat1=0;
                break;
            case 5://只有喵1和炸弹，矩阵布局
                cat3=100;
                cat2=100;
                cat1=20;
                break;
            case 6://只有喵2，矩阵布局
                cat3=100;
                cat2=0;
                cat1=0;
                break;
            case 7://只有喵1和喵2，矩阵布局
                cat3=100;
                cat2=50;
                cat1=0;
                break;
            case 8://只有喵1、喵2和炸弹，矩阵布局
                cat3=100;
                cat2=70;
                cat1=20;
                break;
            case 9://只有喵1、喵2和炸弹，随机布局
                cat3=100;
                cat2=70;
                cat1=10;
                break;
            case 10://只有喵1、喵2和炸弹，随机布局
                cat3=100;
                cat2=80;
                cat1=40;
                break;
            case 11://只有喵3，随机布局
                cat3=0;
                cat2=0;
                cat1=0;
                break;
            case 12://只有喵1、喵2、喵3，随机布局
                cat3=90;
                cat2=60;
                cat1=10;
                break;
            case 13://只有喵1、喵2、喵3，矩阵布局
                cat3=90;
                cat2=60;
                cat1=10;
                break;
            case 14://只有喵1、喵2、喵3，随机布局
                cat3=90;
                cat2=60;
                cat1=20;
                break;
            case 15://只有喵1、喵2、喵3和炸弹，矩阵布局
                cat4=99;
                cat3=90;
                cat2=60;
                cat1=20;
                break;
            case 16://只有喵1、喵2、喵3和炸弹，矩阵布局
                cat4=99;
                cat3=80;
                cat2=60;
                cat1=40;
                break;
            case 17://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=90;
                cat2=80;
                cat1=20;
                break;
            case 18://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=60;
                cat2=30;
                cat1=10;
                break;
            case 19://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=90;
                cat2=60;
                cat1=20;
                break;
            case 20://只有喵1、喵2、喵3和炸弹，矩阵布局
                cat4=99;
                cat3=80;
                cat2=50;
                cat1=30;
                break;
            case 21://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=90;
                cat2=40;
                cat1=20;
                break;
            case 22://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=90;
                cat2=40;
                cat1=10;
                break;
            case 23://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=90;
                cat2=50;
                cat1=20;
                break;
            case 24://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=60;
                cat2=40;
                cat1=20;
                break;
            case 25://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=80;
                cat2=50;
                cat1=20;
                break;
            case 26://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=90;
                cat2=50;
                cat1=20;
                break;
            case 27://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=80;
                cat2=60;
                cat1=20;
                break;
            case 28://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=90;
                cat2=70;
                cat1=20;
                break;
            case 29://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=70;
                cat2=50;
                cat1=20;
                break;
            case 30://只有喵1、喵2、喵3和炸弹，随机布局
                cat4=99;
                cat3=70;
                cat2=50;
                cat1=20;
                break;
        }
        if (tmp>=cat4)
            tmp = 3;
        else if (tmp >= cat3)
            tmp = 2;
        else if (tmp >= cat2)
            tmp = 1;
        else if (tmp>=cat1)
            tmp = 0;
        else
            tmp = -1;
        return tmp;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initGame(){
        initSounds();
        initAnimation();
        Hole.catPoked =getBitmap(R.drawable.cat_poked,gridLength);
        Hole.bombPoked =getBitmap(R.drawable.bomb_poked,gridLength);
        Hole.stubFront=getBitmap(R.drawable.stub_front,gridLength);
        Hole.stubBack=getBitmap(R.drawable.stub_back,gridLength);
        Hole.giftBoxClose=getBitmap(R.drawable.giftbox_close,gridLength);
        Hole.giftBoxOpen=getBitmap(R.drawable.giftbox_open,gridLength);
        Log.i("yaoling1997","Hole ok");
        //initMatrixLayout();
        restart();
        StartActivity.timer.schedule(new TimerTask() {//随机放喵喵
            @Override
            public void run() {
                if (restPutCatDelay>0)
                    restPutCatDelay--;
                if (!isOver&&restPutCatDelay==0) {
                    //Log.i("yaoling1997","put");
                    int row = random.nextInt(rowNum);
                    int col = random.nextInt(colNum);
                    if (holes[row][col].getStatus() == Hole.EMPTY) {
                        int tmp= getNextId();
                        holes[row][col].begin(tmp);
                        handler.sendEmptyMessage(0x123);
                        if (tmp>=0)//出来的是喵
                            playSound(catSoundId[tmp]);
                    }
                }
            }
        },0, putCatPeriod);
        StartActivity.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isOver) {
                    //Log.i("yaoling1997","update");
                    for (int i = 0; i < rowNum; i++)
                        for (int j = 0; j < colNum; j++) {
                            if (!holes[i][j].next())
                                loseHP();
                        }
                    handler.sendEmptyMessage(0x123);
                }
            }
        },0,framePeriod);
    }
    private Bitmap getBitmap(int id,int height){
        //height:从上往下截多长
        Drawable image= getResources().getDrawable(id);
        Bitmap bitmap=Bitmap.createBitmap(gridLength,gridLength, Bitmap.Config.ARGB_8888);
        Rect rect= new Rect(0,0,gridLength,gridLength);
        image.setBounds(rect);//以设定的比例画到bitmap的画布上
        Canvas canvas= new Canvas(bitmap);
        image.draw(canvas);
        //bitmap=Bitmap.createBitmap(bitmap,0,0,gridLength,height);//截取特定部分
        bitmap=Bitmap.createBitmap(bitmap,0,0,gridLength,Math.max(height,1));//截取特定部分
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawInfo(canvas);
        for (int i= 0;i<rowNum;i++)
            for (int j=0;j<colNum;j++) {
                int status=holes[i][j].getStatus();
                int id=holes[i][j].getId();
                Bitmap bitmapEvent;
                Paint paintEvent= new Paint();
                if (status==Hole.EMPTY){
                    bitmapEvent=Hole.stubBack;
                }else if (status<Hole.EMPTY){
                    if (id>=0)//是喵被打了
                        bitmapEvent=Hole.catPoked;
                    else//是炸弹被打了
                        bitmapEvent=Hole.bombPoked;
                    paintEvent.setAlpha((Hole.EMPTY-status)*255/(Hole.EMPTY- Hole.POKED));
                }else if (id>=0){//是喵
                    bitmapEvent= Hole.animationOutAndIn[id][status];
                }else {//是炸弹
                    bitmapEvent= Hole.animationBomb[status];
                }
                float x= holes[i][j].getX();
                float y= holes[i][j].getY();
                float catY= y+gridLength-bitmapEvent.getHeight();
                if (myContext.getLevel()>=30){
                    if (status==Hole.EMPTY)
                        canvas.drawBitmap(Hole.giftBoxClose, x, y, null);
                    else {
                        canvas.drawBitmap(bitmapEvent, x, catY, paintEvent);
                        canvas.drawBitmap(Hole.giftBoxOpen, x, y, null);
                    }
                }else {
                    canvas.drawBitmap(Hole.stubBack, x, y, null);
                    canvas.drawBitmap(bitmapEvent, x, catY, paintEvent);
                    canvas.drawBitmap(Hole.stubFront, x, y, null);
                }
            }
        if (HP<=0||
                (myContext.getLevel()>0&&score>=passScore[myContext.getLevel()])){
            gameOver();
            return;
        }
    }

    private void drawInfo(Canvas canvas){
        myContext.tvHP.mySetText(""+HP);
        if (myContext.getLevel()==0)
            myContext.tvScore.mySetText(""+score);
        else//闯关模式要显示目标得分
            myContext.tvScore.mySetText(""+score+"/"+passScore[myContext.getLevel()]);
    }
    private void updateScoreboard(){//更新积分榜
        SharedPreferences prefs= getContext().getSharedPreferences(Macro.PREFS_FILE,Context.MODE_PRIVATE);
        int tmpScore= score;
        for (int i=0;i<ScoreboardActivity.NoNum;i++){
            int oldScore=prefs.getInt(Macro.NO[i],0);
            if (oldScore<tmpScore){
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(Macro.NO[i],tmpScore);
                editor.commit();
                tmpScore=oldScore;
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void gameOver(){
        if (isOver)
            return;
        isOver=true;
        boolean isPass= myContext.getLevel()>0&&score>=passScore[myContext.getLevel()];//是否过关，决定用哪种对话框，无限模式不存在过关

        if (myContext.getLevel()==0)//是无限模式，需要更新积分榜
            updateScoreboard();

        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        View alertDialogView;
        if (isPass) {
            alertDialogView = View.inflate(getContext(), R.layout.alertdialog_gamepass, null);
        }else {
            alertDialogView = View.inflate(getContext(), R.layout.alertdialog_gameover, null);
        }
        StrokeTextView tvContent= (StrokeTextView)alertDialogView.findViewById(R.id.tvContent);
        Button btnStart= (Button)alertDialogView.findViewById(R.id.btnStart);
        Button btnEnd= (Button)alertDialogView.findViewById(R.id.btnEnd);
        builder.setView(alertDialogView);
        builder.setCancelable(false);

        final AlertDialog dialog= builder.create();

        if (!isPass) {//没过关对话框
            tvContent.setText("得分：" + score + " ");
            btnStart.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    restart();
                    dialog.dismiss();
                }
            });
        }else {//过关对话框
            tvContent.setText("恭喜过关！");
            if (myContext.getLevel()<Macro.LEVEL_NUM) {
                SharedPreferences prefs= myContext.getSharedPreferences(Macro.PREFS_FILE,Context.MODE_PRIVATE);//解锁下一关
                SharedPreferences.Editor editor= prefs.edit();
                editor.putString(Macro.LEVEL+(myContext.getLevel()+1),Macro.OPEN);
                editor.commit();

                btnStart.setText("下一关");
                btnStart.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myContext.updateLevel(myContext.getLevel()+1);;//跳至下一关
                        restart();
                        dialog.dismiss();
                    }
                });
            }else {
                btnStart.setText("恭喜通关");
                btnStart.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent();
                        intent.setAction(Macro.ACTION_PASS_ALL_LEVELS_ACTIVITY);
                        getContext().startActivity(intent);
                        ((StartActivity)getContext()).finish();
                        dialog.dismiss();
                    }
                });
            }
        }
        btnEnd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StartActivity)getContext()).finish();
                dialog.dismiss();
            }
        });
        MainActivity.addBtnAnimation(btnStart);
        MainActivity.addBtnAnimation(btnEnd);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        dialog.show();//show必须放前面，不知道为啥
        WindowManager.LayoutParams  lp= dialog.getWindow().getAttributes();
        lp.width=Macro.ALERTDIALOG_WIDTH;
        lp.height=Macro.ALERTDIALOG_HEIGHT;
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()!=MotionEvent.ACTION_DOWN)//只有手指点击才算poke成功
            return true;
        float x= event.getX();
        float y= event.getY();
        for (int i=rowNum-1;i>=0;i--)
            for (int j=colNum-1;j>=0;j--){//倒序判断，确保在最外面的喵喵先被打到
                float holeX=holes[i][j].getX();
                float holeY=holes[i][j].getY();
                if (holeX<=x&&x<=holeX+gridLength&&
                        holeY<=y&&y<=holeY+gridLength){
                    if (holes[i][j].poke()) {
                        if (holes[i][j].getId()>=0){//敲到的是喵，加分
                            score+=bonus[holes[i][j].getId()]*Hole.sameNum;//连续敲到同一种喵，奖励更多分
                            if (holes[i][j].getId()==catAuthorId)//敲到作者加血
                                HP++;
                            Log.i("yaoling1997","catPokedSoundId:"+ catPokedSoundId);
                            if (catPokedSoundId >=0)
                                playSound(catPokedSoundId);
                        }else {
                            loseHP();
                            if (bombPokedSoundId >=0)
                                playSound(bombPokedSoundId);
                        }
                        invalidate();
                        return true;
                    }
                }
            }
        return true;
    }
}

