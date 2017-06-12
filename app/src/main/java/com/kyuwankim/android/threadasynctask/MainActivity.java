package com.kyuwankim.android.threadasynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int SET_DONE = 1;
    TextView textView;
    Handler handler;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case SET_DONE:
                        setDone();
                        break;
                }
            }
        };

        textView = (TextView) findViewById(R.id.textView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runAsync();
            }
        });

        // 화면에 진행상태를 표시
        // 프로그래스 다이얼로그 정의
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Looooooooding");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void setDone() {
        textView.setText("완료되었습니다");
        // 프로그래스 창을 해제
        progressDialog.dismiss();

    }

    private void runAsync() {
        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {// doInBackground가 호출되기 전에 먼저 호출된다
                super.onPreExecute();
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {// 데이터를 처리..
                try {
                    Thread.sleep(10000);
                    handler.sendEmptyMessage(MainActivity.SET_DONE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) { // doInBackground 호출되고 나서 호출된다
                super.onPostExecute(aVoid);
                // 결과값을 메인 UI에 세팅하는 로직을 여기에 작성한다
                setDone();
                progressDialog.dismiss();
            }
        }.execute();
    }

    private void runThread() {
        // 프로그래스 창을 호출
        progressDialog.show();
        CustomThread thread = new CustomThread(handler);
        thread.start();
    }
}


class CustomThread extends Thread {
    Handler handler;

    public CustomThread(Handler handler) {
        this.handler = handler;
    }

    public void run() {
        try {
            Thread.sleep(10000);
            handler.sendEmptyMessage(MainActivity.SET_DONE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
