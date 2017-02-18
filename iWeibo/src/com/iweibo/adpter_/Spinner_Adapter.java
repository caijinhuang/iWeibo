package com.iweibo.adpter_;

import java.util.List;

import com.example.iweibo.R;
import com.iweibo.GetuserTimelineActivity;
import com.iweibo.LoginActivity;
import com.iweibo.MainActivity;
import com.iweibo.dao.UserDao;
import com.iweibo.pojo.User;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ViewHolder")
public class Spinner_Adapter extends BaseAdapter {
	private TextView user_name,del_user;
	private Context context;
	private List<User> user_list_shows;
	//构造方法
	public Spinner_Adapter(Context context,List<User> user_list_shows){
		this.context = context;
		this.user_list_shows = user_list_shows;
	}

	@Override
	public int getCount() {
		return user_list_shows.size();
	}

	@Override
	public Object getItem(int position) {
		return user_list_shows.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View convert = convertView;
		convertView = View.inflate(context, R.layout.spinner_cell, null);
		user_name = (TextView) convertView.findViewById(R.id.user_name);
		user_name.setText(user_list_shows.get(position).getUser_name());
		del_user = (TextView)convertView.findViewById(R.id.del_user);
		del_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.del_user:
					final String u_name = user_list_shows.get(position).getUser_name();
					final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							// 删除账户信息
							case AlertDialog.BUTTON_POSITIVE:
								new UserDao(LoginActivity.UserSelected.context).deleteUser(u_name);
								System.out.println("删除成功");
								if(LoginActivity.UserSelected.userNum==1){
									Intent intent = new Intent(context,MainActivity.class);
									context.startActivity(intent);
								}else{
									Intent intent = new Intent(context,LoginActivity.class);
									context.startActivity(intent);
								}
								Intent intentsent = new Intent("finishActivity");
								context.sendBroadcast(intentsent);
								break;
							// 取消
							case AlertDialog.BUTTON_NEGATIVE:
								break;
							default:
								break;
							}
						}
					};
					AlertDialog dialog = new AlertDialog.Builder(context).create();
					dialog.setTitle("系统提示");
					dialog.setButton("确定", listener);
					dialog.setButton2("取消", listener);
					dialog.setMessage("是否删除该用户？");
					dialog.show();
				default:
					break;
				}
			}
		});
		return convertView;
	}

}
