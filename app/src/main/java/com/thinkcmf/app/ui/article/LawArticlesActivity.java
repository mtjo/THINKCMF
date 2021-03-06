package com.thinkcmf.app.ui.article;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.base.aframe.Loger;
import com.base.aframe.bitmap.ImageLoader;
import com.base.aframe.http.StringCallBack;
import com.thinkcmf.app.ui.base.WebViewActivity;
import com.thinkcmf.app.api.HttpPostManager;
import com.thinkcmf.app.db.ArticleDBManager;
import com.thinkcmf.app.entity.Articles;
import com.thinkcmf.app.ui.base.BaseListViewActivity;

public class LawArticlesActivity extends BaseListViewActivity {
	private LawArticlesActivity mContext;
	private LawArticleAdapter adapter;
	private ArrayList<Articles> datalist = new ArrayList<Articles>();
	private static ArticleDBManager manager;
	
	/**
	 * 法律文章
	 * @param  aty
	 * 				要跳转到法律文章的Activity
	 */
	public static void LawArticles(Activity aty){
		if(null == aty){
			return;
		}
		Intent intent = new Intent(aty, LawArticlesActivity.class);
		aty.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		manager = new ArticleDBManager(this);
//		manager.delete();
//		manager = null;
		initListener();
		loadData(1);
	}
	
	@Override
	public void initView() {
		super.initView();
		mContext = this;
		
		setLoadMoreAble(false);
		adapter = new LawArticleAdapter(mContext, datalist);
		setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		if (manager != null) {
			manager.close();
		}
		ImageLoader.getInstance(mContext).stop();
		super.onDestroy();
	}
	
	@Override
	public void onRefresh(int pageNo, int pageSize) {
		super.onRefresh(pageNo, pageSize);
		loadData(1);
	}
	
	@Override
	public void onLoadMore(int pageNo, int pageSize) {
		loadData(pageNo);
	}

	private void initListener(){
		setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(arg2 > 0 && arg2 <= datalist.size()){
					Articles article = datalist.get(arg2 -1);
					if(null == article){
						return;
					}
					if (article.getIsRead()== 0) {
						manager.addArticles(article.getId());
					}
					article.setIsRead(1);
					adapter.notifyDataSetChanged();
					WebViewActivity.ShareWebView(mContext, article.getUrl(), "法律讲堂", article.getTitle(), article.getThumb());
				}
			}
		});
		if(null != xlistview){
			xlistview.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					switch (scrollState) {
						case OnScrollListener.SCROLL_STATE_FLING:
							adapter.setFlagBusy(true);
							break;
						case OnScrollListener.SCROLL_STATE_IDLE:
							adapter.setFlagBusy(false);
							break;
						case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
							adapter.setFlagBusy(false);
							break;
						default:
							break;
						}
						adapter.notifyDataSetChanged();
				}
			
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
				}
			});
		}
	}
	

//	public Handler handler = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			try {
//				ArrayList<Articles> list = (ArrayList<Articles>) msg.obj;
//				if(isReflash){
//					datalist.clear();
//				}
//				if(list.size() > 0)
//					datalist.addAll(list);
////				if(isReflash || isInit){
////					if(datalist.size() == 0){
////						ViewInject.showToast(mContext, "暂时没有更多的法律文章!");
////					}
////				}
//				if(null != adapter)
//					adapter.notifyDataSetChanged();
//				setLoadMoreAble(list.size() < pageSize? false:true);
//			} catch (Exception e) {
//				Loger.debug("解析法律文章出错", e);
//			}
//		};
//	};
	
//	private void showData(final JSONArray arr){
//			
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						ArrayList<Articles> list = Articles.jsonToList(arr,manager);
//						handler.sendMessage(handler.obtainMessage(22,list));
//					} catch (Exception e) {
//						Loger.debug("解析法律文章出错", e);
//					}
//				}
//			}).start();
//	}
	
	
	private void showData(JSONArray arr){
		try {
			ArrayList<Articles> list = Articles.jsonToList(arr,manager);
			if(isReflash){
				datalist.clear();
			}
			if(list.size() > 0)
				datalist.addAll(list);
//			if(isReflash || isInit){
//				if(datalist.size() == 0){
//					ViewInject.showToast(mContext, "暂时没有更多的法律文章!");
//				}
//			}
			if(null != adapter)
				adapter.notifyDataSetChanged();
			setLoadMoreAble(list.size() < pageSize? false:true);
		} catch (JSONException e) {
			Loger.debug("解析法律文章出错", e);
		} catch (Exception e) {
			Loger.debug("解析法律文章出错", e);
		}
	}
	
	private void loadData(int page){
		HttpPostManager.getArticleList(page,pageSize,
			new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				showData(this.getJsonArray());
				if(isReflash || isInit){
					if(datalist.size() == 0){
						setEmpty("暂时没有更多的法律文章!");
						return;
					}
				}
				setLoadSuccess();
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				setLoadError();
			}
		}, null, null);
	}
}
