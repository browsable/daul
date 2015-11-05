package com.daemin.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;

public class ProgressDialogActivity extends Activity {

    private boolean isRunning = false;
    private ProgressDialog pDialog;
    private int DONE = 100;
    private String action = null;
    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onResume() {
        super.onResume();
        if(action.equals(Common.ACTION_SHOWDIALOG)){
            // ProgressDialog 실행
            showDialog();
            // 화면상에 ProgressDialog가 실행되고 있음을 명시
            isRunning = true;
        }
        else if(action.equals(Common.ACTION_TIMEOUT)&&isRunning == true){
            // ProgressDialog 종료
            hideDialog();
        }
        else {
            sendActionFinish();
            finish();
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        action = intent.getAction();
        super.onNewIntent(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        action = getIntent().getAction();
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    public void showDialog(){
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Title");
        pDialog.setMessage("Message");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();
        mHandler.sendEmptyMessageDelayed(DONE, 10000);
    }
    public void sendActionFinish(){
        Intent intent = new Intent();
        intent.setAction(Common.ACTION_DIALOGFINISH);
        sendBroadcast(intent);
    }
    // ProgressDialog를 종료하고 activity도 종료, 위젯으로 돌아감
    public void hideDialog(){
        sendActionFinish();
        pDialog.dismiss();
        isRunning = false;
        finish();
    }
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Common.ACTION_DELAY);
        sendBroadcast(intent);
    }
    public Handler mHandler = new Handler(){
        // ProgressDialog 실행 10초후에 호출됨
        public void handleMessage(Message msg){
            if(msg.what == DONE){
                // ProgressDialog 실행도중 별도의 Interrupt intent가 수신되지 않으면,
                if(isRunning = true){
                    // ProgressDialog 종료
                    hideDialog();
                }
            }
        }
    };
} 