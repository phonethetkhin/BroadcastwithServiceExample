package com.example.broadcastwithserviceexample;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyService extends Service {

    //from service to main
    final static String COUNTKEY_FROM_SERVICE = "KEY_COUNT_FROM_SERVICE";
    final static String MESSAGEKEY_FROM_SERVICE = "KEY_MESSAGE_FROM_SERVICE";
    final static String ACTION_BIND_CountDown = "BINDVALUE_CountDown";
    final static String ACTION_BIND_MSG = "BINDVALUE_MESSAGE";

    //from main to service
    final static String ACTION_MSG = "MSG_TO_SERVICE";
    final static String KEY_MSG = "KEY_TO_SERVICE";
    MyserviceReceiver myserviceReceiver;
    MyThread myThread;

    @Override
    public void onCreate() {
        super.onCreate();
        myserviceReceiver=new MyserviceReceiver();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ACTION_MSG);
        registerReceiver(myserviceReceiver,intentFilter);

        myThread=new MyThread();
        myThread.start();

        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        myThread.setRunning(false);
        unregisterReceiver(myserviceReceiver);
        super.onDestroy();


    }

    public class MyserviceReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action==ACTION_MSG)
            {
                String msg=intent.getStringExtra(KEY_MSG);

                Intent i=new Intent();
                i.setAction(ACTION_BIND_MSG);
                i.putExtra(MESSAGEKEY_FROM_SERVICE,msg);
                sendBroadcast(i);

            }

        }
    }
    private class MyThread extends Thread
    {
        private boolean running;

        public void setRunning(boolean running){
            this.running = running;
        }
        @Override
        public void run() {
            super.run();
            int count=0;
            running=true;
            while(running)
            {
                try {
                    Thread.sleep(1000);


                Intent i=new Intent();
                i.setAction(ACTION_BIND_CountDown);
                i.putExtra(COUNTKEY_FROM_SERVICE,count);
                sendBroadcast(i);
                count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
