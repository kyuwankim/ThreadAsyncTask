package com.kyuwankim.android.threadasynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        new AsyncTask<String, Integer, Float>() {
            // 제네릭 타입 1 - doInBackground의 인자
            // 제네릭 타입 2 - onProgressUpdate의 인자
            // 제네릭 타입 3 - doInBackground의 리턴타입


            @Override
            protected void onProgressUpdate(Integer... values) {// 주기적으로 doInBackground 에서 호출이 가능한 함수
                                                               // Progress 사용할대 퍼센트 숫자 올라갈때
                progressDialog.setMessage("진행 : "+values[0]+"%");
            }

            @Override
            protected void onPreExecute() {// doInBackground가 호출되기 전에 먼저 호출된다
                super.onPreExecute();
                progressDialog.show();
            }


            @Override
            protected Float doInBackground(String... params) {// 데이터를 처리..
                try {
                    Log.e("AsyncTask", "첫번째값 : "+params[0]);
                    Log.e("AsyncTask", "두번째값 : "+params[1]);
                    for(int i = 0 ; i < 10 ; i ++){
                        publishProgress(i*10); // <- onProgressUpdate 를 주기적으로 업데이트 해주는 메소드
                        Thread.sleep(1000);
                    }
                    handler.sendEmptyMessage(MainActivity.SET_DONE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 1000.4F;
            }


            @Override
            protected void onPostExecute(Float result) { // doInBackground 호출되고 나서 호출된다

                Log.e("AsyncTask","doInBackground 결과값 : "+result);
                // 결과값을 메인 UI에 세팅하는 로직을 여기에 작성한다
                setDone();
                progressDialog.dismiss();
            }
        }.execute("11111","22222");// doInBackground를 실행
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
