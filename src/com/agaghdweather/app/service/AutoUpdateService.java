package com.agaghdweather.app.service;

import com.agaghdweather.app.activity.NewWeatherActivity;
import com.agaghdweather.app.receiver.AutoUpdateReceiver;
import com.agaghdweather.app.util.HttpCallbackListener;
import com.agaghdweather.app.util.HttpUtil;
import com.agaghdweather.app.util.MyApplication;
import com.agaghdweather.app.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class AutoUpdateService extends Service {

	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				updateWeather();

			}
			
		}).start();
		
		/*
		//ui更新广播注册
		uiRefreshBroadcastManager = LocalBroadcastManager.getInstance
				(MyApplication.getContext());
		intentFilter = new IntentFilter();
		intentFilter.addAction("com.coolweather.app.UIREFRESH_BROADCAST");
		*/
		
		
		
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 4 * 60 * 60 * 1000;		//单位毫秒，即4小时
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * 更新天气信息的方法
	 */
	private void updateWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code", "");
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
				
				//发送更新UI广播
				
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
			
		});
	}
	

}
