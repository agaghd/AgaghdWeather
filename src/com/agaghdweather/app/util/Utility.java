/**
 * 解析数据的工具类
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
	 * 解析和处理服务器返回的省级数据
	 */
	
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB
			, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");	//以","分割字符串后保存在字符串数组allCities中
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {			//遍历allCities中的每个字符串
					String[] array = p.split("\\|");	//将字符串以"|"分割,保存在字符串数组array中
					Province province = new Province();		
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//将解析出来的数据存储到Province表中
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的市级数据
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
	 * 解析和处理服务器返回的县级数据
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
					//将解析出来的数据存储到County表
					
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
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
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
	 * 使用另一个天气接口 http://wthrcdn.etouch.cn/weather_mini?citykey=101010100
	 * 解析JSON数据
	 */
	public static void handleWeatherResponse2 (Context context, String response,
			String weatherCode) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject data = jsonObject.getJSONObject("data");
			String city = data.getString("city");			//获取城市名
			String coldAlert = data.getString("ganmao");	//获取感冒预警信息
			JSONArray jsonArray = data.getJSONArray("forecast");//获取5天内的天气信息
			
			JSONObject todayWeather = jsonArray.getJSONObject(0);	//获取当天天气信息
			String type = todayWeather.getString("type");		//获取天气类型
			String high = todayWeather.getString("high");		//获取最高气温
			String low = todayWeather.getString("low");			//获取最低气温
			
			JSONObject oneDayWeather = jsonArray.getJSONObject(1);	//获取未来第一天的天气信息
			String type1 = oneDayWeather.getString("type");		//获取天气类型
			String high1 = oneDayWeather.getString("high");		//获取最高气温
			String low1 = oneDayWeather.getString("low");			//获取最低气温
			
			JSONObject twoDayWeather = jsonArray.getJSONObject(2);	//获取未来第二天的天气信息
			String type2 = twoDayWeather.getString("type");		//获取天气类型
			String high2= twoDayWeather.getString("high");		//获取最高气温
			String low2 = twoDayWeather.getString("low");			//获取最低气温
			
			JSONObject threeDayWeather = jsonArray.getJSONObject(3);	//获取未来第二天的天气信息
			String type3 = threeDayWeather.getString("type");		//获取天气类型
			String high3= threeDayWeather.getString("high");		//获取最高气温
			String low3 = threeDayWeather.getString("low");			//获取最低气温

			//存储各日的天气信息
			saveWeatherInfo2 (0, context, weatherCode, city, coldAlert, type, low, high,
					type1, low1, high1,
					type2, low2, high2,
					type3, low3, high3);	
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 另一种天气接口数据的存储方法
	 * 将服务器返回的天气信息存储到SharedPreferences文件中
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
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中
	 */
	
	public static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2,
			String weatherDesp, String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月D日", Locale.CHINA);
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
