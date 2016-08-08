package com.wty.ution.widget;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wty.ution.R;

public class OtherToast {

    private Toast t;
    private TextView tv_tip;

    private Context context;

    public OtherToast(Context context) {
        this.context = context;
    }

    public void show(String text) {

        if (t == null) {
            t = new Toast(context);
            View content = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);

            tv_tip = (TextView) content.findViewById(R.id.toast_tip);
            t.setView(content);
            t.setDuration(Toast.LENGTH_SHORT);
        }
        tv_tip.setText(text);
        t.show();
    }
}
