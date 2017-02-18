package com.iweibo.adpter_;

import java.util.ArrayList;

import com.example.iweibo.R;
import com.example.iweibo.R.drawable;
import com.iweibo.util.AsyncImageLoader;
import com.iweibo.util.AsyncImageLoader.ImageCallback;
import com.iweibo.util.Tools;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author Cjh
 *
 */
public class HomeAdapter extends BaseAdapter {
	static boolean Dra_isEmpty = true;
	private StatusList statuses = null;//解析后的微博信息列表
	private Context context = null;
	public HomeAdapter(Context context,StatusList statuses) {
		this.context = context;
		this.statuses = statuses;
	}
	
	//实现数据的异步加载
	class ContentHolder{
		public ImageView content_head;//微博头像
		public  TextView content_user_name;//微博昵称
		public  TextView content_text;//微博正文
		public ImageView content_text_pic;//微博配图
		public  TextView content_time;//微博发布时间
		public  TextView content_redirect;//微博转发数量
		public  TextView content_comment;//微博评论数量
	}
	
	AsyncImageLoader asynImageLoader = new AsyncImageLoader();
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/**
		 * resource:表示要显示的自定义布局文件
		 */
		convertView = View.inflate(context, R.layout.wb_item_template, null);
		ContentHolder holder = new ContentHolder();
		//关联组件，提高效率
		holder.content_head = (ImageView) convertView.findViewById(R.id.img_wb_item_head);
		holder.content_user_name = (TextView) convertView.findViewById(R.id.txt_wb_item_uname);
		holder.content_time = (TextView) convertView.findViewById(R.id.txt_wb_item_time);
		holder.content_text = (TextView) convertView.findViewById(R.id.txt_wb_item_content);
		holder.content_text_pic = (ImageView) convertView.findViewById(R.id.img_wb_item_content_pic);
		holder.content_redirect = (TextView) convertView.findViewById(R.id.txt_wb_item_redirect);
		holder.content_comment = (TextView) convertView.findViewById(R.id.txt_wb_item_comment);
		
		Status weibo_info = statuses.statusList.get(position);
		if(position < statuses.statusList.size()){
			//将微博ID作为每个条目的标签
			convertView.setTag(weibo_info.id);
			//
			holder.content_user_name.setText(weibo_info.user.name);
			holder.content_time.setText(weibo_info.created_at);
			holder.content_text.setText(weibo_info.text);
			holder.content_redirect.setText(""+weibo_info.reposts_count);
			holder.content_comment.setText(""+weibo_info.comments_count);
			
			String pic_ = weibo_info.thumbnail_pic;
			
			//判断是否有微博配图
			if(!pic_.equals("")){
				//如果不包含图片就异步加载图片（异步加载图片）
				Drawable drawable = AsyncImageLoader.loadDrawable(pic_, holder.content_text_pic,new ImageCallback() {
					
					@Override
					public void imageset(Drawable drawable, ImageView iv) {
						iv.setImageDrawable(drawable);
						Dra_isEmpty = false;
					}
				});
				if(Dra_isEmpty){
					holder.content_text_pic.setImageDrawable(drawable);
				}
				Dra_isEmpty = true;
			}
			
			//异步加载微博用户头像
			String head_url = weibo_info.user.avatar_hd;
			Drawable head_drawable = AsyncImageLoader.loadDrawable(head_url, holder.content_head,new ImageCallback() {
				
				@Override
				public void imageset(Drawable drawable, ImageView iv) {
					iv.setImageDrawable(drawable);
					Dra_isEmpty = false;
				}
			});
			if(Dra_isEmpty){
				holder.content_head.setImageDrawable(head_drawable);
			}
			Dra_isEmpty = true;
		}
		
		return convertView;
	}
	
	//获取微博长度
	@Override
	public int getCount() {
		return statuses.statusList.size();
	}
	
	//根据下标获取条目
	@Override
	public Object getItem(int position) {
		return statuses.statusList.get(position);
	}
	
	//获取下标
	@Override
	public long getItemId(int position) {
		return position;
	}
}
