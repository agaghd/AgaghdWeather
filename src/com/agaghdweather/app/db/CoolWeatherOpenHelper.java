package com.agaghdweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	/*
	 *Provice�������
	 */
	
	public static final String CREATE_PROVINCE = "create table Province ("
			+ "id integer primary key autoincrement,"
			+ "province_name text, "
			+ "province_code text)";
	
	/*
	 * City�������
	 */
	
	public static final String CREATE_CITY = "create table City("
			+ "id integer primary key autoincrement,"
			+ "city_name text, "
			+ "city_code text, "
			+ "province_id integer)";
	
	/*
	 * County�������
	 */
	
	public static final String CREATE_COUNTY = "create table County ("
			+ "id integer primary key autoincrement, "
			+ "county_name text, "
			+ "county_code text, "
			+ "city_id integer)";
	
	/*
	 * SpecialCity�������
	 */
	
	public static final String CREATE_SPECIAL = "create table Special ("
			
			+ "special_name text primary key, "
			
			+ "special_code text)";
	
	/*
	 * CoolWeatherOpenHelper��4�������췽��
	 * ��һ������:Context
	 * �ڶ�������:���ݿ���
	 * ����������:Cursor,ͨ������null
	 * ���ĸ�����:���ݿ�汾Version
	 */
	
	public CoolWeatherOpenHelper (Context context, String name, CursorFactory factory, 
									int version) {
		super(context, name, factory, version);
	}
	
	/*
	 * ��дonCreate����
	 */
	
	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);	//����Province��
		db.execSQL(CREATE_CITY);		//����City��
		db.execSQL(CREATE_COUNTY);		//����County��
		db.execSQL(CREATE_SPECIAL);		//����Special��
		
	}
			
	/*
	 * ��дonUpGrade����
	 */

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	

}
