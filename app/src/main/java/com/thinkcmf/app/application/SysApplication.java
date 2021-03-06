package com.thinkcmf.app.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.thinkcmf.app.api.HttpPostManager;
import com.thinkcmf.app.ui.start.MainActivity;
import com.thinkcmf.app.utils.SharedUserInfo;
import com.umeng.analytics.MobclickAgent;
import com.base.aframe.Loger;
import com.base.aframe.http.StringCallBack;
import com.base.aframe.ui.WYActivityManager;
import com.base.aframe.ui.widget.emoji.WXFaceUtils;
import com.thinkcmf.app.entity.UserInfo;

public class SysApplication extends Application {
	private static SysApplication instance;
	private MainActivity mainActivity;
	
	public SysApplication() {
		if(null == instance)
			instance = this;
	}
	
	public synchronized static SysApplication getInstance() { 
		if (null == instance) { 
			instance = new SysApplication(); 
		} 
		return instance; 
	}

	public void addActivity(Activity activity) {
		WYActivityManager.create().addActivity(activity);
	}
	
	public void exit(Context context) {
		MobclickAgent.onKillProcess(context);
		WXFaceUtils.destory();
		WYActivityManager.create().AppExit(context);
    }
	
	public void finishAll(){
		WYActivityManager.create().finishAllActivity();
	}
	
	@Override
	public void onLowMemory() { 
		super.onLowMemory();     
		System.gc(); 
	}
	
	@Override
	public void onCreate() {
		SysAppCrashHandler handler = SysAppCrashHandler.getInstance();
		handler.init(getApplicationContext());
		Thread.setDefaultUncaughtExceptionHandler(handler);
		super.onCreate(); 
		initEngineManager(this);
		MobclickAgent.updateOnlineConfig(this);
		MobclickAgent.setDebugMode(false);
		WXFaceUtils.init(getApplicationContext());
	}
	
	public void initEngineManager(Context context) {
	}
	
	/**
	 * 上传推送token
	 */
	public void uploadToken(){
		UserInfo userinfo = SharedUserInfo.getUserInfo(this);
		String token = SharedUserInfo.getToken(this);
		if(null == userinfo || null == token){
			return;
		}
		HttpPostManager.uploadToken(userinfo.getAuthtoken(), token,
				new StringCallBack() {
					@Override
					public void onSuccess(Object t) {
						Loger.debug("手机token上传成功");
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
										  String strMsg) {
						Loger.debug("手机token上传失败");
					}
				}, null, null);
	}
	
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	/**
	 * 登录成功
	 */
	public void loginSuccess(){
		if(null != mainActivity){
			mainActivity.loginSuccess();
		}
	}
	
	/**
	 * 退出成功
	 */
	public void loginOutSuccess(){
		if(null != mainActivity){
			mainActivity.loginOutSuccess();
		}
	}
	
	/**
	 * 新消息
	 */
	public void newMsg(){
		if(null != mainActivity){
			mainActivity.loadMsg();
		}
	}
	
	/**
	 * 更新消息未读数
	 */
	public void updateMsg(){
		if(null != mainActivity){
			mainActivity.updateMsg();
		}
	}
}