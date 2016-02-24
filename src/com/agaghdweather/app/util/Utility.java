/**
 * �������ݵĹ�����
 */
package com.agaghdweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.security.auth.PrivateCredentialPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.agaghdweather.app.db.CoolWeatherOpenHelper;
import com.agaghdweather.app.model.City;
import com.agaghdweather.app.model.CoolWeatherDB;
import com.agaghdweather.app.model.County;
import com.agaghdweather.app.model.Province;
import com.agaghdweather.app.model.SpecialCity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Utility {
	
	/**
	 * �����ʹ�����������ص�ʡ������
	 */
	
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB
			, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");	//��","�ָ��ַ����󱣴����ַ�������allCities��
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {			//����allCities�е�ÿ���ַ���
					String[] array = p.split("\\|");	//���ַ�����"|"�ָ�,�������ַ�������array��
					Province province = new Province();		
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//���������������ݴ洢��Province����
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ʹ�����������ص��м�����
	 */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, 
			String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length >0 ) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
					
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �����ʹ�����������ص��ؼ�����
	 */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,
			String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (allCounties != null && allCounties.length>0) {
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					//���������������ݴ洢��County��
					
					Log.d("eee","CountyCode is " + array[0]);
					Log.d("eee","CountyName is " + array[1]);
					
					Log.d("eee", "cityId is " + cityId);
					
					coolWeatherDB.saveCounty(county);
					//Log.d("ddd", "saveCounty success");
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �������������ص�JSON���ݣ����������������ݴ洢������
	 */
	public static void handleWeatherResponse (Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp  = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, 
					weatherDesp, publishTime);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ʹ����һ�������ӿ� http://wthrcdn.etouch.cn/weather_mini?citykey=101010100
	 * ����JSON����
	 */
	public static void handleWeatherResponse2 (Context context, String response,
			String weatherCode) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject data = jsonObject.getJSONObject("data");
			String city = data.getString("city");			//��ȡ������
			String coldAlert = data.getString("ganmao");	//��ȡ��ðԤ����Ϣ
			JSONArray jsonArray = data.getJSONArray("forecast");//��ȡ5���ڵ�������Ϣ
			
			JSONObject todayWeather = jsonArray.getJSONObject(0);	//��ȡ����������Ϣ
			String type = todayWeather.getString("type");		//��ȡ��������
			String high = todayWeather.getString("high");		//��ȡ�������
			String low = todayWeather.getString("low");			//��ȡ�������
			
			JSONObject oneDayWeather = jsonArray.getJSONObject(1);	//��ȡδ����һ���������Ϣ
			String type1 = oneDayWeather.getString("type");		//��ȡ��������
			String high1 = oneDayWeather.getString("high");		//��ȡ�������
			String low1 = oneDayWeather.getString("low");			//��ȡ�������
			
			JSONObject twoDayWeather = jsonArray.getJSONObject(2);	//��ȡδ���ڶ����������Ϣ
			String type2 = twoDayWeather.getString("type");		//��ȡ��������
			String high2= twoDayWeather.getString("high");		//��ȡ�������
			String low2 = twoDayWeather.getString("low");			//��ȡ�������
			
			JSONObject threeDayWeather = jsonArray.getJSONObject(3);	//��ȡδ���ڶ����������Ϣ
			String type3 = threeDayWeather.getString("type");		//��ȡ��������
			String high3= threeDayWeather.getString("high");		//��ȡ�������
			String low3 = threeDayWeather.getString("low");			//��ȡ�������

			//�洢���յ�������Ϣ
			saveWeatherInfo2 (0, context, weatherCode, city, coldAlert, type, low, high,
					type1, low1, high1,
					type2, low2, high2,
					type3, low3, high3);	
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��һ�������ӿ����ݵĴ洢����
	 * �����������ص�������Ϣ�洢��SharedPreferences�ļ���
	 */


	private static void saveWeatherInfo2(int day, Context context,
			String weatherCode, String city, String coldAlert, String type, String low, String high,
			String type1, String low1, String high1,
			String type2, String low2, String high2,
			String type3, String low3, String high3) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city", city);
		editor.putString("weather_code", weatherCode);
		
		editor.putString("low", low);
		editor.putString("high", high);
		editor.putString("type", type);
		
		editor.putString("low1", low1);
		editor.putString("high1", high1);
		editor.putString("type1", type1);
		
		editor.putString("low2", low2);
		editor.putString("high2", high2);
		editor.putString("type2", type2);
		
		editor.putString("low3", low3);
		editor.putString("high3", high3);
		editor.putString("type3", type3);
		
		editor.commit();
		
	
	 
		
	}

	/**
	 * �����������ص�����������Ϣ�洢��SharedPreferences�ļ���
	 */
	
	public static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2,
			String weatherDesp, String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��D��", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
		
		
		
		
	}
	

	
}
