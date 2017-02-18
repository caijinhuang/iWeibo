package com.iweibo.util;

import java.util.List;

import com.iweibo.LoginActivity;
import com.iweibo.dao.UserDao;
import com.iweibo.pojo.User;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.utils.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;
/**
 * 授权完成并获取相应用户信息
 * @author Cjh
 *
 */
public class RequestListener_oauth implements RequestListener{
	private Context context;
	private Oauth2AccessToken mAccessToken = null;
	final private String TAG = "oauth";
	public RequestListener_oauth(Context context,Oauth2AccessToken mAccessToken) {
		this.context = context;
		this.mAccessToken = mAccessToken;
	}
	@Override
    public void onComplete(String response) {
        if (!TextUtils.isEmpty(response)) {
            LogUtil.i(TAG, response);
            // 调用 User#parse 将JSON串解析成User对象
            com.sina.weibo.sdk.openapi.models.User user = com.sina.weibo.sdk.openapi.models.User.parse(response);
            if (user != null) {
            	System.out.println("获取User信息成功，用户昵称：" + user.screen_name);
            	System.out.println("获取User信息成功，用户token：" + mAccessToken.getToken());
                Toast.makeText(context, 
                        "获取User信息成功，用户昵称：" + user.screen_name, 
                        Toast.LENGTH_LONG).show();
                Tools tools = new Tools();
                User u = tools.getUserInfo(user,mAccessToken.getToken());
                System.out.println(u.toString());
                UserDao u_Dao = new UserDao(context);
                
        	    u_Dao.updateUser(u);//将用户信息保存进数据库
        	    System.out.println("accessTabken值："+mAccessToken);
        	  //授权结束后实现界面跳转
                UserDao dao = new UserDao(context);
                List<User> list = dao.findAllUser();
                if(list != null){
	                Intent i = new Intent(context,LoginActivity.class);
	                ((Activity) context).finish();
	                context.startActivity(i);
                }
            } else {
                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        LogUtil.e(TAG, e.getMessage());
        ErrorInfo info = ErrorInfo.parse(e.getMessage());
        Toast.makeText(context, info.toString(), Toast.LENGTH_LONG).show();
        System.out.println("错误："+info.toString());
    }
}
