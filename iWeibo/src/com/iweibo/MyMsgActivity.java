package com.iweibo;

import java.util.ArrayList;

import com.example.iweibo.R;
import com.iweibo.ListHomeActivity.Statuses_List_remain;
import com.iweibo.util.AccessTokenKeeper;
import com.iweibo.util.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

public class MyMsgActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.erro_page);
		System.out.println("调用");
		init();
	}

	public void init() {
		Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken_DIY(this);
		CommentsAPI common = new CommentsAPI(MyMsgActivity.this, Constants.APP_KEY, mAccessToken);
		common.timeline(0L, 0L, 50, 1,true,mListener);
	}

	// 授权回调信息
	private RequestListener mListener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

		@SuppressWarnings("unused")
		@Override
		public void onComplete(String response) {
			System.out.println("评论1");
			if (!TextUtils.isEmpty(response)) {
				System.out.println("评论2");
				if (response.startsWith("{\"comments\"")) {
					System.out.println("评论3");
					//解析json信息
					StatusList statusList = StatusList.parse(response);
					Statuses_List_remain.statuses_list = statusList;// 保存微博列表信息
					ArrayList<Status> status = statusList.statusList;
					if(statusList!=null){
					}else{
						System.out.println("无评论");
					}
				}
			}
		}
	};
	/**
	 * 保存微博列表信息
	 * 
	 * @author Administrator
	 *
	 */
	static class Statuses_List_remain {
		static StatusesAPI wb_statues_all = null;
		static StatusList statuses_list = null;
	}
}
