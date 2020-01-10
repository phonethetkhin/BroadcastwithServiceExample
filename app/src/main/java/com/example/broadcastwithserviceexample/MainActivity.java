package com.example.broadcastwithserviceexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
Button btnStart,btnStop,btnSend;
TextView tvResult,tvCountDown;
EditText etMessage;
Intent myIntent=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart=findViewById(R.id.btnStartService);
        btnStop=findViewById(R.id.btnStopService);
        btnSend=findViewById(R.id.btnSend);
        tvResult=findViewById(R.id.tvResult);
        tvCountDown=findViewById(R.id.tvCountDown);
        etMessage=findViewById(R.id.etMessage);
        //*********************************//
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartService();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            StopService();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String inputMessage=etMessage.getText().toString();
            Intent i=new Intent();
            i.setAction(MyService.ACTION_MSG);
            i.putExtra(MyService.KEY_MSG,inputMessage);
            sendBroadcast(i);
            }
        });


    }
    private void StartService()
    {
        myIntent=new Intent(MainActivity.this,MyService.class);
        startService(myIntent);
    }
    private void StopService()
    {
        if(myIntent!=null)
        {
            stopService(myIntent);
        }
        myIntent=null;
    }
private class MyMainReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(action==MyService.ACTION_BIND_CountDown)
        {
            int countdownfromservice=intent.getIntExtra(MyService.COUNTKEY_FROM_SERVICE,0);
            tvCountDown.setText(countdownfromservice+"");
        }
        else if(action==MyService.ACTION_BIND_MSG)
        {
            String Message=intent.getStringExtra(MyService.MESSAGEKEY_FROM_SERVICE);
            tvResult.setText(Message);
        }
    }
}
}
