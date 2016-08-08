package com.wty.ution.widget.fieldlabel.expandcontent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.wty.ution.R;


public class ExpandContentMobilePhone extends ExpandContentTelephone {
	public ExpandContentMobilePhone(Context context) {
		super(context);
		setViewByType(Type_Telephone);
	}

	@Override
	public void setMessageButton(){
		final String telStr = getFieldValue();
		if(TextUtils.isEmpty(telStr)){
			getMultiOption2().setVisibility(View.GONE);
		}else{
			getMultiOption2().setVisibility(View.VISIBLE);
			getMultiOption2().setImageResource(R.drawable.img_contact_message);
			getMultiOption2().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					if(TextUtils.isEmpty(telStr))return;
					Uri uri = Uri.parse("smsto:"+telStr);            
					Intent it = new Intent(Intent.ACTION_SENDTO, uri);            
					getContext().startActivity(it);
				}
			});
		}
		
	}
	
	public void hideMessageButton(){
		getMultiOption2().setVisibility(View.GONE);
	}
}
