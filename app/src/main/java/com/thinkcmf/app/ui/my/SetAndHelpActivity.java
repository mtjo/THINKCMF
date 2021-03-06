package com.thinkcmf.app.ui.my;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;

import com.tencent.android.tpush.XGPushManager;
import com.base.aframe.Loger;
import com.base.aframe.http.StringCallBack;
import com.base.aframe.ui.ViewInject;
import com.thinkcmf.app.api.HttpPostManager;
import com.thinkcmf.app.ui.SelectPicPopupShare;
import com.thinkcmf.app.utils.SharedUserInfo;
import com.thinkcmf.app.utils.UMengUtil;
import com.wanyou.lscn.R;
import com.thinkcmf.app.application.SysApplication;
import com.thinkcmf.app.config.LscnConfig;
import com.thinkcmf.app.entity.UserInfo;
import com.thinkcmf.app.ui.base.BaseActivity;

public class SetAndHelpActivity extends BaseActivity {
    private SetAndHelpActivity mContext;
	
	public static void open(Activity aty, int requestCode){
		Intent intent = new Intent(aty, SetAndHelpActivity.class);
		aty.startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onInit() {
		setContentLayout(R.layout.setandhelp_activity);
	}
	
	@Override
	protected void onInitViews() {
		mContext = this;
		
		setTitle("设置与帮助");
		if(null == SharedUserInfo.getUserInfo(this)){
			findViewById(R.id.login_out_btn).setVisibility(View.GONE);
		}
	}
	
	/**
	 * 告诉好友
	 */
	public void tellFriend(View v){
		UMengUtil.onEvtent(mContext.getApplicationContext(), LscnConfig.UMENG_TOLD_FRIEND);
		SelectPicPopupShare share = new SelectPicPopupShare(this,null);
		share.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	/**
	 * 给我们打分
	 */
	public void givePrise(View v){
		try{
			Uri uri = Uri.parse("market://details?id="+this.getPackageName());    
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);    
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
			startActivity(intent);
		}catch(Exception e){
			Loger.debug("评价", e);
		}
	}
	
	/**
	 * 关于好律师在线
	 */
	public void aboutLscn(View v){
		AboutLSCNActivity.open(this, 0);
	}
	
	public void loginOut(View v){
		exit();
	}
	
	/**
	 * 退出登录 i
	 */
	private void exit(){
		//注销登录统计
		UMengUtil.onEvtent(mContext.getApplicationContext(), LscnConfig.UMENG_EXIT);
		
		UserInfo user = SharedUserInfo.getUserInfo(mContext);
		StringCallBack callBack = new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				SharedUserInfo.exitUserInfo(mContext);
				ViewInject.showToast(mContext, getString(R.string.me_exit_succ_msg));
				XGPushManager.unregisterPush(mContext.getApplicationContext());
				
				findViewById(R.id.login_out_btn).setVisibility(View.GONE);
				SysApplication.getInstance().loginOutSuccess();
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				SharedUserInfo.exitUserInfo(mContext);
				ViewInject.showToast(mContext, getString(R.string.me_exit_succ_msg));
				XGPushManager.unregisterPush(mContext.getApplicationContext());
				
				findViewById(R.id.login_out_btn).setVisibility(View.GONE);
				SysApplication.getInstance().loginOutSuccess();
			}
		};
		
		HttpPostManager.exit(user.getAuthtoken(), callBack, mContext, "正在退出登录，请稍后...");
	}
}
