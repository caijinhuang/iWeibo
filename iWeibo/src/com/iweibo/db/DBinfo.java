package com.iweibo.db;

public class DBinfo {
	public static class DB{
		/**
		 * 数据库名称
		 */
		public static final String DB_NAME = "cjh_iweibo.db";
		/**
		 * 数据库版本
		 */
		public static final int VERSION = 1;
	}
	
	public static class Table{
		/**
		 * 数据表名
		 */
		public static final String USER_TABLE = "userinfo";
		/**
		 * 数据表属性
		 */
		public static final String _ID = "_id";
		public static final String USER_ID = "user_id";//用户uid
		public static final String USER_NAME = "user_name";//用户名
		public static final String TAKEN = "taken";
		public static final String TAKEN_SECRET = "taken_secret";
		public static final String DESCRIPTION = "description";//用户描述
		public static final String USER_HEAD = "user_head";//用户头像
		public static final String U_RefreshToken = "user_head";//用户头像
		public static final String U_ExpiresTime = "user_head";//用户头像
		/**
		 * SQL执行语句
		 */
		public static final String CREATE_USER_TABLE = "create table if not exists "
				+USER_TABLE+"("
				+_ID+" integer primary key autoincrement, "
				+USER_ID+" text, "
				+USER_NAME+" text, "
				+TAKEN+" text, "
				+TAKEN_SECRET+" text, "
				+DESCRIPTION+" text, "
				+USER_HEAD+" BLOB);";
		
		public static final String DROP_USER_TABLE = "drop table "+USER_TABLE;
	}
}
