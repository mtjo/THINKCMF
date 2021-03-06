package com.thinkcmf.app.ui.find;

import java.util.ArrayList;

import com.thinkcmf.app.entity.FindLawDefaultSort;
import com.wanyou.lscn.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FindLawSortAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<FindLawDefaultSort> datalist;
	private int selectItem = -1;
	private int sColor;
	private int nColor;
	private OnItemClickListener mOnItemClickListener;
	private OnClickListener clickListener;
	
	public FindLawSortAdapter(Context context, ArrayList<FindLawDefaultSort> list, int ncolor,int scolor){
		this.mContext = context;
		this.datalist = list;
		this.sColor = scolor;
		this.nColor = ncolor;
		initListener();
	}

	@Override
	public int getCount() {
		return null == datalist? 0:datalist.size();
	}

	@Override
	public Object getItem(int position) {
		return null==datalist&&datalist.size()>0?null:datalist.get(position%datalist.size());
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public  void setSelectItem(int selectItem) {  
          this.selectItem = selectItem;  
     }  
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.select_listitem, parent, false);
			holder.name_tv = (TextView) convertView.findViewById(R.id.select_name_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		FindLawDefaultSort item = (FindLawDefaultSort) getItem(position);
		if(null != item){
			if(null != holder.name_tv){
				holder.name_tv.setText(item.getName());
			}
		}
		
		if(sColor != 0  && nColor != 0)
			 if (position == selectItem) {  
	             convertView.setBackgroundColor(mContext.getResources().getColor(sColor));  
	         }   
	         else {  
	             convertView.setBackgroundColor(mContext.getResources().getColor(nColor));  
	         }  
		holder.name_tv.setTag(position);
		holder.name_tv.setOnClickListener(clickListener);
		return convertView;
	}
	
	private void initListener(){
		clickListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				selectItem = (Integer) v.getTag();
				if (mOnItemClickListener != null) {
					mOnItemClickListener.onItemClick((FindLawDefaultSort)getItem(selectItem));
				}
				notifyDataSetChanged();
			}
		};
	}
	
	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(FindLawDefaultSort item);
	}
	
	private static class ViewHolder{
		public TextView name_tv;
	}
}
