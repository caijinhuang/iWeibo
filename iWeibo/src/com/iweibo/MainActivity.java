package com.iweibo;


import com.example.iweibo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.iweibo.dao.UserDao;
import com.iweibo.pojo.User;
import com.iweibo.util.*;
/**
 * 欢迎页
 * @author Cjh
 *
 */
public class MainActivity extends Activity {
	private ImageView loadImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadImage = (ImageView) super.findViewById(R.id.loadImage);
		//设置动画从透明到不透明变化 0.0(全透明)-1.0(不透明)
		AlphaAnimation animation = new AlphaAnimation(0.1f, 0.9f);
		//设置动画时间2.5s
		animation.setDuration(1500);
		//加载动画
		loadImage.setAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			//设置动画效果
			@Override
			public void onAnimationStart(Animation animation) {
//				Toast.makeText(MainActivity.this, "������ʼ",Toast.LENGTH_SHORT).show();
			}
			//重复启动时调用
			@Override
			public void onAnimationRepeat(Animation animation) {
//				Toast.makeText(MainActivity.this, "重复",Toast.LENGTH_SHORT).show();
			}
			//动画结束
			@Override
			public void onAnimationEnd(Animation animation) {
//				Toast.makeText(MainActivity.this, "动画结束",Toast.LENGTH_SHORT).show();
				Tools.checkNetwork(MainActivity.this);//检查网络是否可用，并页面跳转
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
