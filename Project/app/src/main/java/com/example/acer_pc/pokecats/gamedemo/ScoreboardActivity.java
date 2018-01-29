package com.example.acer_pc.pokecats.gamedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by acer-pc on 2018/1/24.
 */

public class ScoreboardActivity extends Activity {//查看积分界面
    public static final int NoNum=3;
    private SharedPreferences prefs;
    private StrokeTextView []tvNo;
    private Button btnReset;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scoreboard);
        bindViews();
    }
    private void bindViews(){
        tvNo= new StrokeTextView[NoNum];
        tvNo[0]=(StrokeTextView)findViewById(R.id.tvNo1);
        tvNo[1]=(StrokeTextView)findViewById(R.id.tvNo2);
        tvNo[2]=(StrokeTextView)findViewById(R.id.tvNo3);
        btnReset= (Button)findViewById(R.id.btnReset);
        MainActivity.addBtnAnimation(btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(ScoreboardActivity.this);
                View alertDialogView;
                alertDialogView = View.inflate(ScoreboardActivity.this, R.layout.alertdialog_reset_scoreboard, null);
                StrokeTextView tvContent= (StrokeTextView)alertDialogView.findViewById(R.id.tvContent);
                Button btnConfirm= (Button)alertDialogView.findViewById(R.id.btnConfirm);
                Button btnCancel= (Button)alertDialogView.findViewById(R.id.btnCancel);
                builder.setView(alertDialogView);
                builder.setCancelable(false);

                final AlertDialog dialog= builder.create();

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prefs=getSharedPreferences(Macro.PREFS_FILE,MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();//清空用户分数
                        for (int i=0;i<NoNum;i++) {
                            editor.remove(Macro.NO[i]);
                        }
                        editor.commit();
                        updateNo();
                        dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                MainActivity.addBtnAnimation(btnConfirm);
                MainActivity.addBtnAnimation(btnCancel);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                dialog.show();//show必须放前面，不知道为啥
                WindowManager.LayoutParams  lp= dialog.getWindow().getAttributes();
                lp.width=Macro.ALERTDIALOG_WIDTH;
                lp.height=Macro.ALERTDIALOG_HEIGHT;
                dialog.getWindow().setAttributes(lp);
            }
        });
        updateNo();
    }

    private void updateNo() {
        prefs=getSharedPreferences(Macro.PREFS_FILE,MODE_PRIVATE);
        for (int i=0;i<NoNum;i++) {
            int score = prefs.getInt(Macro.NO[i], 0);
            tvNo[i].mySetText(Macro.NO[i] + ":  " + score);
        }
    }

}
