package com.iweibo.util;


import com.example.iweibo.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.openapi.UsersAPI;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class AuthListener implements WeiboAuthListener{
	private Oauth2AccessToken mAccessToken = null;
	private UsersAPI uApi ;
	private Context context;
	private RequestListener_oauth mListener;
	/**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
		public AuthListener(Context context) {
			this.context = context; 
		}
		@Override
		public void onCancel() {
			Toast.makeText(context, 
	                   com.example.iweibo.R.string.string_weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(context, mAccessToken);
                Toast.makeText(context, 
                        R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
                /**
                 * 将信息保存到数库
                 */
                //打印出uid,access_token
                System.out.println(mAccessToken.getUid());
                System.out.println(mAccessToken.getRefreshToken());
                //打印用户信息
                uApi = new UsersAPI(context, Constants.APP_KEY, mAccessToken);
                long uid = Long.parseLong(mAccessToken.getUid());
                mListener = new RequestListener_oauth(context, mAccessToken);
                uApi.show(uid, mListener);
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = context.getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(context, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		public static class User_token{
			public static Oauth2AccessToken now_AccessToken;
		}
}


