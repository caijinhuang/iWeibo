package com.iweibo;

import java.util.List;

import com.example.iweibo.R;
import com.iweibo.adpter_.Spinner_Adapter;
import com.iweibo.dao.UserDao;
import com.iweibo.pojo.User;
import com.iweibo.util.AuthListener;
import com.iweibo.util.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class LoginActivity extends Activity implements OnClickListener {
	private Button bt_login, bt_oauth;
	private Spinner screen_name_list;
	private AuthInfo mAuthInfo;
	private SsoHandler mSsoHandler;
	private List<User> userlist = null;
	private FinishReceiver mFinish;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/**
		 * 注册自定义广播
		 */
		mFinish = new FinishReceiver();
		registerReceiver(mFinish, new IntentFilter("finishActivity"));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		UserSelected.context = LoginActivity.this;
		// 防止多线程阻塞
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		final ImageView image = (ImageView) findViewById(R.id.head);
		final TextView screen_name = (TextView) findViewById(R.id.screen_name);
		init_user();//获取授权用户信息
		
		// 获取用户数据(使用列表监听)
		screen_name_list.setOnItemSelectedListener(new OnItemSelectedListener() {
			String description;
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				User u = userlist.get(position);
				image.setBackground(u.getUser_head());
				description = u.getDescription();
				// 保存当前登录的用户
				UserSelected.nowUser = u;
				if (!description.equals(""))
					screen_name.setText("个人描述:  " + description);
				else
					screen_name.setText("个人描述:  我的微博，我做主！");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				image.setBackground(userlist.get(userlist.size() - 1).getUser_head());
				description = userlist.get(userlist.size() - 1).getDescription();
				if (!description.equals(""))
					screen_name.setText("个人描述:  " + description);
				else
					screen_name.setText("个人描述:  我的微博，我做主！");
			}

		});
		// 获取按钮
		bt_login = (Button) super.findViewById(R.id.but_login);
		bt_oauth = (Button) super.findViewById(R.id.but_oauth);
		bt_login.setOnClickListener(this);
		bt_oauth.setOnClickListener(this);
		// 认证信息实例化
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 登录
		case R.id.but_login:
			Intent i = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(i);
			// finish();
			break;
		// 授权
		case R.id.but_oauth:
			mSsoHandler.authorizeWeb(new AuthListener(LoginActivity.this));
			break;
		}
	}
	//获取已授权用户信息
	public void init_user(){
		UserDao dao = new UserDao(this);
		userlist = dao.findAllUser();
		UserSelected.userNum = userlist.size();
		// 设置下拉框
		screen_name_list = (Spinner) super.findViewById(R.id.screen_name_list);
		screen_name_list.setAdapter(new Spinner_Adapter(LoginActivity.this, userlist));
	}
	/**
	 * 广播接收事件
	 */
	private class FinishReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
	
	/**
	 * 保存当前登录的用户
	 * 
	 * @author Cjh
	 *
	 */
	public static class UserSelected {
		public static User nowUser;
		public static int userNum = 0;
		public static Context context=null;
		public static boolean isfinish = false;
	}
}
