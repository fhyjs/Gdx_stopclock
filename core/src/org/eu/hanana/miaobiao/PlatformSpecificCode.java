package org.eu.hanana.miaobiao;
public interface PlatformSpecificCode {
    String getStringResource(String resourceId);
    void show_toast(String str);
    void log(int priority, String tag, String msg);
    void start();
    void stop();
    void resume();
    void pause();
}
