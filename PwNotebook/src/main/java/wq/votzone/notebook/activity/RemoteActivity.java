package wq.votzone.notebook.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import wq.votzone.notebook.R;
import wq.votzone.notebook.remote.SimpleServer;

public class RemoteActivity extends AppCompatActivity {

    View btnStart, btnStop;
    TextView tvIP, tvHint;

    Boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(wq.votzone.notebook.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvIP = (TextView) findViewById(R.id.ip_address);
        tvHint = (TextView ) findViewById(R.id.tv_hint);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleServer.Companion.simpleDemo();
                tvHint.setText("运行中");
                isRunning = true;
            }
        });
        tvIP.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                String ipaddress = tvIP.getText().toString();
                String tmp = "(点击可复制)";
                if(ipaddress.endsWith(tmp)){
                    ipaddress = ipaddress.substring(0,ipaddress.length()-tmp.length());
                }
                cm.setText(ipaddress);
                Toast.makeText(RemoteActivity.this, "远程地址已复制到剪切板。", Toast.LENGTH_SHORT).show();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SimpleServer.Companion.stopSimpleServer();
                tvHint.setText("已停止");
                isRunning = false;
            }
        });
        tvIP .setText("http://"+SimpleServer.Companion.getLocalIpAddress(this)+":8088(点击可复制)");
    }

    public static void launch(Activity from){
        Intent intent = new Intent(from, RemoteActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isRunning){
            SimpleServer.Companion.stopSimpleServer();
            tvHint.setText("已停止");
            isRunning = false;
            Toast.makeText(this,"远程操作已暂停",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
