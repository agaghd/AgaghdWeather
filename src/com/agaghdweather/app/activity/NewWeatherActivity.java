package com.agaghdweather.app.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ofs.ahd.dii.br.AdSize;
import ofs.ahd.dii.br.AdView;
import ofs.ahd.dii.video.w;

import com.agaghdweather.app.db.CoolWeatherOpenHelper;
import com.agaghdweather.app.model.City;
import com.agaghdweather.app.model.CoolWeatherDB;
import com.agaghdweather.app.model.SpecialCity;
import com.agaghdweather.app.model.SpecialCityAdapter;
import com.agaghdweather.app.service.AutoUpdateService;
import com.agaghdweather.app.service.FxService;
import com.agaghdweather.app.util.HttpCallbackListener;
import com.agaghdweather.app.util.HttpUtil;
import com.agaghdweather.app.util.MyApplication;
import com.agaghdweather.app.util.Utility;
import com.agaghdweather.app.R;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewWeatherActivity extends Activity implements OnClickListener{
	

	@Override
	protected void onDestroy() {
		uiRefreshBroadcastManager.unregisterReceiver(uiRefreshReceiver);
		super.onDestroy();
	}

	/**
	 * UI广播接收实例
	 */
	private UIRefreshReceiver uiRefreshReceiver;
	
	private LocalBroadcastManager uiRefreshBroadcastManager;
	
	private IntentFilter intentFilter;

	/**
	 * 用于显示今天及未来三天的天气的小窗口布局
	 */
	private LinearLayout todayLayout;
	private LinearLayout onedayLayout;
	private LinearLayout twodayLayout;
	private LinearLayout threedayLayout;
	private DrawerLayout drawerLayout;
	
 
	/**
	 * 天气背景
	 */
	
	private RelativeLayout weatherBackgroundLayout;
	
	
	/**
	 * 用于显示城市名
	 */
	private TextView cityNameText;
	
	/**
	 * 侧滑栏城市列表
	 */
	private ListView cityList;
	
	/**
	 * 侧滑栏城市容器
	 */
	
	private List<SpecialCity> sclist;
	
	/**
	 * 侧滑栏城市列表适配器
	 */
	SpecialCityAdapter adapter;
	
	/**
	 * 侧滑栏城市code
	 */
	String slideCountyCode;
	
	/**
	 *CheckBox选择是否显示悬浮窗 
	 */
	private CheckBox showFloatWindow;
	
	/**
	 * 悬浮窗提示是否显示过
	 */
	
	private Boolean floatAlertShowed;
	
	/**
	 * 获取showTextBinder实例
	 */
	private FxService.ShowTextBinder showTextBinder;
	
	
	class MyServiceConnection implements ServiceConnection {
		
		String text;
		
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			showTextBinder = (FxService.ShowTextBinder) service;
			showTextBinder.setFloatWindowText(text);
		}
	}
	
	MyServiceConnection connection = new MyServiceConnection();
	
	/**
	 * 退出按钮
	 */
	private Button exitButton; 	
	
	/**
	 * 用于显示发布内容
	 */
	private TextView publishText;
	
	/**
	 * 用于显示今天及未来三天天气描述
	 */
	private TextView typeText;
	private TextView type1Text;
	private TextView type2Text;
	private TextView type3Text;
	
	/**
	 * 用于显示今天及未来三天气温
	 */
	private TextView tempRange;	
	private TextView tempRange1;	
	private TextView tempRange2;	
	private TextView tempRange3;	
	
	
	
	/**
	 * 用于显示当前及未来三天日期
	 */
	private TextView currentDateText;
	private TextView date1Text;
	private TextView date2Text;
	private TextView date3Text;
	
	/**
	 * 显示感冒小贴士
	 */
	private TextView coldAlertTextView;
	
	/**
	 * 切换城市按钮
	 */
	private Button switchCity;
	
	/**
	 * 添加特别关心城市按钮
	 */
	
	private Button addSpecialCity;
	
	/**
	 * 更新天气按钮
	 */
	
	private Button refreshWeather;
	
	/**
	 * 天气代号
	 */
	String weatherCode;
	
	/**
	 * 县城代号
	 */
	String countyCode;
	
	/**
	 * 城市 天气 温度 日期 感冒指数
	 */
	
	String cityName;
	
	String weatherText;
	String weatherText1;
	String weatherText2;
	String weatherText3;
	
	String temperRange;
	String temperRange1;
	String temperRange2;
	String temperRange3;
	
	String date1;
	String date2;
	String date3;
	
	String coldAlert;
	/**
	 * 数据库
	 * 
	 */
	
	private CoolWeatherDB coolWeatherDB;
	
	
	/**
	 * Notify切换至活动的代号
	 */
	
	private static final int NEWWEATHERACTIVITY = 1;
	
	/**
	 * 2016-2-18
	 * 刷新天气信息的方法
	 */
	public void refreshWeatherInfo () {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		weatherCode = prefs.getString("weather_code", "");
		if (!TextUtils.isEmpty(weatherCode)) {
			queryWeatherInfo(weatherCode);
		}
		readWeatherInfo();
		showNotice();
	}
	
	private void showNotice() {
		NotificationManager manager = (NotificationManager)
				getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"天气已更新", System.currentTimeMillis());
		Intent intent = new Intent(NewWeatherActivity.this, NewWeatherActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(NewWeatherActivity.this,
				0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(NewWeatherActivity.this, 
				cityName, weatherText + " " + temperRange, pendingIntent);
		manager.notify(NEWWEATHERACTIVITY, notification);
		
		//如果开启了悬浮窗，则同时更新悬浮窗的内容
		if (showFloatWindow.isChecked()) {
			//先终止服务并解除绑定，再重新开启服务
			Intent intentShowFloat = new Intent(NewWeatherActivity.this, FxService.class);  
            //终止FxService  
			unbindService(connection);
			stopService(intentShowFloat);			
			//Intent intentShowFloat = new Intent(NewWeatherActivity.this, FxService.class);  
                //启动FxService 
			String floatmessage = cityName + " " + weatherText + " " + temperRange;
			connection.setText(floatmessage);			
            bindService(intentShowFloat, connection, BIND_AUTO_CREATE);
		}
		Toast.makeText(MyApplication.getContext(), "天气已更新", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_weather_layout);
		
		/**
		 * 初始化各控件
		 */
		drawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
		weatherBackgroundLayout = (RelativeLayout) findViewById(R.id.weatherback);
		
		todayLayout = (LinearLayout) findViewById (R.id.today_layout);
		onedayLayout = (LinearLayout) findViewById (R.id.one_day_layout);
		twodayLayout = (LinearLayout) findViewById (R.id.two_day_layout);
		threedayLayout = (LinearLayout) findViewById (R.id.three_day_layout);
		
		typeText = (TextView) findViewById (R.id.type);
		type1Text = (TextView) findViewById (R.id.type1);
		type2Text = (TextView) findViewById (R.id.type2);
		type3Text = (TextView) findViewById (R.id.type3);
		
		tempRange = (TextView) findViewById (R.id.temp_range);
		tempRange1 = (TextView) findViewById (R.id.temp_range1);
		tempRange2 = (TextView) findViewById (R.id.temp_range2);
		tempRange3 = (TextView) findViewById (R.id.temp_range3);
		
		date1Text = (TextView) findViewById(R.id.date1);
		date2Text = (TextView) findViewById(R.id.date2);
		date3Text = (TextView) findViewById(R.id.date3);
		
		cityNameText = (TextView) findViewById (R.id.city_name);
		publishText = (TextView) findViewById (R.id.publish_text);
		
		currentDateText = (TextView) findViewById (R.id.current_date);
		
		coldAlertTextView = (TextView) findViewById(R.id.cold_alert);
		
		switchCity = (Button) findViewById (R.id.switch_city);
		refreshWeather = (Button) findViewById (R.id.refresh_weather);
		addSpecialCity = (Button) findViewById(R.id.add_specialcity);
		exitButton = (Button) findViewById(R.id.exit);
		
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		addSpecialCity.setOnClickListener(this);
		exitButton.setOnClickListener(this);
		
		showFloatWindow = (CheckBox) findViewById(R.id.show_floatwindow);
		showFloatWindow.setOnClickListener(this);
		
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		
		countyCode = getIntent().getStringExtra("county_code");
		//
		
		//初始化控制变量
		floatAlertShowed = false;
		
		//注册UI接收广播
		uiRefreshBroadcastManager = LocalBroadcastManager.getInstance
				(this);
		intentFilter = new IntentFilter();
		intentFilter.addAction("com.coolweather.app.UIREFRESH_BROADCAST");
		uiRefreshReceiver = new UIRefreshReceiver();
		uiRefreshBroadcastManager.registerReceiver(uiRefreshReceiver, intentFilter);
		
		if(!TextUtils.isEmpty(countyCode)) {
			//有县级代号时就去查天气
			publishText.setText("同步中······");
			todayLayout.setVisibility(View.INVISIBLE);
			onedayLayout.setVisibility(View.INVISIBLE);
			twodayLayout.setVisibility(View.INVISIBLE);
			threedayLayout.setVisibility(View.INVISIBLE);
			
			cityNameText.setVisibility(View.INVISIBLE);
			publishText.setText("同步中······");
			queryWeatherCode(countyCode);
			//refreshWeatherInfo ();
			
		} else {
			//没有县级代号时就直接显示本地天气
			
			showWeather();
		}
		
		//向特别关注列表添加城市
		
		sclist = coolWeatherDB.loadSpecials();
		adapter = new SpecialCityAdapter(NewWeatherActivity.this, 
				R.layout.special_city, sclist);
		
		cityList = (ListView) findViewById(R.id.specialcitylist);
		cityList.setAdapter(adapter);
		cityList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				SpecialCity specialCity = sclist.get(index);
				slideCountyCode = specialCity.getCountyCode();
				publishText.setText("同步中······");
				queryWeatherCode(slideCountyCode);
				drawerLayout.closeDrawers();
			}
			
		});
		
		//实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		//获取广告条布局
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		//将广告条加入布局
		adLayout.addView(adView);
		
	}
	
	/**
	 *复写按钮的点击事件 
	 */
	@Override
	public void onClick (View v) {
		switch (v.getId()) {
		case R.id.switch_city:
		case R.id.add_specialcity:
			Intent intent = new Intent(MyApplication.getContext(), ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		
		case R.id.refresh_weather:
			publishText.setText("同步中······");
			refreshWeatherInfo ();
			/*
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			weatherCode = prefs.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}
			*/
			break;
		
		case R.id.exit:
			finish();
			break;
			
		case R.id.show_floatwindow:
			if (showFloatWindow.isChecked()) {
				
				Toast.makeText(NewWeatherActivity.this, 
                		"若悬浮窗没有显示，请在设置-应用-找到酷看天气，并将显示悬浮框设置为true",
                		Toast.LENGTH_LONG).show();
				
				Intent intentShowFloat = new Intent(NewWeatherActivity.this, FxService.class);  
	                //启动FxService 
				String floatmessage = cityName + " " + weatherText + " " + temperRange;
				connection.setText(floatmessage);
				
                bindService(intentShowFloat, connection, BIND_AUTO_CREATE);
                
                if (floatAlertShowed) {
	                Toast.makeText(NewWeatherActivity.this, 
	                		"若悬浮窗没有显示，请在设置-应用-找到酷欧天气，将显示悬浮框设置为true",
	                		Toast.LENGTH_LONG).show();
	                floatAlertShowed = true;
                }
                //finish();
               
			} else {
				Intent intentShowFloat = new Intent(NewWeatherActivity.this, FxService.class);  
	                //终止FxService  
				unbindService(connection);
	            stopService(intentShowFloat);  
	            floatAlertShowed = false;
			}
			 break;
		default:
			break;
		}
	}
	
	
	/**
	 * 查询县级代号所对应的天气
	 */
	
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
		queryFromServer(address, "countyCode");
				
	}
	
	/**
	 * 使用新接口
	 * 查询天气代号所对应天气
	 */
	private void queryWeatherInfo (String weatherCode) {
		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + weatherCode;
		queryFromServer(address, "weatherCode");
		
		/*
		SpecialCity specialCity = new SpecialCity();
		specialCity.setCityName(cityName);
		specialCity.setWeatherCode(weatherCode);
		coolWeatherDB.saveSpecial(specialCity);
		*/
		//Toast.makeText(NewWeatherActivity.this, "天气已更新", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 根据传人的地址和类型去向服务器查询天气代号或天气信息
	 */
	
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener (){

			@Override
			public void onFinish(final String response) {
				if ("countyCode".equals(type)) {
					if(!TextUtils.isEmpty(response)) {
						//从服务器返回的数据中解析出天气代号
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if("weatherCode".equals(type)) {
					//处理服务器返回的天气信息
					if (weatherCode != null) {
						Utility.handleWeatherResponse2 (NewWeatherActivity.this, response, 
								weatherCode);
					}
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							/*
							SpecialCity specialCity = new SpecialCity(cityName, weatherCode);
						    coolWeatherDB.saveSpecial(specialCity);
							*/
							showWeather();
						}
						
					});
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						publishText.setText("(╯‵□′)╯︵┻━┻  同步失败啦！");
						
					}
					
				});
			}
		
		});
	}
	/**
	 * 从SharedPreference文件中读取存储的天气信息
	 */
	
	private void readWeatherInfo() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityName = prefs.getString("city", "");
		coldAlert = prefs.getString("coldAlert", "");
		
		weatherText = prefs.getString("type", "");
		weatherText1 = prefs.getString("type1", "");
		weatherText2 = prefs.getString("type2", "");
		weatherText3 = prefs.getString("type3", "");
		
		date1 = prefs.getString("date1", "");
		date2 = prefs.getString("date2", "");
		date3 = prefs.getString("date3", "");
		
		temperRange = divideTemp(prefs.getString("low", ""), prefs.getString("high", ""));
		temperRange1 = divideTemp(prefs.getString("low1", ""), prefs.getString("high1", ""));
		temperRange2 = divideTemp(prefs.getString("low2", ""), prefs.getString("high2", ""));
		temperRange3 = divideTemp(prefs.getString("low3", ""), prefs.getString("high3", ""));
	}
	
	/**
	 * 分割返回温度并重新拼接温度字符串的方法
	 */
	
	private String divideTemp(String lowTemp, String highTemp) {
		String[] lowTempArgs = lowTemp.split(" ");
		String[] highTempArgs = highTemp.split(" ");
		String dividedTemp = lowTempArgs[1] + " ~ " + highTempArgs[1];
		return dividedTemp;
	}
	/**
	 *从SharedPreferences文件中读取存储的天气信息并显示到界面上
	 */
	private void showWeather() {
		
		//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		readWeatherInfo();
		
		cityNameText.setText(cityName);
		publishText.setText("今天天气");
		coldAlertTextView.setText(coldAlert);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		currentDateText.setText(sdf.format(new Date()));
		
		typeText.setText(weatherText);
		type1Text.setText(weatherText1);
		type2Text.setText(weatherText2);
		type3Text.setText(weatherText3);
		
		tempRange.setText(temperRange);
		tempRange1.setText(temperRange1);
		tempRange2.setText(temperRange2);
		tempRange3.setText(temperRange3);
		
		date1Text.setText(date1);
		date2Text.setText(date2);
		date3Text.setText(date3);
		
		todayLayout.setVisibility(View.VISIBLE);
		onedayLayout.setVisibility(View.VISIBLE);
		twodayLayout.setVisibility(View.VISIBLE);
		threedayLayout.setVisibility(View.VISIBLE);
		
		cityNameText.setVisibility(View.VISIBLE);
		
		changeWeatherBackground(weatherText, weatherBackgroundLayout);
		changeWeatherBackground(weatherText1, onedayLayout);
		changeWeatherBackground(weatherText2, twodayLayout);
		changeWeatherBackground(weatherText3, threedayLayout);
		
		showNotice();
	
		
		
		//激活AutoUpdateService服务
		
		
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
		
		//刷新ListView
		//SpecialCity sc = new SpecialCity(cityName, countyCode);
		//sclist.add(sc);
		//adapter.notifyDataSetChanged();
		//cityList.setSelection(sclist.size());
		
		
		
	}
	
	/**
	 * 
	 */
	private  void changeWeatherBackground(String weatherText, View layoutView) {
		
		if (("小雨".equals(weatherText)) || ("中雨".equals(weatherText)) ||
				("阵雨".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.rain));					
			//小到中雨的背景
			
		} else if (("大雨".equals(weatherText)) || ("暴雨".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.rainstorm));
			//大雨和暴雨的背景
			
		} else if (("雷雨".equals(weatherText)) || ("雷阵雨".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.thunder));
			//雷雨和雷阵雨的背景
			
		} else if (("小雪".equals(weatherText)) || ("中雪".equals(weatherText))
				|| ("大雪".equals(weatherText)) || ("暴雪".equals(weatherText))
				|| ("阵雪".equals(weatherText)) || ("雨夹雪".equals(weatherText))
				|| ("雨加雪".equals(weatherText)) || ("雪".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.snow));
			//所有下雪的背景
			
		} else if (("雾".equals(weatherText)) || ("霾".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.fog));
			//雾霾的背景
			
		} else if (("冰雹".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.hailstone));
			//冰雹的背景
			
		} else if (("沙尘暴".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.sandstorm));
			//沙尘暴的背景
			
		} else if (("多云".equals(weatherText)) || ("少云".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.cloud));
			//有云时的背景
			
		}  else if (("阴".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.overcast));
			//阴天的背景
			
		} else if (("晴".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.sun));
			//晴天的背景
			
		}
	}
	
	/**
	 * 重写方法 让其不关闭现有活动
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {  
            moveTaskToBack(false);  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * UI更新广播接收器
	 */
	class UIRefreshReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			refreshWeatherInfo();
			//Toast.makeText(MyApplication.getContext(), "UI更新", Toast.LENGTH_SHORT).show();
		}
		
	}

}

