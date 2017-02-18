package com.iweibo;

import com.example.iweibo.R;
import com.iweibo.ListHomeActivity.Statuses_List_remain;
import com.iweibo.util.AsyncImageLoader;
import com.iweibo.util.AsyncImageLoader.ImageCallback;
import com.iweibo.util.Tools;
import com.sina.weibo.sdk.openapi.models.Status;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentActivity extends Activity {
	private ImageView now_user_head;//用户头像
	private  TextView content_user_name;//微博昵称
	private  TextView content_text;//微博正文
	private ImageView content_text_pic;//微博配图
	private  TextView content_time;//微博发布时间
	private  TextView content_from;//微博来源
	private  TextView content_redirect;//微博转发数量
	private  TextView content_comment;//微博评论数量
	private  Button back;
	static  boolean DraIsEmpty = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_activity);
		//获取组件
		now_user_head = (ImageView) super.findViewById(R.id.now_img_wb_head);
		content_text_pic = (ImageView) super.findViewById(R.id.now_img_wb_content_pic);
		content_user_name = (TextView) super.findViewById(R.id.now_txt_user_name);
		content_time = (TextView) super.findViewById(R.id.now_txt_wb_time);
		content_from = (TextView) super.findViewById(R.id.now_wb_from);
		content_text = (TextView) super.findViewById(R.id.now_txt_wb_content);
		
		back = (Button) super.findViewById(R.id.btn_back);
		//点击事件
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_back:
					finish();
					break;
			}
			}
		});
		//获取微博位置信息
		Intent intent = this.getIntent();
		if(intent != null){
			Bundle b = intent.getExtras();
			if(b!=null){
				if(b.containsKey("weiboId")){
					int weiboId = b.getInt("weiboId");
					init(weiboId,ListHomeActivity.Statuses_List_remain.who);
				}
			}
		}
	}
	public void init(int weiboId,int who){
		Status weibo_info = null;
		if(who==1){
			weibo_info = Statuses_List_remain.statuses_list.statusList.get(weiboId);		
		}else if(who==2){
			weibo_info = com.iweibo.GetuserTimelineActivity.Statuses_List_remain.statuses_list.statusList.get(weiboId);
		}
		now_user_head.setImageDrawable(Tools.getDrawableFromUrl(weibo_info.user.avatar_hd));
		content_user_name.setText(weibo_info.user.name);
		content_time.setText(weibo_info.created_at);
		content_from.setText(weibo_info.source);
		content_text.setText(weibo_info.text);
		content_text_pic.setImageDrawable(Tools.getDrawableFromUrl(weibo_info.original_pic));
	}
}
