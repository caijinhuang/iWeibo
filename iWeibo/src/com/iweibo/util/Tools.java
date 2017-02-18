package com.iweibo.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.iweibo.LoginActivity;
import com.iweibo.LoginActivity.UserSelected;
import com.iweibo.MainActivity;
import com.iweibo.OAuthActivity;
import com.iweibo.dao.UserDao;
import com.iweibo.pojo.User;
import com.iweibo.pojo.Weibo_info;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.widget.Toast;

public class Tools {
	private Context context;
	/**
	 * 首次网络状态监测
	 */
	public static void checkNetwork(final MainActivity context) {
		if(!isNetworkAvaliable(context)){
			TextView msg = new TextView(context);
			msg.setText("当前没有可使用的网络请设置网络");
			new AlertDialog.Builder(context,AlertDialog.THEME_TRADITIONAL)
						.setIcon(android.R.drawable.ic_menu_help)
						.setTitle("网络状态提示")
						.setView(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//跳转网络设置
									context.startActivity(new Intent(
											android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS
											));
									context.finish();//关闭应用
								}
							}).create().show();
		}
		else{
			init(context);
		}
	}
	//页面跳转
	public static void init(final Context context){
		UserDao dao = new UserDao(context);
		List<User> userlist = dao.findAllUser();
		if(userlist == null || userlist.isEmpty()){
			Toast.makeText(context, "无用户数据", Toast.LENGTH_LONG).show();
			Intent i = new Intent(context,OAuthActivity.class);
			context.startActivity(i);
			((Activity) context).finish();
		}else{
			Toast.makeText(context, "已经有用户登录过", Toast.LENGTH_LONG).show();
			//授权结束后实现界面跳转
            Intent i = new Intent(context,LoginActivity.class);
            context.startActivity(i);
            ((Activity) context).finish();
		}
	}
	/**
	 * 检查网络状态̬
	 * @return true 有网 false 没网
	 */
	public static boolean isNetworkAvaliable(Context context) {
		//获取所有网络状态
		ConnectivityManager connectivitymanager = (ConnectivityManager)context.
				getSystemService(Context.CONNECTIVITY_SERVICE);
		//检查网络状态是否为空
		if(connectivitymanager == null){
			return false;
		}else{
//			Toast.makeText(MainActivity.this, "���ӳɹ�",Toast.LENGTH_SHORT).show();
			//获取所有网络
			NetworkInfo[] netInfo = connectivitymanager.getAllNetworkInfo();
			if(netInfo != null){
				for(NetworkInfo network : netInfo){
					//检查是已经连接
					if(network.getState() == NetworkInfo.State.CONNECTED){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public ArrayList<Weibo_info> loadHomeData(){
		//判断用户是否存在
		if(UserSelected.nowUser!=null){
			
		}
		return null;
	}
	
	/**
	 * 获取微博用户信息并保存到数据库
	 * @param user
	 * @return
	 */
	public User getUserInfo(com.sina.weibo.sdk.openapi.models.User user,String access_taken){
	    System.out.println("用户昵称："+user.screen_name);
	    System.out.println("用户所在地："+user.location);
	    System.out.println("用户个人描述："+user.description);
	    System.out.println("粉丝数："+user.followers_count);
	    System.out.println("关注数："+user.friends_count);
	    System.out.println("用户头像地址："+user.profile_image_url);
	    //将用户信息暂存到User里面并返回
	    User u = new User();
	    u.setUser_id(user.id);//用户id
	    u.setUser_name(user.screen_name);//用户昵称
	    u.setDescription(user.description);//用户描述
	    u.setAccess_taken(access_taken);//用户的access_taken
	    u.setUser_head(getDrawableFromUrl(user.profile_image_url));//获取用户头像
		return u;
	}
	/**
	 * 获取用户头像
	 * @param url 用户头像的下载路径
	 * @return
	 */
	public static Drawable getDrawableFromUrl(String url){
		
		try {
			//打开头像链接路径
			URLConnection urls = new URL(url).openConnection();
			//获取图片流信息,并转换成图片信息然后然后返回给调用者
			return Drawable.createFromStream(urls.getInputStream(),"image");
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
