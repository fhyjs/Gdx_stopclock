package org.eu.hanana.miaobiao;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.badlogic.gdx.backends.android.AsynchronousAndroidAudio;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AndroidLauncher extends AndroidApplication implements PlatformSpecificCode{
	Intent serviceIntent;
	Handler handler = new Handler(Looper.getMainLooper());
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		serviceIntent = new Intent(this, Timer.class);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		initialize(new Miaobiao(this), config);
	}
	@Override
	public String getStringResource(String resourceId) {
		Resources resources = getApplicationContext().getResources();
		try {
			return resources.getString((Integer) R.string.class.getField(resourceId).get(null));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public void show_toast(String str) {
		handler.post(() -> Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show());
	}
	@Override
	public void log(int priority, String tag, String msg) {
		Log.println(priority,tag,msg);
	}

	@Override
	public void start() {
		startService(serviceIntent);
	}

	@Override
	public void stop() {
		stopService(serviceIntent);
	}

	@Override
	public void resume() {
		Timer.Instance.resume();
	}

	@Override
	public void pause() {
		Timer.Instance.pause();
	}

	@Override
	public AndroidAudio createAudio(Context context, AndroidApplicationConfiguration config) {
		return new AsynchronousAndroidAudio(context, config);
	}
	boolean VUPl,VDOWNl;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
			if (!VDOWNl)
				((Miaobiao) Gdx.app.getApplicationListener()).btnpcl.clicked(null,0,0);
			VDOWNl=true;
			return true;
		}
		if (keyCode==KeyEvent.KEYCODE_VOLUME_UP){
			if (!VUPl)
				((Miaobiao) Gdx.app.getApplicationListener()).btnscl.clicked(null,0,0);
			VUPl=true;
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
			VDOWNl=false;
			return true;
		}
		if (keyCode==KeyEvent.KEYCODE_VOLUME_UP){
			VUPl=false;
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}
