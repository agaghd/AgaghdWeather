package com.agaghdweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	/*
	 *Provice表建表语句
	 */
	
	public static final String CREATE_PROVINCE = "create table Province ("
			+ "id integer primary key autoincrement,"
			+ "province_name text, "
			+ "province_code text)";
	
	/*
	 * City表建表语句
	 */
	
	public static final String CREATE_CITY = "create table City("
			+ "id integer primary key autoincrement,"
			+ "city_name text, "
			+ "city_code text, "
			+ "province_id integer)";
	
	/*
	 * County表建表语句
	 */
	
	public static final String CREATE_COUNTY = "create table County ("
			+ "id integer primary key autoincrement, "
			+ "county_name text, "
			+ "county_code text, "
			+ "city_id integer)";
	
	/*
	 * SpecialCity表建表语句
	 */
	
	public static final String CREATE_SPECIAL = "create table Special ("
			
			+ "special_name text primary key, "
			
			+ "special_code text)";
	
	/*
	 * CoolWeatherOpenHelper的4参数构造方法
	 * 第一个参数:Context
	 * 第二个参数:数据库名
	 * 第三个参数:Cursor,通常传人null
	 * 第四个参数:数据库版本Version
	 */
	
	public CoolWeatherOpenHelper (Context context, String name, CursorFactory factory, 
									int version) {
		super(context, name, factory, version);
	}
	
	/*
	 * 复写onCreate方法
	 */
	
	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);	//创建Province表
		db.execSQL(CREATE_CITY);		//创建City表
		db.execSQL(CREATE_COUNTY);		//创建County表
		db.execSQL(CREATE_SPECIAL);		//创建Special表
		
	}
			
	/*
	 * 复写onUpGrade方法
	 */

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	

}
