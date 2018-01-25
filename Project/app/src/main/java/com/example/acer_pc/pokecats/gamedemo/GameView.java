package com.example.acer_pc.pokecats.gamedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.TimerTask;

/**
 * Created by acer-pc on 2018/1/23.
 */

public class GameView extends View {
    private static final int INIT_HP=10;//初始HP
    private static final int INIT_SCORE=0;//初始score
    private static final int []bonus={1,2,3};//每打到一只喵喵增加的分数
    private static final int gridLength=420;//一个格子的长宽
    private static final int rowNum=3;//多少行
    private static final int colNum=3;//多少列
    private static final int textSize=40;//字体大小
    private static final int HPX=20;//HP的X
    private static final int HPY=50;//HP的Y
    private static final int scoreX=20;//score的X
    private static final int scoreY=80;//score的Y
    private static final int catKindNum=3;//喵喵种类
    private static final int putCatDelay =10;//延迟多久开始放喵，延迟时长为putCatDelay*putCatPeriod
    private static final int putCatPeriod =300;//放喵的周期
    private int HP=INIT_HP;//生命值
    private int score=INIT_SCORE;//得分
    private boolean isUpdateing=false;
    private Random random= new Random();
    private Hole [][]holes;
    private boolean isOver=false;
    public SoundPool soundPool=null;
    private int pokedSoundId=-1;
    private int []catSoundId;
    private int restPutCatDelay=0;

    private final Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x123&&!isUpdateing&&!isOver){//更新画布
                invalidate();
            }
            super.handleMessage(msg);
        }
    };//处理事件

    public GameView(Context context) {
        super(context);
    }
    public class MyPoint{
        public float x,y;

        public MyPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initSounds(){
        pokedSoundId=-1;
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
            pokedSoundId = soundPool.load(getContext(), R.raw.duang, 1);
            catSoundId[0] = soundPool.load(getContext(), R.raw.cat1, 1);
            catSoundId[1] = soundPool.load(getContext(), R.raw.cat2, 1);
            catSoundId[2] = soundPool.load(getContext(), R.raw.cat3, 1);
        }
    }

    private void initAnimation0(){
        Hole.animationOutAndIn[0]= new Bitmap[20];
        Hole.animationOutAndIn[0][0]=getBitmap(R.drawable.cat1_1,1*gridLength/6);
        Hole.animationOutAndIn[0][1]=getBitmap(R.drawable.cat1_2,2*gridLength/6);
        Hole.animationOutAndIn[0][2]=getBitmap(R.drawable.cat1_3,3*gridLength/6);
        Hole.animationOutAndIn[0][3]=getBitmap(R.drawable.cat1_4,4*gridLength/6);
        Hole.animationOutAndIn[0][4]=getBitmap(R.drawable.cat1_5,5*gridLength/6);
        Hole.animationOutAndIn[0][5]=getBitmap(R.drawable.cat1_1,6*gridLength/6);
        Hole.animationOutAndIn[0][6]=getBitmap(R.drawable.cat1_2,6*gridLength/6);
        Hole.animationOutAndIn[0][7]=getBitmap(R.drawable.cat1_3,6*gridLength/6);
        Hole.animationOutAndIn[0][8]=getBitmap(R.drawable.cat1_4,6*gridLength/6);
        Hole.animationOutAndIn[0][9]=getBitmap(R.drawable.cat1_5,6*gridLength/6);
        Hole.animationOutAndIn[0][10]=getBitmap(R.drawable.cat1_1,6*gridLength/6);
        Hole.animationOutAndIn[0][11]=getBitmap(R.drawable.cat1_2,6*gridLength/6);
        Hole.animationOutAndIn[0][12]=getBitmap(R.drawable.cat1_3,6*gridLength/6);
        Hole.animationOutAndIn[0][13]=getBitmap(R.drawable.cat1_4,6*gridLength/6);
        Hole.animationOutAndIn[0][14]=getBitmap(R.drawable.cat1_5,6*gridLength/6);
        Hole.animationOutAndIn[0][15]=getBitmap(R.drawable.cat1_1,5*gridLength/6);
        Hole.animationOutAndIn[0][16]=getBitmap(R.drawable.cat1_2,4*gridLength/6);
        Hole.animationOutAndIn[0][17]=getBitmap(R.drawable.cat1_3,3*gridLength/6);
        Hole.animationOutAndIn[0][18]=getBitmap(R.drawable.cat1_4,2*gridLength/6);
        Hole.animationOutAndIn[0][19]=getBitmap(R.drawable.cat1_5,1*gridLength/6);
    }
    private void initAnimation1(){
        Hole.animationOutAndIn[1]= new Bitmap[16];
        Hole.animationOutAndIn[1][0]=getBitmap(R.drawable.cat2_1,1*gridLength/6);
        Hole.animationOutAndIn[1][1]=getBitmap(R.drawable.cat2_2,2*gridLength/6);
        Hole.animationOutAndIn[1][2]=getBitmap(R.drawable.cat2_3,3*gridLength/6);
        Hole.animationOutAndIn[1][3]=getBitmap(R.drawable.cat2_4,6*gridLength/6);
        Hole.animationOutAndIn[1][4]=getBitmap(R.drawable.cat2_1,6*gridLength/6);
        Hole.animationOutAndIn[1][5]=getBitmap(R.drawable.cat2_2,6*gridLength/6);
        Hole.animationOutAndIn[1][6]=getBitmap(R.drawable.cat2_3,6*gridLength/6);
        Hole.animationOutAndIn[1][7]=getBitmap(R.drawable.cat2_4,6*gridLength/6);
        Hole.animationOutAndIn[1][8]=getBitmap(R.drawable.cat2_1,6*gridLength/6);
        Hole.animationOutAndIn[1][9]=getBitmap(R.drawable.cat2_2,6*gridLength/6);
        Hole.animationOutAndIn[1][10]=getBitmap(R.drawable.cat2_3,6*gridLength/6);
        Hole.animationOutAndIn[1][11]=getBitmap(R.drawable.cat2_4,6*gridLength/6);
        Hole.animationOutAndIn[1][12]=getBitmap(R.drawable.cat2_1,6*gridLength/6);
        Hole.animationOutAndIn[1][13]=getBitmap(R.drawable.cat2_2,3*gridLength/6);
        Hole.animationOutAndIn[1][14]=getBitmap(R.drawable.cat2_3,2*gridLength/6);
        Hole.animationOutAndIn[1][15]=getBitmap(R.drawable.cat2_4,1*gridLength/6);
    }
    private void initAnimation2(){
        Hole.animationOutAndIn[2]= new Bitmap[10];
        Hole.animationOutAndIn[2][0]=getBitmap(R.drawable.cat3_1,1*gridLength/6);
        Hole.animationOutAndIn[2][1]=getBitmap(R.drawable.cat3_2,2*gridLength/6);
        Hole.animationOutAndIn[2][2]=getBitmap(R.drawable.cat3_3,3*gridLength/6);
        Hole.animationOutAndIn[2][3]=getBitmap(R.drawable.cat3_4,6*gridLength/6);
        Hole.animationOutAndIn[2][4]=getBitmap(R.drawable.cat3_5,6*gridLength/6);
        Hole.animationOutAndIn[2][5]=getBitmap(R.drawable.cat3_4,6*gridLength/6);
        Hole.animationOutAndIn[2][6]=getBitmap(R.drawable.cat3_5,6*gridLength/6);
        Hole.animationOutAndIn[2][7]=getBitmap(R.drawable.cat3_6,3*gridLength/6);
        Hole.animationOutAndIn[2][8]=getBitmap(R.drawable.cat3_7,2*gridLength/6);
        Hole.animationOutAndIn[2][9]=getBitmap(R.drawable.cat3_8,1*gridLength/6);
    }
    private void initAnimation(){
        Hole.animationOutAndIn= new Bitmap[catKindNum][];
        initAnimation0();
        initAnimation1();
        initAnimation2();
    }
    private void initMatrixLayout(){//矩阵型的布局
        WindowManager wm= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int gridSpace= 20;//格子间的间隔
        int firstX=(wm.getDefaultDisplay().getWidth()-gridSpace*(colNum-1)-colNum*gridLength)/2;
        int firstY= 200;//第一个格子的Y
        for (int i=0;i<rowNum;i++)
            for (int j=0;j<colNum;j++)
                holes[i][j]=new Hole(firstX+j*(gridLength+gridSpace),firstY+i*(gridLength-gridSpace));
    }
    private void initRandomLayout(){//随机布局
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
        initRandomLayout();
        restPutCatDelay=putCatDelay;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initGame(){
        initSounds();
        initAnimation();
        Hole.poked=getBitmap(R.drawable.poked,gridLength);
        Hole.stubFront=getBitmap(R.drawable.stub_front,gridLength);
        Hole.stubBack=getBitmap(R.drawable.stub_back,gridLength);
        Log.i("yaoling1997","Hole ok");
        holes= new Hole[rowNum][colNum];
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
                        int tmp = random.nextInt(6);
                        if (tmp >= 5)
                            tmp = 2;
                        else if (tmp >= 3)
                            tmp = 1;
                        else tmp = 0;
                        holes[row][col].begin(tmp);
                        handler.sendEmptyMessage(0x123);
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
                                HP = Math.max(0, HP - 1);
                        }
                    handler.sendEmptyMessage(0x123);
                }
            }
        },0,120);
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
        if (HP<=0){
            gameOver();
            return;
        }
        drawInfo(canvas);
        for (int i= 0;i<rowNum;i++)
            for (int j=0;j<colNum;j++) {
                int status=holes[i][j].getStatus();
                int id=holes[i][j].getId();
                Bitmap b;
                if (status==Hole.EMPTY){
                    b=Hole.stubBack;
                }else if (status<Hole.EMPTY){
                    b=Hole.poked;
                }else {
                    b= Hole.animationOutAndIn[id][status];
                }
                float x= holes[i][j].getX();
                float y= holes[i][j].getY();
                float catY= y+gridLength-b.getHeight();
                canvas.drawBitmap(Hole.stubBack,x,y,null);
                canvas.drawBitmap(b,x,catY,null);
                canvas.drawBitmap(Hole.stubFront,x,y,null);
            }
    }

    private void drawInfo(Canvas canvas){
        Paint paint= new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(textSize);
        canvas.drawText("HP: "+HP,HPX,HPY,paint);
        canvas.drawText("Score: "+score,scoreX,scoreY,paint);
    }
    private void gameOver(){
        if (isOver)
            return;
        isOver=true;
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
        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        builder.setTitle("GAME OVER");
        builder.setMessage("Score: "+score+", restart?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                restart();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((StartActivity)getContext()).finish();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog= builder.create();
        dialog.show();
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
                        score+=bonus[holes[i][j].getId()];
                        invalidate();
                        Log.i("yaoling1997","pokedSoundId:"+pokedSoundId);
                        if (pokedSoundId>=0)
                            playSound(pokedSoundId);
                        return true;
                    }
                }
            }
        return true;
    }
}

