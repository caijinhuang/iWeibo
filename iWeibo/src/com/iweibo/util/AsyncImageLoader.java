package com.iweibo.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncImageLoader {
	/**
	 * 图片缓存 key = 用户昵称 , value = 图片资源对象
	 */
	private static HashMap<String,SoftReference<Drawable>> imageCache;
	public AsyncImageLoader() {
		if(imageCache == null){
			imageCache = new HashMap<String, SoftReference<Drawable>>();
		}
	}
	
	/**
	 * 异步加载图片资源
	 * @param url 图片地址
	 * @param imageview 需要显示图片的组件
	 * @param callback 回调接口
	 * @return 图片资源
	 */
	public static Drawable loadDrawable(final String url,final ImageView imageview, final ImageCallback callback){
		//从缓存中判断图片是否已经下载过了,如果下载过直接返回，否则异步下载
		if(imageCache.containsKey(url)){
			SoftReference<Drawable> soft = imageCache.get(url);
			Drawable dra =  soft.get();
			if(dra != null){
				return  dra;
			}
		}
		
		final Handler handler =  new Handler(){
			@Override
			public void handleMessage(Message msg) {
				//图片资源设置操作
				callback.imageset((Drawable)msg.obj, imageview);
			}
		};
		//新建线程下载图片
		new Thread(){
			public void run() {
				Drawable drawable = Tools.getDrawableFromUrl(url);
				//设置缓存，避免重复下载图片
				imageCache.put(url, new SoftReference<Drawable>(drawable));
				Message msg = handler.obtainMessage();
				msg.obj = drawable;
				handler.sendMessage(msg);
			}
		}.start();
		
		return null;
	}
	/**
	 * 回调接口
	 * @author Administrator
	 *
	 */
	public interface ImageCallback{
		/**
		 * 图片资源设置
		 * @param drawable
		 * @param iv
		 */
		public void imageset (Drawable drawable, ImageView iv);
	}
	
}
