package com.wty.ution.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.ution.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author wty
 */
public class ReportButton extends LinearLayout{

    boolean disableJump = false;

    @Bind(R.id.visitbtn_tv_text)
    TextView tv_text;

    @Bind(R.id.visitbtn_tv_count)
    TextView tv_count;

    public ReportButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_visitbutton ,this);
        ButterKnife.bind(this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VisitPlanButton);

        int count = ta.getInteger(R.styleable.VisitPlanButton_planCount,0);
        String text = ta.getString(R.styleable.VisitPlanButton_planText);
        setText(text);
        setCount(count);
        ta.recycle();
    }

    public void setText(String text){
        if(TextUtils.isEmpty(text)){
            this.tv_text.setText("");
            return;
        }
        this.tv_text.setText(text);
    }

    public void setCount(int count){
        this.tv_count.setText("" + count);
    }

    public void setDisableJump(boolean disableJump){
        this.disableJump = disableJump;
    }

}
