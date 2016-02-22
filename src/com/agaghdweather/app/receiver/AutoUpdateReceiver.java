package com.agaghdweather.app.receiver;

import com.agaghdweather.app.service.AutoUpdateService;
import com.agaghdweather.app.util.MyApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class AutoUpdateReceiver extends BroadcastReceiver {

	private LocalBroadcastManager uiRefreshBroadcastManager;
	
	//private IntentFilter intentFilter;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		uiRefreshBroadcastManager = LocalBroadcastManager.getInstance
				(MyApplication.getContext());
		
		Intent intentUI = new Intent("com.coolweather.app.UIREFRESH_BROADCAST");
		uiRefreshBroadcastManager.sendBroadcast(intentUI);
		Intent i = new Intent(context, AutoUpdateService.class);
		context.startService(i);
	}
	
}
