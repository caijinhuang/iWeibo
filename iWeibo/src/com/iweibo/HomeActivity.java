package com.iweibo;

import com.example.iweibo.R;
import com.iweibo.ListHomeActivity.Statuses_List_remain;
import com.iweibo.adpter_.HomeAdapter;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
/**
 * 显示用户微博列表
 * @author Cjh
 *
 */
@SuppressWarnings("deprecation")
public class HomeActivity extends ActivityGroup {
	private ListView home_lv;
	private View rd_home,rd_at,rd_msg,rd_more;
	private HomeAdapter homeAdapter;
	private static TabHost tabhost;//初始化标签
	private TabWidget tabwidget;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		HomeContext.home = HomeActivity.this;
		System.out.println("正常1");
		//操作栏空间获取=============必须要有这步=====================
		LayoutInflater fi = LayoutInflater.from(HomeActivity.this);
		View parent = fi.inflate(R.layout.operation_bar, null);
		LinearLayout chaild = (LinearLayout) parent.findViewById(R.id.tab_frame);
		rd_home =  parent.findViewById(R.id.rd_home);
		rd_at =  parent.findViewById(R.id.rd_at);
		rd_msg =  parent.findViewById(R.id.rd_msg);
		rd_more = parent.findViewById(R.id.rd_more);
		chaild.removeAllViews();//从ViewGroup中移除所有子视图
		System.out.println("正常1");
		init_tab();
		//菜单栏事件监听
		tabhost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				//页面判断,为了区分列表点击事件=====================
				if(tabId.equals("home")){
					Statuses_List_remain.who = 1;
				}
				if(tabId.equals("user")){
					Statuses_List_remain.who = 2;
				}
			}
		});
	}
	/**
	 * 初始化标签事件
	 */
	public void init_tab(){
		tabhost =(TabHost) super.findViewById(R.id.tabhost);//获取tabhost控件；
		tabhost.setup(this.getLocalActivityManager());
		tabwidget = tabhost.getTabWidget();
		//====================================================
		//主页
		TabSpec tabspec_home = tabhost.newTabSpec("home");//定义选项卡名称
		tabspec_home.setIndicator(rd_home);//定义选项 卡指针(确定要设置点击事件的按钮)
		tabspec_home.setContent(new Intent(HomeActivity.this, ListHomeActivity.class));//未设置flag不及时刷新。保存原页
//		tabspec_home.setContent(new Intent(HomeActivity.this, ListHomeActivity.class).
//		setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));//设置了flag则每次都会从新刷新页面。即重新初始化一遍activity.
		tabhost.addTab(tabspec_home);//将选项卡加入选项列表中。
		//刷新主页
		TabSpec tab_reHome = tabhost.newTabSpec("rehome");
		tab_reHome.setIndicator("rehome");
		tab_reHome.setContent(new Intent(HomeActivity.this,ListHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabhost.setTag(tab_reHome);
		//==========================================================
		//个人发布的微博
		TabSpec tabspec_user = tabhost.newTabSpec("user");//定义选项卡名称
		tabspec_user.setIndicator(rd_at);//定义选项 卡指针(确定要设置点击事件的按钮)
		tabspec_user.setContent(new Intent(HomeActivity.this, GetuserTimelineActivity.class));
		tabhost.addTab(tabspec_user);//将选项卡加入选项列表中。
		//======================================================================
		//错误页面
		TabSpec erro = tabhost.newTabSpec("erro");//定义选项卡名称
		erro.setIndicator(rd_msg);//定义选项 卡指针(确定要设置点击事件的按钮)
		erro.setContent(new Intent(HomeActivity.this, MyMsgActivity.class));
		tabhost.addTab(erro);//将选项卡加入选项列表中。
	}
	/**
	 * ActivityGroup返回事件监听
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			//创建对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			//设置对话框标题
			isExit.setTitle("系统提示");
			//设置对话框内容
			isExit.setMessage("确定要退出吗");
			//添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			//显示对话框
			isExit.show();
		}
		return super.dispatchKeyEvent(event);
	}
	//对话框监听事件
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE://确认按钮退出程序
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE://"取消按钮"
				break;
			default:
				break;
			}
		}
	};
	
	static class HomeContext{
		static Context home;
		static int mylist = 1;
		static boolean flag_iserro_home = false;
		static boolean flag_iserro_user = false;
	}
}
