package com.wty.ution.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wty.ution.R;
import com.wty.ution.widget.filter.base.FilterOptionModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 多选模式字段筛选控件
 * @author Amberllo
 */
@SuppressLint("ClickableViewAccessibility")
public class FilterMulSelectAdapter extends BaseAdapter {


	private Context context;
	//列表数据源
	private List<FilterOptionModel> models;

	//记录已选结果
	private LinkedHashMap<String,FilterOptionModel> choiceModel = new LinkedHashMap<String,FilterOptionModel>();

	public FilterMulSelectAdapter(Context context, List<FilterOptionModel> models, Map<String,FilterOptionModel> selected) {
		this.context = context;
		this.models = models;
		this.choiceModel.clear();
        this.choiceModel.putAll(selected);
	}

	@Override
	public int getCount() {
		return models.size();
	}

	@Override
	public FilterOptionModel getItem(int position) {
		return models.get(position);
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_filterlabell_select,null);
			holder = new ViewHolder();
			holder.init(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		FilterOptionModel model = getItem(position);
		holder.setValue(view, model);
		return view;
	}

	class ViewHolder {

		TextView tv_option; 
		
		CheckBox cb_selected;// 多选控件隐藏

		public void init(View view) {
			this.tv_option = (TextView) view.findViewById(R.id.item_flb_select_option);
			this.cb_selected = (CheckBox) view.findViewById(R.id.item_flb_select_cb);
		}

		public void setValue(View view, final FilterOptionModel model) {
		    
		    
			tv_option.setText(model.getText());

			cb_selected.setVisibility(View.VISIBLE);
			cb_selected.setClickable(false);

			final boolean isSelected = choiceModel.containsKey(model.getValue());
			cb_selected.setChecked(isSelected);

			view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

                    if(isSelected){
                        choiceModel.remove(model.getValue());
                    }else{
                        choiceModel.put(model.getValue(),model);
                    }
					FilterMulSelectAdapter.this.notifyDataSetChanged();
				}
			});
			
		}
	}
	
  	public LinkedHashMap<String,FilterOptionModel> getSelected(){
  		return choiceModel;
  	}

}