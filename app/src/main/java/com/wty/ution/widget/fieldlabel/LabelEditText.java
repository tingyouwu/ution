package com.wty.ution.widget.fieldlabel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


import com.wty.ution.R;
import com.wty.ution.base.AppContext;
import com.wty.ution.data.dalex.basedata.RegionInfoDALEx;
import com.wty.ution.ui.activity.RegionInfoActivity;
import com.wty.ution.ui.activity.RegionSelectActivity;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.CheckCodeButton;
import com.wty.ution.widget.CustomDatePickerDialog;
import com.wty.ution.widget.DateTimePicker;
import com.wty.ution.widget.MenuDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class LabelEditText extends LinearLayout {

    protected static final int DECIMAL_MAXLENGTH = 13; //总长度
    private int 				 resource			= R.layout.layout_let;

    public static final int      Type_Text          = 1;
    public static final int      Type_SingleChoice  = 2;
    public static final int      Type_Date          = 3;
    public static final int      Type_TextArea      = 4;
    public static final int      Type_Money         = 5;
    public static final int      Type_Int           = 6;
    public static final int      Type_Telephone     = 7;
    public static final int      Type_Location      = 8;
    public static final int      Type_Tree          = 9;
    public static final int      Type_MutipleChoice = 10;
    public static final int      Type_Passwrod      = 11;
    public static final int      Type_Decimal       = 12;
    public static final int      Type_DateTime      = 13;

    protected LinearLayout	 	 layout_root;
    protected LinearLayout	 	 layout_content;
    protected LinearLayout		 layout_foot;
    protected LinearLayout		 layout_header;
    
    protected EditText           edt_content;
    protected ImageButton        edt_clear;
    protected TextView 			 tv_content;
    protected TextView           tv_required;
    protected TextView           tv_label;
    protected LinearLayout		 layout_label;
    private ImageView            iv_multioption;
    private ImageView            iv_multioption2;
    private View                 underline;
    private ProgressBar          pb_loading;
    private CheckCodeButton btn_checkcode;
    private LinearLayout		 layout_multioption;
    protected String             dateShowFormat     = CommonUtil.DataFormat_list_year;
    protected String             dateTimeShowFormat     = CommonUtil.defaultDateFormat;
    protected String             content            = "";
    protected int                letType            = Type_Text;
    protected boolean            isReadOnly         = false;
    private boolean              isUnderline        = true;
    private boolean              isRequired         = false;
    private String               label              = "";
    private String               hint               = "";
    private int                  maxLenth           = 999;
    private int                  minLenth           = 0;
    private boolean              isHidden           = false;
    private boolean              singleline         = false;
    private boolean              cancelable         = false;
    private boolean 			 vcardmode			= false;
    private int                  labelTextColor     = 0xFF333333;
    private int                  contentTextColor   = 0xFF000000;
    /**
     *  map的 key = 中文选项， value = 选项对应的id 
     */
    private LinkedHashMap<String,String>  options = new LinkedHashMap<String,String>();

    // 日期插件
    Calendar calendar;
    
    private String[] vcardValue;
    private onCancelListener onCancelListener = null;
    private OnClickListener onClearListener = null;
    
    public LabelEditText(Context context){
    	super(context);
    	init(context);
    	setProperty();
    }
    
    public LabelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelEditText);

        label = ta.getString(R.styleable.LabelEditText_labelText);
        hint = ta.getString(R.styleable.LabelEditText_hintText);
        isUnderline = ta.getBoolean(R.styleable.LabelEditText_isUnderline, true);
        isRequired = ta.getBoolean(R.styleable.LabelEditText_isRequired, false);
        isReadOnly = ta.getBoolean(R.styleable.LabelEditText_isReadOnly, false);
        isHidden = ta.getBoolean(R.styleable.LabelEditText_isHidden, false);
        singleline = ta.getBoolean(R.styleable.LabelEditText_singleline, false);
        maxLenth = ta.getInt(R.styleable.LabelEditText_maxContentLength, 999);
        minLenth = ta.getInt(R.styleable.LabelEditText_minContentLength, 0);
        cancelable = ta.getBoolean(R.styleable.LabelEditText_isCanlable, false);
        labelTextColor = ta.getColor(R.styleable.LabelEditText_leftLabelTextColor, labelTextColor);
        contentTextColor = ta.getColor(R.styleable.LabelEditText_contentTextColor, contentTextColor);
        
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String name = attrs.getAttributeName(i);
            String value = attrs.getAttributeValue(i);
            if ("letType".equals(name) && value != null) {
            	if(!isInEditMode()){
            		letType = Integer.valueOf(value);
            	}
                
            }
        }
        ta.recycle();
        setProperty();
    }
    
    protected void setProperty(){
    	setMinLenth(minLenth);
        setSingleline(singleline);
        setHint(hint);
        setMaxLength(maxLenth);
        setLabel(label);
        setUnderline(isUnderline);
        setIsRequired(isRequired);
        setIsHidden(isHidden);
        setViewByType(letType);
        setIsReadOnly(isReadOnly);
        setLabelTextColor(labelTextColor);
        setContentTextColor(contentTextColor);
    }
    
    /**
     * 根据控件的类型设置相应的样式操作
     * @param type
     */
    public void setViewByType(int type) {
        this.letType = type;
        edt_content.setSingleLine(singleline);
        tv_content.setSingleLine(singleline);
        try {
            switch (type) {

            case Type_Text:
                
                iv_multioption.setVisibility(View.INVISIBLE);
                edt_content.setFocusableInTouchMode(true);
                edt_content.setInputType(InputType.TYPE_CLASS_TEXT);
                
                break;
            case Type_SingleChoice:
            	
            	iv_multioption.setVisibility(View.VISIBLE);
                iv_multioption.setImageResource(R.drawable.img_nav_down);

                edt_content.setSingleLine(false);
            	tv_content.setSingleLine(false);
            	tv_content.setMaxLines(3);
                
                setContentListener(singleClickListener);
                break;
               
            case Type_MutipleChoice:
            	iv_multioption.setVisibility(View.VISIBLE);
                iv_multioption.setImageResource(R.drawable.img_nav_down);
//                edt_content.setFocusable(false);
                edt_content.setSingleLine(false);
            	tv_content.setSingleLine(false);
            	
                break;
            case Type_Location:
            	iv_multioption.setVisibility(View.VISIBLE);
            	iv_multioption.setImageResource(R.drawable.img_locationnav);

            	edt_content.setSingleLine(false);
            	tv_content.setSingleLine(false);
            	tv_content.setMaxLines(3);
                break;
            case Type_Tree:
                edt_content.setMaxLines(2);
                edt_content.setSingleLine(false);
                tv_content.setMaxLines(2);
                tv_content.setSingleLine(false);
                
            	iv_multioption.setVisibility(View.VISIBLE);
            	iv_multioption.setImageResource(R.drawable.img_nav_down);
                
                setContentListener(regionClickListener);
                break;

            case Type_Date:
            	iv_multioption.setVisibility(View.VISIBLE);
            	iv_multioption.setImageResource(R.drawable.img_nav_down);

            	setContentListener(dateSelectedClickListener);
            	
                break;
                
            case Type_DateTime:
            	iv_multioption.setVisibility(View.VISIBLE);
            	iv_multioption.setImageResource(R.drawable.img_nav_down);

            	setContentListener(dateTimeSelectedClickListener);
            	
                break;
            case Type_Money:
            	iv_multioption.setVisibility(View.INVISIBLE);
                edt_content.setInputType(8194);
                break;
            case Type_TextArea:

                iv_multioption.setVisibility(View.INVISIBLE);
                edt_content.setInputType(InputType.TYPE_CLASS_TEXT);
                edt_content.setSingleLine(false);
                tv_content.setSingleLine(false);
                this.post(new Runnable() {
                    @Override
                    public void run() {
                        edt_content.setMinHeight(CommonUtil.dip2px(getContext(), 100));
                    }
                });
                break;
            case Type_Int:
                iv_multioption.setVisibility(View.INVISIBLE);

                edt_content.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case Type_Decimal:
                iv_multioption.setVisibility(View.INVISIBLE);

                edt_content.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                edt_content.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(13, 4)});
                break;
            case Type_Telephone:
            	iv_multioption.setVisibility(View.INVISIBLE);
            	
            	tv_content.setSingleLine(false);
            	edt_content.setSingleLine(false);
                edt_content.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case Type_Passwrod:
            	iv_multioption.setVisibility(View.INVISIBLE);
            	
            	edt_content.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            	edt_content.addTextChangedListener(new TextWatcher() {
            		String tmp = "";   
            	    String digits = "._0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLIMNOPQRSTUVWXYZ";   
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {   
				    }   
				    @Override  
				    public void beforeTextChanged(CharSequence s, int start, int count, int after) {   
				        tmp = s.toString();   
				    } 
					
					@Override
					public void afterTextChanged(Editable s) {
						String str = s.toString();   
				        if(str.equals(tmp)){   
				            return;   
				        }   
				           
				        StringBuffer sb = new StringBuffer();   
				        for(int i = 0; i < str.length(); i++){   
				            if(digits.indexOf(str.charAt(i)) >= 0){   
				                sb.append(str.charAt(i));   
				            }   
				        }   
				        tmp = sb.toString();   
				        edt_content.setText(tmp);
				        edt_content.setSelection(tmp.length());
					}
				});
                break;
            default:
                // 默认给Text类型
                iv_multioption.setVisibility(View.INVISIBLE);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取content的文字
     * @return
     */
    public String getContentValue() {
    	return edt_content.getText().toString();
    }
    /**
     * 更新图标状态
     */
	protected void refreshIcon() {

		if (cancelable) {
			if (!TextUtils.isEmpty(getContentValue())) {
				iv_multioption.setVisibility(View.VISIBLE);
				iv_multioption.setImageResource(R.drawable.img_notice_min);
				iv_multioption.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						setValue(null);
						onCancelListener.onCancel(LabelEditText.this);
						refreshIcon();
					}
				});
			} else {
				iv_multioption.setClickable(false);
				try {
					switch (this.letType) {

					case Type_Text:

						iv_multioption.setVisibility(View.GONE);

						break;
					case Type_SingleChoice:

						iv_multioption.setVisibility(View.VISIBLE);
						iv_multioption
								.setImageResource(R.drawable.img_nav_down);
						break;

					case Type_MutipleChoice:
						iv_multioption.setVisibility(View.VISIBLE);
						iv_multioption
								.setImageResource(R.drawable.img_nav_down);

						break;
					case Type_Location:
						iv_multioption.setVisibility(View.VISIBLE);
						iv_multioption
								.setImageResource(R.drawable.img_locationnav);

						break;
					case Type_Tree:

						iv_multioption.setVisibility(View.VISIBLE);
						iv_multioption
								.setImageResource(R.drawable.img_nav_down);

						break;

					case Type_Date:
					case Type_DateTime:

						iv_multioption.setVisibility(View.VISIBLE);
						iv_multioption
								.setImageResource(R.drawable.img_nav_down);

						break;
					case Type_Money:
						iv_multioption.setVisibility(View.INVISIBLE);
						break;
					case Type_TextArea:
						iv_multioption.setVisibility(View.INVISIBLE);
						break;
					case Type_Int:
						iv_multioption.setVisibility(View.INVISIBLE);

						break;
					case Type_Decimal:
						iv_multioption.setVisibility(View.INVISIBLE);

						break;
					case Type_Telephone:
						iv_multioption.setVisibility(View.INVISIBLE);

						break;
					case Type_Passwrod:
						iv_multioption.setVisibility(View.INVISIBLE);
						break;
					default:
						// 默认给Text类型
						iv_multioption.setVisibility(View.INVISIBLE);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
    
    /**
     * 初始化控件
     * 
     * @param context
     */
    protected void init(Context context) {

        // 以下代码实现动态加载xml布局文件
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li;
        // 获取LAYOUT_INFLATER_SERVICE，实例化LayoutInflater，实现动态加载布局
        li = (LayoutInflater) context.getSystemService(infService);
        li.inflate(resource, this);

        
        layout_root = (LinearLayout)findViewById(R.id.let_layout_root);
        layout_content = (LinearLayout)findViewById(R.id.let_layout_content);
        tv_label = (TextView) findViewById(R.id.let_textview);
        layout_label = (LinearLayout)findViewById(R.id.let_layout_label);
        tv_required = (TextView) findViewById(R.id.let_required);
        underline = (View) findViewById(R.id.let_underline);
        iv_multioption = (ImageView) findViewById(R.id.let_multioption);
        iv_multioption2 = (ImageView) findViewById(R.id.let_multioption2);
        edt_content = (EditText) findViewById(R.id.let_edittext);
        edt_clear = (ImageButton)findViewById(R.id.let_editclear);
        pb_loading = (ProgressBar) findViewById(R.id.let_loading);
        layout_multioption = (LinearLayout) findViewById(R.id.let_layout_multioption);
        btn_checkcode = (CheckCodeButton)findViewById(R.id.let_btn_checkcode);
        tv_content = (TextView)findViewById(R.id.let_tv_content);

        layout_foot = (LinearLayout)findViewById(R.id.let_layout_footview);
        layout_header = (LinearLayout)findViewById(R.id.let_layout_headview);
        
        edt_content.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textview, int code,KeyEvent key) {
				// 此处为得到焦点时的处理内容
				keyboardControl(false, edt_content);
				return true;
			}
		});

        if(!isInEditMode()){
            calendar = CommonUtil.get1980();
        }

    }

    /**
     * 设置键盘的键
     * 
     * @param imeOptions
     */
    public void setImeOption(int imeOptions) {
        edt_content.setImeOptions(imeOptions);
    }

    public void cancel(){
    	setValue(null);
		onCancelListener.onCancel(LabelEditText.this);
		refreshIcon();
    }
    /**
     * 设置焦点改变事件
     */
    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        edt_content.setOnFocusChangeListener(listener);
    }

    /**
     * 设置单选控件清除功能
     **/
    public void setOnClearContentListener(OnClickListener listener){
        if(this.letType == Type_SingleChoice) {
            if (listener != null) {
                onClearListener = listener;
                edt_clear.setOnClickListener(listener);
            }
        }
    }

    /**
     * 键盘键监听
     * 
     * @param listener
     */
    public void setOnEditorActionListener(OnEditorActionListener listener) {
        edt_content.setOnEditorActionListener(listener);
    }

    /**
     * 显示菊花
     */
    public void showLoading() {
        pb_loading.setVisibility(View.VISIBLE);
        iv_multioption.setVisibility(View.GONE);
        iv_multioption2.setVisibility(View.GONE);
    }

    /**
     * 隐藏菊花
     */
    public void hideLoading() {
        pb_loading.setVisibility(View.GONE);
        switch (letType) {
		case Type_SingleChoice:
			iv_multioption.setVisibility(View.VISIBLE);
			break;
		default:
			iv_multioption.setVisibility(View.GONE);
			break;
		}
    }

    /**
     * 控件下面的线
     * 
     * @param isUnderline
     */
    public void setUnderline(boolean isUnderline) {
        this.isUnderline = isUnderline;
        if (isUnderline) {
            underline.setVisibility(View.VISIBLE);
        } else {
            underline.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @param hint
     */
    public void setHint(String hint) {
        this.hint = hint;
        edt_content.setHint(hint);
    }

    /**
     * 设置输入框最大值
     */
    public void setLabel(String label) {
        tv_label.setText(label);
        this.label = label;
    }

    /**
     * 设置label字体颜色
     **/
    public void setLabelTextColor(int color){
        tv_label.setTextColor(color);
        this.labelTextColor = color;
    }

    /**
     * 设置edittext字体颜色
     **/
    public void setContentTextColor(int color){
        edt_content.setTextColor(color);
        tv_content.setTextColor(color);
        this.contentTextColor = color;
    }

    /**
     * 设置输入框最大值
     * 
     * @param maxLenth
     */
    public void setMaxLength(int maxLenth) {
        this.maxLenth = maxLenth;
        InputFilter.LengthFilter lengthfilter = new InputFilter.LengthFilter(maxLenth);
        edt_content.setFilters(new InputFilter[] { lengthfilter });
    }

    /**
     * 设置是否必填
     * 
     * @param isRequired
     */
    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;
        if (isRequired) {
            tv_required.setVisibility(View.VISIBLE);
        } else {
            tv_required.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置是否必填
     */
    public void setIsRequired(int isAllowEmpty) {
        if (isAllowEmpty == 0) {
            tv_required.setVisibility(View.VISIBLE);
            this.isRequired = true;
        } else {
            tv_required.setVisibility(View.INVISIBLE);
            this.isRequired = false;
        }
    }
    
    public void setIsHidden(boolean isHidden){
        if(isHidden){
            this.setVisibility(View.GONE);
        }else{
           this.setVisibility(View.VISIBLE); 
        }
    }
    
    public void setIsReadOnly(boolean isReadOnly){
    	this.isReadOnly = isReadOnly;
        if(isReadOnly){
        	tv_required.setVisibility(View.GONE);
        	edt_content.setVisibility(View.GONE);
        	tv_content.setVisibility(View.VISIBLE);
        	
        	if(letType == Type_Location){
				layout_multioption.setVisibility(View.VISIBLE);
				setContentListener(showLocationClickListener);
				
        	}else{
        		layout_multioption.setVisibility(View.GONE);
        		tv_content.setClickable(false);
        		this.setClickable(false);
        	}
        }else{
        	edt_content.setVisibility(View.VISIBLE);
        	tv_content.setVisibility(View.GONE);
        	if(letType == Type_Location){
        		this.setOnClickListener(new ContentClickListener(locationClickListener));
        	}
        }
    }


//    public interface OnEditChangeListener {
//        public void onChange(String content);
//
//        public void onEmpty();
//    }

    public String getLabel() {
        return label;
    }

    /**
     * 校验内容
     * 
     * @return
     */
    public boolean validate() {
        boolean result = false;
        // 必填没填，直接返回失败
        if (isRequired) {
            if (isEmpty()) {
                result = false;
            } else {
                result = true;
            }
        } else {
            result = true;
        }
        return result;
    }

    /**
     * 检查内容是否为空
     * 
     * @return
     */
    public boolean isEmpty() {

        boolean isEdtEmpty = true;
        String edtContent = edt_content.getText().toString();
        if (edtContent == null || TextUtils.isEmpty(edtContent)) {
            isEdtEmpty = true;
        } else {
            isEdtEmpty = false;
        }
        return isEdtEmpty;
    }

    public void setOriginalValue(String content){
    	if(letType == Type_Location){
    		edt_content.setText(content);
    		tv_content.setText(content);
    	}else{
    		setValue(content);
    	}
    }
    
    public void setValue(String content) {
        this.content = content;
        
		if (TextUtils.isEmpty(content)) {
			edt_content.setText("");
			tv_content.setText("");

            if(onClearListener != null){
                edt_clear.setVisibility(View.GONE);
            }

			return;
		}
        switch (letType) {
        case Type_Text:
            edt_content.setText(content);
            tv_content.setText(content);
            break;
        case Type_SingleChoice:
            edt_content.setText(content);
            tv_content.setText(content);
            if(onClearListener != null){
                edt_clear.setVisibility(View.VISIBLE);
            }
            break;
        case Type_MutipleChoice:
            edt_content.setText(content);
            tv_content.setText(content);
            break;
        case Type_Location:
            try {
            	if(TextUtils.isEmpty(content)){
            		edt_content.setText("");
            		tv_content.setText("");
            		return;
            	}
            	String[] contentSplit = content.split(";");
            	switch (contentSplit.length){
            	case 1:
            		//只有一位，地址：
                    edt_content.setText(contentSplit[0]);
                    tv_content.setText(contentSplit[0]);
            		break;
            	case 2:
            		//只有坐标没有地址：
                    edt_content.setText("");
                    tv_content.setText("");
            		break;
            	case 3:
            		//地址和坐标都具备
                    edt_content.setText(contentSplit[2]);
                    tv_content.setText(contentSplit[2]);
            		break;
            	default:
            		edt_content.setText("");
                    tv_content.setText("");
            		//未知
            		break;
            	}
            	

            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        case Type_Tree:
            try {
            	if(!TextUtils.isEmpty(content)){
            		RegionInfoDALEx region = RegionInfoDALEx.get().queryById(Integer.valueOf(content));
            		edt_content.setText(region!=null?region.getNamepath():"");
            		tv_content.setText(region!=null?region.getNamepath():"");
            	}
            } catch (Exception e) {
                edt_content.setText("");
                tv_content.setText("");
                e.printStackTrace();
            }
            break;
        case Type_Date:

            try{
                String dateStr = CommonUtil.dateToFormat(dateShowFormat,content);
                edt_content.setText(dateStr);
                tv_content.setText(dateStr);
            }catch (Exception e){
                e.printStackTrace();
                edt_content.setText(content);
                tv_content.setText(content);
            }

            break;
        case Type_DateTime:
            edt_content.setText(content);
            tv_content.setText(content);
            break;
        case Type_Money:
            edt_content.setText(content);
            tv_content.setText(content);
            break;
        case Type_TextArea:
            edt_content.setText(content);
            tv_content.setText(content);
            break;
        case Type_Int:
            edt_content.setText(content);
            tv_content.setText(content);
            break;
        case Type_Decimal:
            edt_content.setText(content);
            tv_content.setText(content);
            break;
        case Type_Telephone:
            edt_content.setText(content);
            tv_content.setText(content);
            break;
        case Type_Passwrod:
            edt_content.setText(content);
            tv_content.setText(content);
            break;    
        default:
            edt_content.setText(content);
            tv_content.setText(content);
            break;
        }
        refreshIcon();
    }

    /**
     * 取值
     * 
     */
    public String getValue() {
        String result = "";
        switch (letType) {
        case Type_Text:
            result = edt_content.getText().toString();
            
            break;
        case Type_SingleChoice:
            result = options.size()==0? "":options.get(edt_content.getText().toString()); 
            break;
        case Type_MutipleChoice:
            result = edt_content.getText().toString();
            break;
        case Type_Location:
        	String address = edt_content.getText().toString();
        	if(TextUtils.isEmpty(address)){
        		result = "";
        	}else{
	            try {
	                String[] valuesArray = content.split(";");
	                if (valuesArray.length == 1 ) {
	                	//没有坐标信息
	                    result = ";;" + address;
	                }
	                else{
	                	result = valuesArray[0]+";"+ valuesArray[1] +";"+address;
	                	if(TextUtils.isEmpty(address)){
	                		result = ";;";
	                	}
	                }
	            } catch (Exception e) {
	            	e.printStackTrace();
	                result = address;
	            }
        	}
            break;
        case Type_Tree:
            result = content;
            break;
        case Type_Date:
        case Type_DateTime:
            if(!TextUtils.isEmpty(content))return content;
            result = edt_content.getText().toString();
            break;
        case Type_Money:
            result = edt_content.getText().toString();
            break;
        case Type_TextArea:
            result = edt_content.getText().toString();
            break;
        case Type_Int:
            result = edt_content.getText().toString();
            break;
        case Type_Decimal:
            result = edt_content.getText().toString();
            break;
        case Type_Telephone:
            result = edt_content.getText().toString();
            break;
        case Type_Passwrod:
        	result = edt_content.getText().toString();
        	break;
        default:
            result = edt_content.getText().toString();

            break;
        }
        return result;
    }

    public EditText getEditText(){
    	return edt_content;
    }

    /**
     * 弹出选择拍照或选择照片裁剪框
     */
    private void showSingleChoiceDialog(final String[] optionsText) {
    	if(optionsText==null)return;
        final Context dialogContext = new ContextThemeWrapper(getContext(),
                android.R.style.Theme_Light);

        MenuDialog.Builder builder = new MenuDialog.Builder(dialogContext);
        MenuDialog dialog = builder
//                .setTitle("请选择")
                .setItems(optionsText, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(onOptionSelectedListener!=null){
                        	if(!onOptionSelectedListener.onSelected(optionsText[which],options.get(optionsText[which]))){
                        		if(optionsText[which].equals("无")){
                        			edt_content.setText("");
                        		}else{
                        			edt_content.setText(optionsText[which]);
                        		}
                        	}
                        	
                        }else{
                        	if(optionsText[which].equals("无")){
                    			edt_content.setText("");
                    		}else{
                    			edt_content.setText(optionsText[which]);
                    		}
                        }
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    
    public void setOptions(LinkedHashMap<String,String> values){
        setOptions(values,false);
    }
    
    public void setOptions(LinkedHashMap<String,String> values,boolean hasEmptyValue){
        if(hasEmptyValue){
        	this.options.put("无", "");
        }
        this.options.putAll(values);
        setContentListener(singleClickListener);
    }

    /**
	 * 显示日期对话框
	 * 
	 */
	private void showDateDialog(Context context, OnDateSetListener mDateSetListener) {
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
		CustomDatePickerDialog datePickDialog = new CustomDatePickerDialog(context, mDateSetListener, mYear, mMonth, mDay);
		datePickDialog.show();
	}
	
    /**
	 * 显示日期时间对话框
	 * 
	 */
	private void showDateTimeDialog(Context context, DateTimePicker.ICustomDateTimeListener dateTimeListener) {
		DateTimePicker dateTimePicker = new DateTimePicker(context, dateTimeListener);
		dateTimePicker.showDialog();
	}

	/**
	 * 设置日期控件的默认时间
	 * @param defaultDate
	 */
	public void setDefaultDate(String defaultDate){
		if(TextUtils.isEmpty(defaultDate))return;
		try {
			this.calendar = CommonUtil.dateStrToCalendar(defaultDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setDefaultDate(Date date){
		this.calendar.setTime(date);

	}


	public void setDate(int year, int month, int day){

		calendar.set(year,month,day);
        String time = CommonUtil.calendarToDate(calendar, CommonUtil.defaultDateFormat);
        setValue(time);

	}
	
	public void setDateTime(Calendar calendar){
        String text = CommonUtil.calendarToDateTime(calendar);
		edt_content.setText(text);
        tv_content.setText(text);
	}
	
	public void setDate(String dateString,String dateformat){
		SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
		try {
			Date date = sdf.parse(dateString);
			setDefaultDate(date);
            this.content = dateString;
			setValue(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public LinearLayout getMultiOptionLayout(){
		return layout_multioption;
	}
	public ImageView getMultiOption(){
		return iv_multioption;
	}
	
	public ImageView getMultiOption2(){
		return iv_multioption2;
	}
	
	public CheckCodeButton getCheckcodeButton(){
		return btn_checkcode;
	}

	public LinearLayout getLabelLayout(){
		return layout_label;
	}
	
	public TextView getLabelRedTextView(){
		return tv_required;
	}
	
	public void unregisterContentListener(){
		edt_content.setClickable(true);
		edt_content.setFocusable(true);
		tv_content.setFocusable(true);
		edt_content.setOnClickListener(new ContentClickListener(null));
		tv_content.setOnClickListener(new ContentClickListener(null));
		this.setOnClickListener(new ContentClickListener(null));
	}
	
	public void setOnContentClickListener(OnClickListener clickListener){
		setContentListener(clickListener);
	}
	
	public void setOnOptionSelectedListener(OnOptionSelectedListener clickListener){
		this.onOptionSelectedListener = clickListener;
	}
	
	public void setOnDateSelectedListener(final OnDateSetListener dateSetListener){
		if(this.letType == Type_Date){
			
			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showDateDialog(getContext(), dateSetListener);
				}
			};
			setContentListener(listener);
		}
	}

	protected OnOptionSelectedListener onOptionSelectedListener;
	public interface OnOptionSelectedListener{
		public boolean onSelected(String option, String optionid);
	}

	OnClickListener locationClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
//			try {
//				MultiMediaActivity activity = (MultiMediaActivity) getContext();
//				activity.pickFromMapView(edt_content.getText().toString());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}

	};


	OnClickListener showLocationClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
//			try {
//				MultiMediaActivity activity = (MultiMediaActivity) getContext();
//				activity.showAddress(edt_content.getText().toString());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
	};



	OnClickListener regionClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Activity activity = (Activity) getContext();
            Intent i = new Intent(activity, RegionSelectActivity.class);
            int regionid = RegionInfoDALEx.defaultId;
            i.putExtra("regionid", regionid);
            activity.startActivityForResult(i, AppContext.Constant.ActivityResult_FieldLabel_RegionSelect);
        }
    };

    OnClickListener dateSelectedClickListener = null;
    {
    	if (!isInEditMode()) {
    		dateSelectedClickListener =  new OnClickListener() {

    			@Override
    			public void onClick(View view) {
    				if (isInEditMode()) { return; }
    				showDateDialog(getContext(), new OnDateSetListener() {
    					public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
    						setDate(year, monthOfYear, dayOfMonth);
    					}
    				});
    			}
    		};
    	}
    }

    OnClickListener dateTimeSelectedClickListener = null;
    {
    	if (!isInEditMode()) {
    		dateTimeSelectedClickListener =  new OnClickListener() {

    			@Override
    			public void onClick(View view) {
    				if (isInEditMode()) { return; }
    				showDateTimeDialog(getContext(), new DateTimePicker.ICustomDateTimeListener() {

    					@Override
    					public void onSet(Calendar calendarSelected, Date dateSelected,
    							int year, String monthFullName, String monthShortName,
    							int monthNumber, int date, String weekDayFullName,
    							String weekDayShortName, int hour24, int hour12, int min,
    							int sec, String AM_PM) {
    						LabelEditText.this.calendar = calendarSelected;
    						setDateTime(calendarSelected);
    					}

    					@Override
    					public void onCancel() {

    					}
    				});
    			}
    		};
    	}
    }

    OnClickListener singleClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            try {
                if (options!=null && !options.isEmpty()) {
                	showSingleOptionsDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

	public void showSingleOptionsDialog() {
		List<String> ops = new ArrayList<String>();
		Iterator<Entry<String, String>> it = options.entrySet().iterator();
		while(it.hasNext()){
			Entry<String,String> entry = (Entry<String,String>) it.next();
			String text = entry.getKey();
			ops.add(text);
		}
		showSingleChoiceDialog(ops.toArray(new String[ops.size()]));
		
	}

	public boolean isSingleline() {
		return singleline;
	}

	public void setSingleline(boolean singleline) {
		this.singleline = singleline;
		edt_content.setSingleLine(singleline);
        tv_content.setSingleLine(singleline);
	}

	public void setMinLenth(int minLenth) {
		this.minLenth = minLenth;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	public void setContentListener(OnClickListener listener){
		edt_content.setFocusable(false);
		tv_content.setFocusable(false);
		edt_content.setOnClickListener(new ContentClickListener(listener));
		tv_content.setOnClickListener(new ContentClickListener(listener));
		this.setOnClickListener(new ContentClickListener(listener));
	}

	class ContentClickListener implements OnClickListener{
		OnClickListener listener;
		public ContentClickListener(OnClickListener listener) {
			this.listener = listener;
		}
		
		@Override
		public void onClick(View v) {
			if(listener != null) {
				layout_root.requestFocus();
				listener.onClick(v);
			}
		}
	}
	
	public void keyboardControl(boolean show,EditText editText){
	    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	    if(show){
	        //显示键盘
	        imm.showSoftInput(editText, 0);
	    }else{
	        //隐藏键盘
	        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); 
	    }
	}

    public void setDecimalDigitsInputFilter(int digitsBeforeZero,
                                            int digitsAfterZero){
        edt_content.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(digitsBeforeZero, digitsAfterZero)});
    }

	class DecimalDigitsInputFilter implements InputFilter {

		Pattern mPattern;

		public DecimalDigitsInputFilter(int digitsBeforeZero,
				int digitsAfterZero) {
			mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1)
					+ "}+((\\.[0-9]{0," + (digitsAfterZero - 1)
					+ "})?)||(\\.)?");
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			
			Matcher matcher = mPattern.matcher(dest);
			if(!matcher.matches() || source.length() + dest.length() > DECIMAL_MAXLENGTH)
				return "";
			return null;
		}

	}
	
	public void setVCardMode(String[] vcardValue){
		vcardmode = true;
		unregisterContentListener();
		this.vcardValue = vcardValue;
		if(vcardValue!=null && vcardValue.length!=0){
			setOriginalValue(vcardValue[0]);
		}

        if(letType == Type_Location){
            //地址字段需要特殊处理
            iv_multioption.setVisibility(View.VISIBLE);
            iv_multioption.setImageResource(R.drawable.img_locationnav);
            iv_multioption.setOnClickListener(locationClickListener);

            iv_multioption2.setVisibility(View.VISIBLE);
            iv_multioption2.setOnClickListener(new VCardClickListener());
            this.setOnClickListener(new VCardClickListener());

        }else{
            iv_multioption.setVisibility(View.VISIBLE);
            iv_multioption.setImageResource(R.drawable.img_nav_down);
            iv_multioption.setOnClickListener(new VCardClickListener());
            this.setOnClickListener(new VCardClickListener());
        }

	}

	public int getViewType() {
		return this.letType;
	}
	
	class VCardClickListener implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			layout_root.requestFocus();
			List<String> list = new ArrayList<String>();
			list.add("无");
			
			if(vcardValue!=null){
				list.addAll(Arrays.asList(vcardValue));
			}
			showSingleChoiceDialog(list.toArray(new String[list.size()]));
		}
		
	}
	
	public boolean isVcardmode() {
		return vcardmode;
	}
	
	public interface onCancelListener {
		void onCancel(View view);
	}
	
	public void setOnCancelListener(onCancelListener listener) {
		this.onCancelListener = listener;
	}

    public void setDateShowFormat(String format){
        if(TextUtils.isEmpty(format))return;
        this.dateShowFormat = format;

    }

    public void clearValue(){
        edt_content.setText("");
        tv_content.setText("");
        content = "";
    }

}