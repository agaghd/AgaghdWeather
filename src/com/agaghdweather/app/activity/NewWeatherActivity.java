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
	 * UI�㲥����ʵ��
	 */
	private UIRefreshReceiver uiRefreshReceiver;
	
	private LocalBroadcastManager uiRefreshBroadcastManager;
	
	private IntentFilter intentFilter;

	/**
	 * ������ʾ���켰δ�������������С���ڲ���
	 */
	private LinearLayout todayLayout;
	private LinearLayout onedayLayout;
	private LinearLayout twodayLayout;
	private LinearLayout threedayLayout;
	private DrawerLayout drawerLayout;
	
 
	/**
	 * ��������
	 */
	
	private RelativeLayout weatherBackgroundLayout;
	
	
	/**
	 * ������ʾ������
	 */
	private TextView cityNameText;
	
	/**
	 * �໬�������б�
	 */
	private ListView cityList;
	
	/**
	 * �໬����������
	 */
	
	private List<SpecialCity> sclist;
	
	/**
	 * �໬�������б�������
	 */
	SpecialCityAdapter adapter;
	
	/**
	 * �໬������code
	 */
	String slideCountyCode;
	
	/**
	 *CheckBoxѡ���Ƿ���ʾ������ 
	 */
	private CheckBox showFloatWindow;
	
	/**
	 * ��������ʾ�Ƿ���ʾ��
	 */
	
	private Boolean floatAlertShowed;
	
	/**
	 * ��ȡshowTextBinderʵ��
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
	 * �˳���ť
	 */
	private Button exitButton; 	
	
	/**
	 * ������ʾ��������
	 */
	private TextView publishText;
	
	/**
	 * ������ʾ���켰δ��������������
	 */
	private TextView typeText;
	private TextView type1Text;
	private TextView type2Text;
	private TextView type3Text;
	
	/**
	 * ������ʾ���켰δ����������
	 */
	private TextView tempRange;	
	private TextView tempRange1;	
	private TextView tempRange2;	
	private TextView tempRange3;	
	
	
	
	/**
	 * ������ʾ��ǰ��δ����������
	 */
	private TextView currentDateText;
	private TextView date1Text;
	private TextView date2Text;
	private TextView date3Text;
	
	/**
	 * ��ʾ��ðС��ʿ
	 */
	private TextView coldAlertTextView;
	
	/**
	 * �л����а�ť
	 */
	private Button switchCity;
	
	/**
	 * ����ر���ĳ��а�ť
	 */
	
	private Button addSpecialCity;
	
	/**
	 * ����������ť
	 */
	
	private Button refreshWeather;
	
	/**
	 * ��������
	 */
	String weatherCode;
	
	/**
	 * �سǴ���
	 */
	String countyCode;
	
	/**
	 * ���� ���� �¶� ���� ��ðָ��
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
	 * ���ݿ�
	 * 
	 */
	
	private CoolWeatherDB coolWeatherDB;
	
	
	/**
	 * Notify�л�����Ĵ���
	 */
	
	private static final int NEWWEATHERACTIVITY = 1;
	
	/**
	 * 2016-2-18
	 * ˢ��������Ϣ�ķ���
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
				"�����Ѹ���", System.currentTimeMillis());
		Intent intent = new Intent(NewWeatherActivity.this, NewWeatherActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(NewWeatherActivity.this,
				0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(NewWeatherActivity.this, 
				cityName, weatherText + " " + temperRange, pendingIntent);
		manager.notify(NEWWEATHERACTIVITY, notification);
		
		//�������������������ͬʱ����������������
		if (showFloatWindow.isChecked()) {
			//����ֹ���񲢽���󶨣������¿�������
			Intent intentShowFloat = new Intent(NewWeatherActivity.this, FxService.class);  
            //��ֹFxService  
			unbindService(connection);
			stopService(intentShowFloat);			
			//Intent intentShowFloat = new Intent(NewWeatherActivity.this, FxService.class);  
                //����FxService 
			String floatmessage = cityName + " " + weatherText + " " + temperRange;
			connection.setText(floatmessage);			
            bindService(intentShowFloat, connection, BIND_AUTO_CREATE);
		}
		Toast.makeText(MyApplication.getContext(), "�����Ѹ���", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_weather_layout);
		
		/**
		 * ��ʼ�����ؼ�
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
		
		//��ʼ�����Ʊ���
		floatAlertShowed = false;
		
		//ע��UI���չ㲥
		uiRefreshBroadcastManager = LocalBroadcastManager.getInstance
				(this);
		intentFilter = new IntentFilter();
		intentFilter.addAction("com.coolweather.app.UIREFRESH_BROADCAST");
		uiRefreshReceiver = new UIRefreshReceiver();
		uiRefreshBroadcastManager.registerReceiver(uiRefreshReceiver, intentFilter);
		
		if(!TextUtils.isEmpty(countyCode)) {
			//���ؼ�����ʱ��ȥ������
			publishText.setText("ͬ���С�����������");
			todayLayout.setVisibility(View.INVISIBLE);
			onedayLayout.setVisibility(View.INVISIBLE);
			twodayLayout.setVisibility(View.INVISIBLE);
			threedayLayout.setVisibility(View.INVISIBLE);
			
			cityNameText.setVisibility(View.INVISIBLE);
			publishText.setText("ͬ���С�����������");
			queryWeatherCode(countyCode);
			//refreshWeatherInfo ();
			
		} else {
			//û���ؼ�����ʱ��ֱ����ʾ��������
			
			showWeather();
		}
		
		//���ر��ע�б���ӳ���
		
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
				publishText.setText("ͬ���С�����������");
				queryWeatherCode(slideCountyCode);
				drawerLayout.closeDrawers();
			}
			
		});
		
		//ʵ���������
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		//��ȡ���������
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		//����������벼��
		adLayout.addView(adView);
		
	}
	
	/**
	 *��д��ť�ĵ���¼� 
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
			publishText.setText("ͬ���С�����������");
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
                		"��������û����ʾ����������-Ӧ��-�ҵ��ῴ������������ʾ����������Ϊtrue",
                		Toast.LENGTH_LONG).show();
				
				Intent intentShowFloat = new Intent(NewWeatherActivity.this, FxService.class);  
	                //����FxService 
				String floatmessage = cityName + " " + weatherText + " " + temperRange;
				connection.setText(floatmessage);
				
                bindService(intentShowFloat, connection, BIND_AUTO_CREATE);
                
                if (floatAlertShowed) {
	                Toast.makeText(NewWeatherActivity.this, 
	                		"��������û����ʾ����������-Ӧ��-�ҵ���ŷ����������ʾ����������Ϊtrue",
	                		Toast.LENGTH_LONG).show();
	                floatAlertShowed = true;
                }
                //finish();
               
			} else {
				Intent intentShowFloat = new Intent(NewWeatherActivity.this, FxService.class);  
	                //��ֹFxService  
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
	 * ��ѯ�ؼ���������Ӧ������
	 */
	
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
		queryFromServer(address, "countyCode");
				
	}
	
	/**
	 * ʹ���½ӿ�
	 * ��ѯ������������Ӧ����
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
		//Toast.makeText(NewWeatherActivity.this, "�����Ѹ���", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * ���ݴ��˵ĵ�ַ������ȥ���������ѯ�������Ż�������Ϣ
	 */
	
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener (){

			@Override
			public void onFinish(final String response) {
				if ("countyCode".equals(type)) {
					if(!TextUtils.isEmpty(response)) {
						//�ӷ��������ص������н�������������
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if("weatherCode".equals(type)) {
					//������������ص�������Ϣ
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
						publishText.setText("(�s�F����)�s��ߩ���  ͬ��ʧ������");
						
					}
					
				});
			}
		
		});
	}
	/**
	 * ��SharedPreference�ļ��ж�ȡ�洢��������Ϣ
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
	 * �ָ���¶Ȳ�����ƴ���¶��ַ����ķ���
	 */
	
	private String divideTemp(String lowTemp, String highTemp) {
		String[] lowTempArgs = lowTemp.split(" ");
		String[] highTempArgs = highTemp.split(" ");
		String dividedTemp = lowTempArgs[1] + " ~ " + highTempArgs[1];
		return dividedTemp;
	}
	/**
	 *��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ����ʾ��������
	 */
	private void showWeather() {
		
		//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		readWeatherInfo();
		
		cityNameText.setText(cityName);
		publishText.setText("��������");
		coldAlertTextView.setText(coldAlert);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��",Locale.CHINA);
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
	
		
		
		//����AutoUpdateService����
		
		
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
		
		//ˢ��ListView
		//SpecialCity sc = new SpecialCity(cityName, countyCode);
		//sclist.add(sc);
		//adapter.notifyDataSetChanged();
		//cityList.setSelection(sclist.size());
		
		
		
	}
	
	/**
	 * 
	 */
	private  void changeWeatherBackground(String weatherText, View layoutView) {
		
		if (("С��".equals(weatherText)) || ("����".equals(weatherText)) ||
				("����".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.rain));					
			//С������ı���
			
		} else if (("����".equals(weatherText)) || ("����".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.rainstorm));
			//����ͱ���ı���
			
		} else if (("����".equals(weatherText)) || ("������".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.thunder));
			//�����������ı���
			
		} else if (("Сѩ".equals(weatherText)) || ("��ѩ".equals(weatherText))
				|| ("��ѩ".equals(weatherText)) || ("��ѩ".equals(weatherText))
				|| ("��ѩ".equals(weatherText)) || ("���ѩ".equals(weatherText))
				|| ("���ѩ".equals(weatherText)) || ("ѩ".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.snow));
			//������ѩ�ı���
			
		} else if (("��".equals(weatherText)) || ("��".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.fog));
			//�����ı���
			
		} else if (("����".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.hailstone));
			//�����ı���
			
		} else if (("ɳ����".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.sandstorm));
			//ɳ�����ı���
			
		} else if (("����".equals(weatherText)) || ("����".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.cloud));
			//����ʱ�ı���
			
		}  else if (("��".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.overcast));
			//����ı���
			
		} else if (("��".equals(weatherText))) {
			layoutView.setBackground(NewWeatherActivity.this.getResources()
					.getDrawable(R.drawable.sun));
			//����ı���
			
		}
	}
	
	/**
	 * ��д���� ���䲻�ر����л
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {  
            moveTaskToBack(false);  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * UI���¹㲥������
	 */
	class UIRefreshReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			refreshWeatherInfo();
			//Toast.makeText(MyApplication.getContext(), "UI����", Toast.LENGTH_SHORT).show();
		}
		
	}

}

