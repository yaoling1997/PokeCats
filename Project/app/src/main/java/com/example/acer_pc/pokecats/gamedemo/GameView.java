package com.example.acer_pc.pokecats.gamedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by acer-pc on 2018/1/23.
 */

public class GameView extends View {
    private static final int INIT_HP=20;//初始HP
    private static final int INIT_SCORE=0;//初始score
    private static int bonus=20;//每打到一只喵喵增加的分数
    private static int gridLength=200;//一个格子的长宽
    private static int rowNum=4;//多少行
    private static int colNum=3;//多少列
    private static int textSize=40;//字体大小
    private static int HPX=20;//HP的X
    private static int HPY=50;//HP的Y
    private static int scoreX=20;//score的X
    private static int scoreY=80;//score的Y
    private int HP=INIT_HP;//生命值
    private int score=INIT_SCORE;//得分
    private boolean isUpdateing=false;
    private Random random= new Random();
    private Hole [][]holes;
    private boolean isOver=false;

    public GameView(Context context) {
        super(context);
    }
    public void initGame(){
        Hole.animationOutAndIn= new Bitmap[5];
        Hole.animationOutAndIn[0]=getBitmap(R.drawable.cat);
        Hole.animationOutAndIn[1]=getBitmap(R.drawable.cat);
        Hole.animationOutAndIn[2]=getBitmap(R.drawable.cat);
        Hole.animationOutAndIn[3]=getBitmap(R.drawable.cat);
        Hole.animationOutAndIn[4]=getBitmap(R.drawable.cat);
        Hole.emptyHole=getBitmap(R.drawable.empty);
        Hole.poked=getBitmap(R.drawable.poked);

        WindowManager wm= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int gridSpace= 20;//格子间的间隔
        //int firstX= 20;//第一个格子的X
        int firstX=(wm.getDefaultDisplay().getWidth()-gridSpace*(colNum-1)-colNum*gridLength)/2;
        int firstY= 200;//第一个格子的Y
        holes= new Hole[rowNum][colNum];
        for (int i=0;i<rowNum;i++)
            for (int j=0;j<colNum;j++)
                holes[i][j]=new Hole(firstX+j*(gridLength+gridSpace),firstY+i*(gridLength+gridSpace));
        final Handler handler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0x123&&!isUpdateing){//更新画布
                    invalidate();
                }
                super.handleMessage(msg);
            }
        };
        Timer timer= new Timer();
        timer.schedule(new TimerTask() {//随机放喵喵
            @Override
            public void run() {
                Log.i("yaoling1997","put");
                int row= random.nextInt(rowNum);
                int col= random.nextInt(colNum);
                if (holes[row][col].getStatus()==Hole.EMPTY){
                    holes[row][col].begin();
                    handler.sendEmptyMessage(0x123);
                }
            }
        },0,1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("yaoling1997","update");
                for (int i=0;i<rowNum;i++)
                    for (int j=0;j<colNum;j++) {
                        if (!holes[i][j].next())
                            HP=Math.max(0,HP-1);
                    }
                handler.sendEmptyMessage(0x123);
            }
        },0,1000);
    }
    private void restart(){
        HP= INIT_HP;
        score=INIT_SCORE;
        for (int i=0;i<rowNum;i++)
            for (int j=0;j<colNum;j++)
                holes[i][j].init();
        isOver=false;
    }
    private Bitmap getBitmap(int id){
        Drawable image= getResources().getDrawable(id);
        Bitmap bitmap=Bitmap.createBitmap(gridLength,gridLength, Bitmap.Config.ARGB_8888);
        Rect rect= new Rect(0,0,gridLength,gridLength);
        image.setBounds(rect);//以设定的比例画到bitmap的画布上
        Canvas canvas= new Canvas(bitmap);
        image.draw(canvas);
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
                float x= holes[i][j].getX();
                float y= holes[i][j].getY();
                if (status==Hole.EMPTY){
                    canvas.drawBitmap(Hole.emptyHole,x,y,null);
                }else if (status==Hole.POKED){
                    canvas.drawBitmap(Hole.poked,x,y,null);
                }else {
                    canvas.drawBitmap(Hole.animationOutAndIn[status],x , y, null);
                }
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
//        if (event.getAction()!=MotionEvent.ACTION_DOWN)//只有手指点击才算poke成功
//            return true;
        float x= event.getX();
        float y= event.getY();
        for (int i=0;i<rowNum;i++)
            for (int j=0;j<colNum;j++){
                float holeX=holes[i][j].getX();
                float holeY=holes[i][j].getY();
                if (holeX<=x&&x<=holeX+gridLength&&
                        holeY<=y&&y<=holeY+gridLength){
                    if (holes[i][j].poke()) {
                        score+=bonus;
                        invalidate();
                        return true;
                    }
                }
            }
        return true;
    }
}

