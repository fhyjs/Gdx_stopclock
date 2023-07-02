package org.eu.hanana.miaobiao;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class Timer extends Service {
    private static final String TAG = "TimerService";
    private TimeClock tl;
    public static Timer Instance;
    public Timer(){
        super();
        Instance=this;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        tl=new TimeClock();
    }
    public void pause(){
        tl.stop=true;
        tl.interrupt();
        tl.ispause=true;
    }
    public void resume(){
        if (tl.getState()== Thread.State.TERMINATED)
            tl=new TimeClock(tl);
        tl.start();
        tl.ispause=false;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        tl.start();
        tl.isrunning=true;
        tl.ispause=false;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tl.stop=true;
        tl.interrupt();
        tl.isrunning=false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
