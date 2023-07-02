package org.eu.hanana.miaobiao;

import java.lang.reflect.Field;
import java.util.Locale;

public class TimeClock extends Thread{
    private static TimeClock TC;
    public int Seconds;
    public static TimeClock getTC() {
        return TC;
    }
    public boolean isrunning,ispause;
    public TimeClock(){
        super();
        TC=this;
        Seconds=0;
    }
    public TimeClock(TimeClock l){
        super();
        TC=this;
        for (Field f : this.getClass().getFields()){
            f.setAccessible(true);
            try {
                f.set(this,getval(f,l));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public boolean stop;
    @Override
    public void run() {
        stop=false;
        while (!stop){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Seconds--;
            }
            Seconds++;
        }
    }
    public Object getval(Field field, TimeClock l){
        for (Field f : l.getClass().getFields()){
            f.setAccessible(true);
            if (f.getName().equals(field.getName())) {
                try {
                    return f.get(l);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return formatDateTime(Seconds);
    }
    public static String formatDateTime(long mss) {
        String DateTimes = null;
        long hours = mss / 3600;
        long minutes = (mss % 3600) / 60;
        long remainingSeconds = mss % 60;
        DateTimes=String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,remainingSeconds);

        return DateTimes;
    }
}
