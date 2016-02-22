/**
 * 封装常用的 数据库操作
 */
package com.agaghdweather.app.model;

import java.util.ArrayList;
import java.util.List;

import com.agaghdweather.app.db.CoolWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CoolWeatherDB {
	
	/**
	 * 数据库名
	 */
	
	public static final String DB_NAME = "cool_weather.db";
	
	/**
	 * 数据库版本
	 */
	
	public static final int VERSION = 1;
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	
	/**
	 * 将构造方法私有化
	 */
	
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, 
				null, VERSION);
		db = dbHelper.getWritableDatabase();		//获取可操作的数据库
	}
	
	/**
	 * 获取CoolWeatherDB的实例
	 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if(coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/**
	 * 将Province实例存储到数据库
	 * 存储到Province表中	
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
			
			
		}
		
	}
	
	
	/**
	 * 从数据库读取全国所有省份信息
	 */
	
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		
		if(cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while(cursor.moveToNext());
		}
		
		if (cursor != null) {
			cursor.close();
		}
		
		return list;
	}
	
	/**
	 * 将City实例存储到数据库中
	 */
	
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	
	/**
	 * 从数据库读取某省下所有的城市信息
	 */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
	
	
	/**
	 *将County实例存储到数据库中 
	 */
	public void saveCounty (County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			
			/*
			Log.d("insert", "countyName is " + county.getCountyName());
			Log.d("insert", "countyCode is " + county.getCountyCode());
			Log.d("insert", "cityId is " + county.getCityId());
			*/
			
			db.insert("County", null, values);
			
		}
	}
	
	/**
	 * 从数据库读取某城市下所有的县的信息
	 * 
	 */
	
	/**
	 * 服务器返回数据CityID一律为0 ？？？？？？？？？？？？？？？？？
	 */
	public List<County> loadCounties(int cityId) {
		Log.d("size", "cityId is " + cityId);
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[] {	String.valueOf(cityId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
				//Log.d("ddd","load county data successful");
			} while (cursor.moveToNext());
			
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 数据库存储特别关注的城市信息
	 */
	public void saveSpecial(SpecialCity specialCity) {
		if (specialCity != null) {
			ContentValues values = new ContentValues();
			values.put("special_name", specialCity.getCountyName());
			values.put("special_code", specialCity.getCountyCode());
			db.replace("Special", null, values);
		}
	}
	
	/**
	 * 从数据库读取特别关注的城市信息
	 */
	public List<SpecialCity> loadSpecials () {
		List<SpecialCity> list = new ArrayList<SpecialCity>();
		Cursor cursor = db.query("Special", null, null,
				null, null, null, null);
		if(cursor.moveToFirst()) {
			do {
				SpecialCity specialCity = new SpecialCity();
				
				specialCity.setCountyName(cursor.getString
						(cursor.getColumnIndex("special_name")));
				specialCity.setCountyCode(cursor.getString
						(cursor.getColumnIndex("special_code")));
				list.add(specialCity);
			} while (cursor.moveToNext());
			
		}
		if (cursor != null) {
			cursor.close();
			
		}
		return list;
	}
	
	/**
	 * 从Special删除数据
	 */
	public void deleteSpecial(String countyName) {
		
		db.delete("Special", "special_name = ?", new String[] { countyName });
	}
	
}
