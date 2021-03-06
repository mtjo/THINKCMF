package com.thinkcmf.app.ui.start;

import com.base.aframe.utils.AppUtils;
import com.thinkcmf.app.utils.UMengUtil;
import com.wanyou.lscn.R;
import com.thinkcmf.app.config.LscnConfig;
import com.thinkcmf.app.ui.ask.AskLawyerActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class HomeFragment extends Fragment implements OnClickListener{
	private Activity aty;
	
	public HomeFragment(){
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.home_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		aty = getActivity();
		initView();
		showTip();
	}
	
	private void initView(){
		getActivity().findViewById(R.id.ask_img).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ask_img:
				UMengUtil.onEvtent(getActivity().getApplicationContext(), LscnConfig.UMENG_ASK_LAWYER1);
				AskLawyerActivity.open(getActivity(), 0);
				break;

			default:
				break;
		}
	}
	
	/************************首次使用功能引导提示**************************/
	/**
	 * 显示提示
	 */
	private void showTip(){
		String isMainGuid = AppUtils.getLocalCache(aty, "isMainGuid");
		if(!"true".equals(isMainGuid)){
			((MainActivity)getActivity()).showtipView(R.drawable.main_guid);
			AppUtils.saveLocalCache(aty, "isMainGuid", "true");
		}
	}
}
