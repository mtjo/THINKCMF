package com.thinkcmf.app.ui.find;

import java.util.ArrayList;

import com.base.aframe.http.StringCallBack;
import com.base.aframe.json.parse.ParseJson;
import com.thinkcmf.app.api.HttpPostManager;
import com.thinkcmf.app.entity.FindLawDefaultSort;
import com.wanyou.lscn.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("NewApi")
public class FindLawFilterSort extends LinearLayout {
	private Activity mContext;
	private ListView listView;
	
	private FindLawSortAdapter padapter;
	private ArrayList<FindLawDefaultSort> plist = new ArrayList<FindLawDefaultSort>();
	
	
	private int tPPosition = 0;
	private int tCPosition = 0;
	
	private OnItemSelectListener mOnSelectListener;
	
	/**
	 * 设置选择监听
	 */
	public void setOnItemSelectListener(OnItemSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnItemSelectListener {
		public void getValue(FindLawDefaultSort item);
	}
	
	public FindLawFilterSort(Context context) {
		super(context);
		init(context);
	}
	
	public FindLawFilterSort(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public FindLawFilterSort(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	public FindLawFilterSort(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}
	
	private void init(Context context){
		mContext = (Activity) context;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.listview_layout, this, true);
		listView = (ListView) findViewById(R.id.listview);

		padapter = new FindLawSortAdapter(context, plist, 
				R.color.theme_window_background, R.color.theme_window_background_click);
		listView.setAdapter(padapter);
		padapter.setOnItemClickListener(new FindLawSortAdapter.OnItemClickListener() {			
			@Override
			public void onItemClick(FindLawDefaultSort item) {
				if(null != mOnSelectListener)
					mOnSelectListener.getValue(item);
			}
		});	
	}

	public void initData(){
		if(plist.size() > 0)return;
		
		loadDefaultFindlaw();//加载好律师在线排序
	}
	
	/**
	 * 加载好律师在线排序
	 */
	private void loadDefaultFindlaw() {
		HttpPostManager.getFindlawDefalutSort(new StringCallBack() {
			@Override
			public void onSuccess(Object t) {
				plist.clear();
				plist.addAll(ParseJson.getEntityList(this.getJsonArray().toString(), FindLawDefaultSort.class));
				padapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}
		}, mContext, null);
	}
}
