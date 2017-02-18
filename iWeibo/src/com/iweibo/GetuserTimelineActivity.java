package com.iweibo;

import com.example.iweibo.R;
import com.iweibo.ListHomeActivity.Myclick;
import com.iweibo.ListHomeActivity.Statuses_List_remain;
import com.iweibo.LoginActivity.UserSelected;
import com.iweibo.adpter_.HomeAdapter;
import com.iweibo.util.AccessTokenKeeper;
import com.iweibo.util.Constants;
import com.iweibo.util.Tools;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemLongClickListener;

public class GetuserTimelineActivity extends Activity {
	private HomeAdapter homeAdapter;
	private ListView home_lv;
	private StatusesAPI userapi;
	private Oauth2AccessToken mAccessToken;
	private long web_id;
	private Button btn_refresh, btn_write;
	private Button weibo_title;
	private TextView erro_hint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.list_user);
		ListHomeActivity.Statuses_List_remain.who = 2;
		// 获取控件
		home_lv = (ListView) super.findViewById(R.id.lv_weibos_user);
		btn_refresh = (Button) super.findViewById(R.id.btn_refresh);
		weibo_title = (Button) super.findViewById(R.id.btn_txt_wb_title);
		btn_write = (Button) super.findViewById(R.id.btn_writer);
		weibo_title.setText("全部微博");
		erro_hint = (TextView) super.findViewById(R.id.erro_hint);
		// 检查网络状态
		if (!Tools.isNetworkAvaliable(GetuserTimelineActivity.this)) {
			Toast.makeText(GetuserTimelineActivity.this, "刷新失败！当前网络状态不佳！", Toast.LENGTH_SHORT).show();
			if (Statuses_List_remain.statuses_list != null) {
				homeAdapter = new HomeAdapter(GetuserTimelineActivity.this, Statuses_List_remain.statuses_list);
				home_lv.setAdapter(homeAdapter);
			} else {
				erro_hint.setText("世界上最远的距离就是，有手机却没法联网！请检查网络连接。。。");
				erro_hint.setVisibility(View.VISIBLE);
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
		final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				// 删除微博
				case AlertDialog.BUTTON_POSITIVE:
					userapi.destroy(web_id, new RequestListener() {

						@Override
						public void onWeiboException(WeiboException arg0) {
							Toast.makeText(GetuserTimelineActivity.this, "删除失败", Toast.LENGTH_LONG).show();
						}

						@Override
						public void onComplete(String response) {
							init();
							Toast.makeText(GetuserTimelineActivity.this, "成功删除微博！", Toast.LENGTH_LONG).show();
						}
					});
					break;
				// 取消
				case AlertDialog.BUTTON_NEGATIVE:
					break;
				default:
					break;
				}
			}
		};
		// 设置长按点击事件删除微博
		home_lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				AlertDialog dialog = new AlertDialog.Builder(GetuserTimelineActivity.this).create();
				web_id = Long.parseLong(Statuses_List_remain.statuses_list.statusList.get(position).id);
				dialog.setTitle("系统提示");
				dialog.setButton("确定", listener);
				dialog.setButton2("取消", listener);
				dialog.setMessage("是否删除该微博？");
				dialog.show();
				return true;//返回为True表示长按事件被实现，才不会和单机事件冲突
			}
		});

	}

	public void init() {
		mAccessToken = AccessTokenKeeper.readAccessToken_DIY(GetuserTimelineActivity.this);
		userapi = new StatusesAPI(GetuserTimelineActivity.this, Constants.APP_KEY, mAccessToken);
		userapi.homeTimeline(0L, 0L, 10, 1, true, 1, false, mListener);
	}

	/**
	 * OPenAPI回调接口
	 */
	private String TAG = "as";
	private RequestListener mListener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
		}

		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				LogUtil.i(TAG, response);
				if (response.startsWith("{\"statuses\"")) {
					// 调用 StatusList#parse 解析字符串成微博列表对象
					StatusList statuses = StatusList.parse(response);
					// System.out.println();
					Statuses_List_remain.statuses_list = statuses;// 保存微博列表信息
					if (statuses.statusList == null) {
						// 判断是否获取出错
						erro_hint.setText("亲，您还没发表微博哦！快发一条吧");
						erro_hint.setVisibility(View.VISIBLE);
						home_lv.setVisibility(View.GONE);
					} else {
						/**
						 * 获取详细信息并显示在组件上
						 */
						HomeActivity.HomeContext.flag_iserro_user = false;
						homeAdapter = new HomeAdapter(GetuserTimelineActivity.this, statuses);
						home_lv.setAdapter(homeAdapter);
						home_lv.setVisibility(View.VISIBLE);
						if (statuses != null && statuses.total_number > 0) {
							Toast.makeText(GetuserTimelineActivity.this, "成功获取 " + statuses.statusList.size() + "条微博！", Toast.LENGTH_LONG)
									.show();
						}
					}
				} else if (response.startsWith("{\"created_at\"")) {
					// 调用 Status#parse 解析字符串成微博对象
					Status status = Status.parse(response);
					Toast.makeText(GetuserTimelineActivity.this, "发送一送微博成功, id = " + status.id, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(GetuserTimelineActivity.this, response, Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(GetuserTimelineActivity.this, "获取失败", Toast.LENGTH_LONG).show();
			}
		}
	};

	// 按钮点击事件
	class Myclick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_refresh:
				Toast.makeText(GetuserTimelineActivity.this, "刷新", Toast.LENGTH_SHORT).show();
				init();
				if(Statuses_List_remain.statuses_list!=null){
					erro_hint.setVisibility(View.GONE);//刷新列表隐藏
				}
				break;
			case R.id.btn_writer:
				startActivity(new Intent(GetuserTimelineActivity.this, Write_Weibo_Activity.class));
				Toast.makeText(GetuserTimelineActivity.this, "写微博", Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_txt_wb_title:
				Toast.makeText(GetuserTimelineActivity.this, "个人微博", Toast.LENGTH_SHORT).show();
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
		static int list_flag = 0;

	}
}
