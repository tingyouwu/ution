package com.wty.ution.widget.filter.base;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wty.ution.widget.filter.base.IFilterModel.InputMode;
import com.wty.ution.R;


public class FilterLabelItemView extends LinearLayout{
	
	IFilterModel model;
	TextView tv_content;
	EditText edt_content;
	TextView tv_label;
	RelativeLayout layout_select;
	FilterEventBus eventBus;
	String eventBusId;
	
	RelativeLayout layout_root;
	
	public FilterLabelItemView(Context context,FilterEventBus eventBus,String eventBusId) {
		super(context);
        setEventBus(eventBus,eventBusId);
		init();
	}

    public void setEventBus(FilterEventBus eventBus,String eventBusId){
        this.eventBus = eventBus;
        this.eventBusId = eventBusId;
    }

	private void init(){
		
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_filterlabel, this);
		
		tv_label = (TextView)findViewById(R.id.flb_textview);
		tv_content = (TextView)findViewById(R.id.flb_content_select);
		edt_content = (EditText)findViewById(R.id.flb_content_edt);
		layout_select = (RelativeLayout)findViewById(R.id.flb_layout_select);
		layout_root = (RelativeLayout)findViewById(R.id.flb_root);
	}
		
	public void setModel(final IFilterModel model){
		this.model = model;
		final InputMode mode = model.getInputMode();
		
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				model.onSelect(new IFilterModel.SelectCallback(){

					@Override
					public void onResult(FilterOptionModel result) {
						if(mode!= InputMode.Select)return;
						model.setTextValue(result.getText());
						model.setValue(result.getValue());
						setModel(model);
						if(eventBus!=null){
							if(!TextUtils.isEmpty(result.getValue())){
								eventBus.onSubmit(eventBusId,model);
							}else{
								eventBus.onCancel(eventBusId,model);
							}
						}
					}
					
				});
			}
		});
		
		if(mode==InputMode.Select){
			layout_select.setVisibility(View.VISIBLE);
			edt_content.setVisibility(View.GONE);
		}else{
			layout_select.setVisibility(View.GONE);
			edt_content.setVisibility(View.VISIBLE);
		}
		tv_label.setText(model.getLabel());
		String textValue = model.getTextValue() == null?"":model.getTextValue();
		edt_content.setText(textValue);
		edt_content.setHint("请输入"+model.getLabel());
		edt_content.addTextChangedListener(watcher);
		if(TextUtils.isEmpty(model.getValue())){
			tv_content.setText("不限");
			tv_content.setTextColor(getContext().getResources().getColor(R.color.gray_font_3));
		}else{
			tv_content.setText(textValue);
			tv_content.setTextColor(getContext().getResources().getColor(R.color.blue_crm));
		}
		
	}

	/**
	 * 特殊处理textview显示效果
	 **/
	public void setTextViewContent(String textValue){
		tv_content.setText(textValue);
	}

    public IFilterModel getFilterModel(){
        if(model!=null && (model.getInputMode()==InputMode.Text ||  model.getInputMode()==InputMode.TextArea) ){
            String value = edt_content.getText().toString();
            model.setTextValue(value);
            model.setValue(value);
        }
        return model;
    }

	TextWatcher watcher = new TextWatcher() {

		String before="";
		String after="";
		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			before = arg0.toString();
		}

		@Override
		public void afterTextChanged(Editable editable) {
			after = editable.toString();
			if(before.equals(after))return;
			countDowntimer.cancel();
			countDowntimer.start();
		}
	};
	
	
	CountDownTimer countDowntimer = new CountDownTimer(500,100) {

		@Override
		public void onTick(long millisUntilFinished) {

		}

		@Override
		public void onFinish() {
			String value = edt_content.getText().toString();
			if(model!=null && (model.getInputMode()==InputMode.Text ||  model.getInputMode()==InputMode.TextArea)){
				model.setTextValue(value);
				model.setValue(value);
			}
			if(eventBus!=null){
				if(model!=null && (model.getInputMode()==InputMode.Text ||  model.getInputMode()==InputMode.TextArea)){
					if(!TextUtils.isEmpty(value)){
						eventBus.onSubmit(eventBusId,model);
					}else{
						eventBus.onCancel(eventBusId,model);
					}
				}
			}
		}
	};
}