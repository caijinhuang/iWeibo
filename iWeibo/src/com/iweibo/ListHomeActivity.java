package com.iweibo;

import com.example.iweibo.R;
import com.iweibo.HomeActivity.HomeContext;
import com.iweibo.LoginActivity.UserSelected;
import com.iweibo.adpter_.HomeAdapter;
import com.iweibo.util.AccessTokenKeeper;
import com.iweibo.util.Constants;
import com.iweibo.util.Tools;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListHomeActivity extends Activity {
	private ListView home_lv;
	private HomeAdapter homeAdapter;
	private Button btn_refresh, btn_write;
	private Button weibo_title;
	private TextView home_erro_hint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.list_home);
		// 获取控件
		home_lv = (ListView) super.findViewById(R.id.lv_weibos);
		btn_refresh = (Button) super.findViewById(R.id.btn_refresh);
		weibo_title = (Button) super.findViewById(R.id.btn_txt_wb_title);
		btn_write = (Button) super.findViewById(R.id.btn_writer);
		home_erro_hint = (TextView) super.findViewById(R.id.home_erro_hint);
		weibo_title.setText(UserSelected.nowUser.getUser_name());
		Statuses_List_remain.who = 1;
		// 检查网络状态
		if (!Tools.isNetworkAvaliable(ListHomeActivity.this)) {
			if(Statuses_List_remain.statuses_list!=null){
				Toast.makeText(ListHomeActivity.this, "刷新失败！当前网络状态不佳！", Toast.LENGTH_SHORT).show();
				HomeActivity.HomeContext.flag_iserro_home = true;// 如果获取不到内容则跳转到错误页面
			}else{
				home_erro_hint.setText("世界上最远的距离就是，有手机却没法联网！请检查网络连接。。。");
				home_erro_hint.setVisibility(View.VISIBLE);
			}
		} else {
			init();
		}
		// 顶部菜单栏事件监听
		Myclick myclick = new Myclick();
		btn_refresh.setOnClickListener(myclick);
		weibo_title.setOnClickListener(myclick);
		btn_write.setOnClickListener(myclick);
		// 列表点击事件
		home_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(HomeActivity.HomeContext.home, ContentActivity.class);
				Bundle bundle = new Bundle();
				System.out.println("选中第" + position + "个微博");
				bundle.putInt("weiboId", position);
				intent.putExtras(bundle);// 通过intent传递当前微博id
				System.out.println("正常");
				startActivity(intent);
			}
		});
	}

	// 刷新列表
	public void refresh_list() {
		homeAdapter = new HomeAdapter(ListHomeActivity.this, Statuses_List_remain.statuses_list);
		home_lv.setAdapter(homeAdapter);
	}

	public void init() {
		// 打印用户信息
		Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken_DIY(this);
		StatusesAPI wb_statues = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
		Statuses_List_remain.wb_statues_all = wb_statues;
		wb_statues.friendsTimeline(0L, 0L, 50, 1, false, 0, false, mListener);
	}

	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private String TAG = "as";
	private RequestListener mListener = new RequestListener() {
		@SuppressLint("NewApi")
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				LogUtil.i(TAG, response);
				if (response.startsWith("{\"statuses\"")) {
					// 调用 StatusList#parse 解析字符串成微博列表对象
					StatusList statuses = StatusList.parse(response);
					Statuses_List_remain.statuses_list = statuses;// 保存微博列表信息
					if (statuses.statusList == null) {
						home_erro_hint.setText("获取失败，请重试！");
						home_erro_hint.setVisibility(View.VISIBLE);
					} else {
						/**
						 * 获取详细信息并显示在组件上
						 */
						HomeActivity.HomeContext.flag_iserro_home = false;
						homeAdapter = new HomeAdapter(ListHomeActivity.this, statuses);
						home_lv.setAdapter(homeAdapter);

						if (statuses != null && statuses.total_number > 0) {
							Toast.makeText(ListHomeActivity.this, "成功获取 " + statuses.statusList.size() + "条微博！",
									Toast.LENGTH_LONG).show();
						}
					}
				} else if (response.startsWith("{\"created_at\"")) {
					// 调用 Status#parse 解析字符串成微博对象
					Status status = Status.parse(response);
					Toast.makeText(ListHomeActivity.this, "发送一送微博成功, id = " + status.id, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ListHomeActivity.this, response, Toast.LENGTH_LONG).show();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			LogUtil.e(TAG, e.getMessage());
			// ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Toast.makeText(ListHomeActivity.this, /* info.toString() */"对不起，该用户使用期限已到，请重新授权", Toast.LENGTH_LONG).show();
			finish();
		}
	};

	// 按钮点击事件
	class Myclick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_refresh:
				Toast.makeText(ListHomeActivity.this, "主页刷新", Toast.LENGTH_SHORT).show();
				init();
				if(Statuses_List_remain.statuses_list!=null){
					home_erro_hint.setVisibility(View.GONE);//刷新列表隐藏
				}
				break;
			case R.id.btn_writer:
				startActivity(new Intent(ListHomeActivity.this, Write_Weibo_Activity.class));
				Toast.makeText(ListHomeActivity.this, "写微博", Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_txt_wb_title:
				Toast.makeText(ListHomeActivity.this, "个人微博", Toast.LENGTH_SHORT).show();
				break;
			}
		}

	}

	/**
	 * 保存微博列表信息
	 * 
	 * @author Administrator
	 *
	 */
	static class Statuses_List_remain {
		static StatusesAPI wb_statues_all = null;
		static StatusList statuses_list = null;
		static int list_flag = 1;// 第一次初始化的时候要更新列表并获取最新信息。当第二次回到页面时，就无需从新联网获取微博。
		static int who = 1;// 设置列表代号，1为好友微博列表，2为个人微博列表。
	}
}
