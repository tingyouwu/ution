package com.wty.ution.widget.report;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.ution.R;


public class ReportPrecentButton extends LinearLayout{


    private int unit;//单位类型
    private final static int Unit_Count = 1;
    private final static int Unit_Money = 2;
    private final static int Unit_Precent = 3;
    TextView tv_text;
    TextView tv_count;

    String text = "";
    String value = "";


    public ReportPrecentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ReportPrecentButton);
        String text = ta.getString(R.styleable.ReportPrecentButton_reportLabel);
        int value = ta.getInt(R.styleable.ReportPrecentButton_reportvalue, Unit_Count);
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String name = attrs.getAttributeName(i);
            String v = attrs.getAttributeValue(i);
            if ("reportunit".equals(name) &&  v!= null) {
                if(!isInEditMode()){
                    this.unit = Integer.valueOf(value);
                }

            }
        }
        ta.recycle();
        this.text = text;

        switch (unit){
            case Unit_Count:
                setCount(""+value);
                break;
            case Unit_Money:
                setMoney("" + value);
                break;
            case Unit_Precent:
                setPrecent("" + value);
                break;
        }
    }

    public void setText(String text){
        if(TextUtils.isEmpty(text)){
            this.tv_text.setText("");
            return;
        }
        this.tv_text.setText(text);
    }

    public void setMoney(String money){
        this.value = money;
        init(getContext(), R.layout.layout_reportbtn_money);
    }

    public void setCount(String count){
        this.value = (count==null?"":count);
        init(getContext(), R.layout.layout_reportbtn_count);
    }

    public void setPrecent(String precent){
        //去掉重复的百分号
        if(precent.contains("%"))precent = precent.replace("%","");
        this.value = precent;
        init(getContext(),R.layout.layout_reportbtn_precent);
    }

    private void init(Context context,int resouce){

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resouce,null);
        tv_text = (TextView)view.findViewById(R.id.report_precent_tv_text);
        tv_count = (TextView)view.findViewById(R.id.report_precent_tv_value);

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        removeAllViews();
        addView(view,params);

        this.tv_count.setText(value);
        this.tv_text.setText(text);
    }

}
