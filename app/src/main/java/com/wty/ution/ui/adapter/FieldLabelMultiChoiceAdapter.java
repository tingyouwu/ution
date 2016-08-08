package com.wty.ution.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.wty.ution.R;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentMultiSelect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class FieldLabelMultiChoiceAdapter extends BaseAdapter {
	private Context context;
	//列表数据源
	private List<String> data;
	
	//上一次已经选上的选项
	private String selected;
	
	//记录多选结果
	private LinkedHashMap<String, Boolean> choiceIdMap = new LinkedHashMap<String, Boolean>();
	public FieldLabelMultiChoiceAdapter(Context context, List<String> data, String selected) {
		this.context = context;
		this.data = data;
		this.selected = selected;
		init();
	}
	
	private void init(){
		if(TextUtils.isEmpty(selected))return;
		//把已选项put到map里面
		String[] selectedArray = ExpandContentMultiSelect.cropValue(selected);
		for(String s:selectedArray){
			choiceIdMap.put(s, true);
		}
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public String getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_fieldlabel_multichoice,null);
			holder = new ViewHolder();
			holder.init(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		String option = getItem(position);
		holder.setValue(view, option, position);


		return view;
	}

	class ViewHolder {

		TextView tv_option; 
		
		CheckBox cb_selected;// 多选控件隐藏

		public void init(View view) {
			this.tv_option = (TextView) view.findViewById(R.id.item_fieldlabel_multichoice_option);
			this.cb_selected = (CheckBox) view.findViewById(R.id.item_fieldlabel_multichoice_cb);
		}

		public void setValue(View view, final String option,final int position) {
		    
		    
			tv_option.setText(option);

			cb_selected.setVisibility(View.VISIBLE);
			cb_selected.setClickable(false);

			Boolean hasChoice = choiceIdMap.get(option);
			cb_selected.setChecked(hasChoice == null ? false : hasChoice);

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 多选框至反
					cb_selected.setChecked(!cb_selected.isChecked());
					// 多选状态
					boolean isCheck = cb_selected.isChecked();
					
					if(choiceIdMap.containsKey(option)){
						choiceIdMap.remove(option);
					}
					// map记录状态
					choiceIdMap.put(option, isCheck);

				}
			});

			
		}
	}
	
  	public String getSelected(){
  		List<String> selected = new ArrayList<String>();
  		Iterator<Entry<String, Boolean>> iter = choiceIdMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Boolean> entry = (Entry<String, Boolean>) iter.next();
			String option= (String) entry.getKey();
			Boolean value = (Boolean) entry.getValue();
			if(value){
				selected.add(option);
			}
		}
		return TextUtils.join("|", selected);
  		
  	}
	
}
