package com.wty.ution.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wty.ution.R;


@SuppressLint("NewApi")
public class CustomItemView extends RelativeLayout implements OnClickListener{

	TextView tv_count;

    TextView tv_Label;

    ImageView iv_img;

    ImageView iv_point;
    Class<? extends Activity> clazz;

    View iv_underline;
    View iv_divide;


	public CustomItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.item_crm, this);
        iv_img = (ImageView)findViewById(R.id.item_crm_img);
        tv_Label = (TextView)findViewById(R.id.item_crm_label);
        tv_count = (TextView)findViewById(R.id.item_crm_count);
        iv_point = (ImageView)findViewById(R.id.item_crm_point);
        iv_underline = findViewById(R.id.item_crm_underline);
        iv_divide= findViewById(R.id.item_crm_divide);

        tv_count.setVisibility(View.GONE);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CrmItemView);
        Boolean underline = ta.getBoolean(R.styleable.CrmItemView_itemUnderline, false);
        Boolean divide = ta.getBoolean(R.styleable.CrmItemView_itemDivide,true);
        String label = ta.getString(R.styleable.CrmItemView_itemText);
        int resourceId = ta.getResourceId(R.styleable.CrmItemView_itemImage, 0);

        tv_Label.setText(label);
        iv_img.setImageResource(resourceId);
        iv_underline.setVisibility(underline ? View.VISIBLE : View.GONE);
        iv_divide.setVisibility(divide?View.VISIBLE:View.GONE);


        setPoint(View.GONE);
        setOnClickListener(this);
	}
	

    public void setPoint(int visable){
        iv_point.setVisibility(visable);
    }

    public void setCount(int count){

        this.tv_count.setVisibility(View.VISIBLE);
        this.tv_count.setText("" + count);
    }


    @Override
    public void onClick(View v) {
        if(clazz!=null){
            getContext().startActivity(new Intent(getContext(),clazz));
        }
    }

    public void setNextPage(Class<? extends Activity> clazz){
        this.clazz = clazz;
    }

}
