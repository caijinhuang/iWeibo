package com.iweibo;

import com.example.iweibo.R;
import com.iweibo.util.AuthListener;
import com.iweibo.util.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 用户授权页面
 * 
 * @author CJH
 *
 */

public class OAuthActivity extends Activity {
	private Button oauth_start = null;// 授权按钮
	private Dialog dialog = null;// 对话框
	private SsoHandler mSsoHandler = null;
	private AuthInfo mAuthInfo = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oauth);
		// 防止多线程阻塞
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		// 自定义对话框
		View dialogView = View.inflate(this, R.layout.oauth_dialog, null);
		dialog = new Dialog(this, R.style.oauth_dialog);
		dialog.setContentView(dialogView);
		// 显示对话框
		dialog.show();
		// 必须是获取View对象上的按钮对象，否则会报错
		oauth_start = (Button) dialogView.findViewById(R.id.oauth_start);
		// 认证信息实例化
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		System.out.println(Constants.APP_KEY);
		//
		mSsoHandler = new SsoHandler(OAuthActivity.this, mAuthInfo);
		oauth_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSsoHandler.authorizeWeb(new AuthListener(OAuthActivity.this));
			}
		});
	}
}
