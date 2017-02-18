package com.iweibo.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.iweibo.db.DBhelper;
import com.iweibo.db.DBinfo;
import com.iweibo.pojo.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class UserDao {
	private DBhelper dbhelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;

	public UserDao(Context context) {
		dbhelper = new DBhelper(context);
	}
	//获取要插入的值得
	public ContentValues getvalues(User user){
		values = new ContentValues();
		values.put(DBinfo.Table.USER_ID, user.getUser_id());
		values.put(DBinfo.Table.USER_NAME, user.getUser_name());
		values.put(DBinfo.Table.TAKEN, user.getTaken());
		values.put(DBinfo.Table.TAKEN_SECRET, user.getAccess_taken());
		values.put(DBinfo.Table.DESCRIPTION, user.getDescription());

		// 对图片进行特殊处理
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 转换格式
		BitmapDrawable newHead = (BitmapDrawable) user.getUser_head();
		// 插入图片，质量100%
		newHead.getBitmap().compress(CompressFormat.PNG, 100, os);
		// 插入列表
		values.put(DBinfo.Table.USER_HEAD, os.toByteArray());
		return values;
	}
	/**
	 * 数据库操作
	 * 
	 * @param user
	 * @return
	 */
	public long inserUser(User user) {
		// 获取写入权限
		db = dbhelper.getWritableDatabase();
		// 插入数据
		getvalues(user);
		// 插入一条数据
		long rowId = db.insert(DBinfo.Table.USER_TABLE, DBinfo.Table.USER_NAME, values);
		// 关闭数据库服务
		db.close();
		return 1;
	}

	/**
	 * 更新用户
	 * 
	 * @param user
	 * @return
	 */
	public int updateUser(User user) {
		// 更新列表
		db = dbhelper.getWritableDatabase();
		String id[] = {user.getUser_id()};
		// 插入数据
		Cursor cursor = db.query(DBinfo.Table.USER_TABLE, columns, DBinfo.Table.USER_ID+"=?", id, null, null,null);
		if (cursor != null && cursor.getCount()>0) {
			getvalues(user);
			db.update(DBinfo.Table.USER_TABLE, values, DBinfo.Table.USER_ID+"=?", id);
			System.out.println("更新");
		} else {
			inserUser(user);
		}
		db.close();
		return 1;
	}

	/**
	 * 删除用户
	 * 
	 * @param user_id
	 * @return
	 */
	public int deleteUser(String user_name) {
		// 获取写入权限
		db = dbhelper.getWritableDatabase();
		String sql = "delete from " + DBinfo.Table.USER_TABLE + " where " + DBinfo.Table.USER_NAME + "='" + user_name
				+ "'";
		System.out.println(sql);
		db.execSQL(sql);
		db.close();
		return 1;
	}

	/**
	 * 通过id查询用户
	 * 
	 * @param user_id
	 * @return
	 */
	public User findUserByUserId(String user_id) {
		return null;
	}

	/**
	 * User集合
	 */
	String[] columns = { DBinfo.Table._ID, DBinfo.Table.USER_ID, DBinfo.Table.USER_NAME, DBinfo.Table.TAKEN,
			DBinfo.Table.TAKEN_SECRET, DBinfo.Table.DESCRIPTION, DBinfo.Table.USER_HEAD };

	/**
	 * 获取所有用户信息
	 * 
	 * @return
	 */
	public List<User> findAllUser() {
		db = dbhelper.getReadableDatabase();
		List<User> userlist = null;
		User user = null;
		Cursor cursor = db.query(DBinfo.Table.USER_TABLE, columns, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			userlist = new ArrayList<User>(cursor.getCount());
			while (cursor.moveToNext()) {
				user = new User();
				user.set_id(cursor.getLong(cursor.getColumnIndex(DBinfo.Table._ID)));
				user.setUser_id(cursor.getString(cursor.getColumnIndex(DBinfo.Table.USER_ID)));
				user.setUser_name(cursor.getString(cursor.getColumnIndex(DBinfo.Table.USER_NAME)));
				user.setTaken(cursor.getString(cursor.getColumnIndex(DBinfo.Table.TAKEN)));
				user.setAccess_taken(cursor.getString(cursor.getColumnIndex(DBinfo.Table.TAKEN_SECRET)));
				user.setDescription(cursor.getString(cursor.getColumnIndex(DBinfo.Table.DESCRIPTION)));
				// 对头像进行转换设置才能进行保存
				byte[] byteHead = cursor.getBlob(cursor.getColumnIndex(DBinfo.Table.USER_HEAD));
				ByteArrayInputStream is = new ByteArrayInputStream(byteHead);
				Drawable userHead = Drawable.createFromStream(is, "image");
				user.setUser_head(userHead);
				userlist.add(user);
			}
		}
		cursor.close();
		db.close();
		return userlist;
	}
}
