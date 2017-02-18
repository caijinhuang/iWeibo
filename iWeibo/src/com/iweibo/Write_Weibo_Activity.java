package com.iweibo;

import java.io.File;

import com.example.iweibo.R;
import com.iweibo.ListHomeActivity.Statuses_List_remain;
import com.iweibo.util.Tools;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.Status;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class Write_Weibo_Activity extends Activity {
	private Button but_add_pic,write_weibo,write_back;
	private EditText weibo_text;
	private TextView text_limit_count,title_name;
	private ImageView image;
	private int weibo_max_length = 140;
	private boolean flag = false;
	private static final String IMAGE_UNSPECIFIED = "image/*";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_weibo_activity);
		
		//获得组件
		but_add_pic = (Button) super.findViewById(R.id.btn_add_pic);
		write_weibo = (Button) super.findViewById(R.id.write_weibo);
		write_back = (Button) super.findViewById(R.id.write_btn_back);
		weibo_text = (EditText) super.findViewById(R.id.write_content_text);
		text_limit_count = (TextView) super.findViewById(R.id.text_count);
		image = (ImageView) super.findViewById(R.id.write_with_pic);
		title_name = (TextView) super.findViewById(R.id.write_txt_wb_title);
		title_name.setText("随时随地乐享生活");
		//注册点击事件
		MyOnClick myOnclick = new MyOnClick();
		but_add_pic.setOnClickListener(myOnclick);
		write_weibo.setOnClickListener(myOnclick);
		write_back.setOnClickListener(myOnclick);
		//编辑窗事件监听
		weibo_text.addTextChangedListener(new TextWatcher() {
			//输入内容改变时
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//获取已经输入内容的长度
				String myText = weibo_text.getText().toString();
				int len = myText.length();
				if(len <= weibo_max_length){
					len = weibo_max_length - len;
					text_limit_count.setTextColor(R.color.black);
					text_limit_count.setEnabled(true);
					//如果没有超出范围则显示出发布按钮
					if(write_weibo.getVisibility() == View.GONE){
						write_weibo.setVisibility(View.VISIBLE);
					}
				}else{
					len = weibo_max_length - len;
					text_limit_count.setTextColor(R.color.red);
					text_limit_count.setEnabled(false);
					//文字超出范围隐藏发布按钮
					if(write_weibo.getVisibility() == View.VISIBLE){
						write_weibo.setVisibility(View.GONE);
						flag = true;
					}
				}
				text_limit_count.setText(flag ? "字数超出："+len : "字数剩余："+len);
			}
			//输入内容改变前
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			//输入内容改变后
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	/**
	 * 浏览系统图片
	 */
	public void setImage(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType(IMAGE_UNSPECIFIED);
		startActivityForResult(intent,0);
		System.out.println();
	}
	//获取系统图片
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("读取图片");
		if (requestCode == 0 && resultCode == -1) {
			// 获取原图的Uri，它是一个内容提供者
			Uri uri = data.getData();
			image.setImageURI(uri);
			image.setVisibility(View.VISIBLE);
		}
	}
	//点击事件
	class MyOnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			//返回
			case R.id.write_btn_back:
				finish();
				break;
			//添加图片
			case R.id.btn_add_pic:
				setImage();
				break;
			//发送微博
			case R.id.write_weibo:
				//判断是否有图片
				if(image.getDrawable() != null){
				//Drawable转换成Bitmap
				BitmapDrawable bitDra =  (BitmapDrawable) image.getDrawable();
				Bitmap pic = bitDra.getBitmap();
				
				Statuses_List_remain.wb_statues_all.upload(weibo_text.getText().toString(), pic,
						null, null, new RequestListener() {
							@Override
							public void onComplete(String arg0) {
								if(arg0.startsWith("{\"created_at\"")) {
				                    // 调用 Status#parse 解析字符串成微博对象
				                    Toast.makeText(Write_Weibo_Activity.this, 
				                            "微博发送成功" , 
				                            Toast.LENGTH_LONG).show();
								text_limit_count.setText("");//发布完成，清空文本框
								image.setVisibility(View.GONE);
								finish();
								}
								
							}
							@Override
							public void onWeiboException(WeiboException arg0) {
								
							}
							
						});
				}else{
					Statuses_List_remain.wb_statues_all.update(weibo_text.getText().toString(), null, null,new RequestListener() {
						
						@Override
						public void onWeiboException(WeiboException arg0) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onComplete(String arg0) {
							if(arg0.startsWith("{\"created_at\"")) {
			                    // 调用 Status#parse 解析字符串成微博对象
			                    Toast.makeText(Write_Weibo_Activity.this, 
			                            "微博发送成功" , 
			                            Toast.LENGTH_LONG).show();
							text_limit_count.setText("");//发布完成，清空文本框
//							new GetuserTimelineActivity().init();
//							new ListHomeActivity().init();
							finish();
							}
						}
					});
					
				}
				break;

			
			}
		}
		
	}
}
