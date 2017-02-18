package com.iweibo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据库创建
 * @author Administrator
 *
 */
public class DBhelper extends SQLiteOpenHelper {
	/**
	 * 
	 * @param context 上下文
	 * @param name 数据库名称
	 * @param factory 指针工厂
	 * @param version 版本
	 */
	public DBhelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	public DBhelper(Context context){
		this(context,DBinfo.DB.DB_NAME, null, DBinfo.DB.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBinfo.Table.CREATE_USER_TABLE);//创建数据表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DBinfo.Table.DROP_USER_TABLE);//删除数据表
		onCreate(db);
	}

}
