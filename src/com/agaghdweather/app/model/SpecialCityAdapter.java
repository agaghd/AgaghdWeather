package com.agaghdweather.app.model;

import java.util.List;

import com.agaghdweather.app.util.MyApplication;
import com.agaghdweather.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SpecialCityAdapter extends ArrayAdapter<SpecialCity> {

	List<SpecialCity> list;
	
	CoolWeatherDB coolWeatherDB;
	
	private int resourceId;
	
	public SpecialCityAdapter(Context context, int textViewResourceId,
			List<SpecialCity> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
		this.list = objects;		//传递list的实例进来
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		SpecialCity sc = getItem(position);
		View view;
		ViewHolder viewHolder;
		
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.specialCityText = (TextView) 
					view.findViewById(R.id.special_city);
			viewHolder.dustbin = (Button)
					view.findViewById(R.id.dustbin);
			view.setTag(viewHolder);
		}
		else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		 
		viewHolder.specialCityText.setText(sc.getCountyName());
		viewHolder.dustbin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SpecialCity specialCity = list.get(position); 
				String countyName = specialCity.getCountyName();
				coolWeatherDB = CoolWeatherDB.getInstance(MyApplication.getContext());
				coolWeatherDB.deleteSpecial(countyName);
				list.remove(position);
			    notifyDataSetChanged();
				Toast.makeText(MyApplication.getContext(), "删除成功", Toast.LENGTH_LONG).show();
			}
		});
		return view;
		
	}
	
	class ViewHolder {
		TextView specialCityText;
		Button dustbin;
	}
	

}
