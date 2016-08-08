package com.wty.ution.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ViewSwitcher;


import com.wty.ution.R;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.util.ScreenUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


@SuppressLint("NewApi")
public class DateTimePicker implements OnClickListener, OnDateChangedListener, OnTimeChangedListener
{
	private DatePicker datePicker;
	private TimePicker timePicker;
	private ViewSwitcher viewSwitcher;
	
	private final int SET_DATE=100,SET_TIME=101,SET=102,CANCEL=103;
	
	private Button btn_setDate,btn_setTime,btn_set,btn_cancel; 
	
	private Calendar calendar_date = null;
	
	private Calendar calendar_default = null;
	
	private Context context;
	
	private ICustomDateTimeListener iCustomDateTimeListener = null;
	
	private Dialog dialog;
	
	private boolean is24HourView = true;
	
	public DateTimePicker(Context context, ICustomDateTimeListener customDateTimeListener)
	{
		this.context = context;
		iCustomDateTimeListener = customDateTimeListener;
		
		this.calendar_date = Calendar.getInstance();
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View dialogView = getDateTimePickerLayout();
		dialog.setContentView(dialogView);
	}
	
	public DateTimePicker(Context context, ICustomDateTimeListener customDateTimeListener, Calendar calendar)
	{
		this.context = context;
		iCustomDateTimeListener = customDateTimeListener;
		this.calendar_date = calendar;
		this.calendar_default = calendar;
		
//		Toast.makeText(context, getDate(calendar_date, "yyyy-MM-dd HH:mm"), Toast.LENGTH_LONG).show();
		
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View dialogView = getDateTimePickerLayout();
		dialog.setContentView(dialogView);
	}
	
	private View getDividerView(LinearLayout layout) {
		return getDividerView(layout.getOrientation());
	}
	
	private View getDividerView(int orientation) {
		LinearLayout.LayoutParams divider_params;
		if(orientation == LinearLayout.HORIZONTAL) {
			divider_params = new LinearLayout.LayoutParams(CommonUtil.dip2px(context, 0.5), LinearLayout.LayoutParams.MATCH_PARENT);
		} else {
			divider_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtil.dip2px(context, 0.5));
		}
		View v = new View(context);
		v.setBackgroundResource(R.color.list_line);
		v.setLayoutParams(divider_params);
		return v;
	}
	
	public View getDateTimePickerLayout()
    {
    	LinearLayout.LayoutParams linear_match_wrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    	LinearLayout.LayoutParams linear_wrap_wrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    	FrameLayout.LayoutParams  frame_match_wrap = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    	
    	LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
    	
    	button_params.setMargins(0, 0, 0, 0);
    	
    	LinearLayout linear_main = new LinearLayout(context);
    	linear_main.setLayoutParams(linear_match_wrap);
    	linear_main.setOrientation(LinearLayout.VERTICAL);
    	linear_main.setGravity(Gravity.CENTER);
        linear_match_wrap.width = (int) (ScreenUtil.getSCREEN_WIDTH(context) * 0.88); // 宽度设置为屏幕的0.8

    	LinearLayout linear_child = new LinearLayout(context);
    	linear_child.setLayoutParams(linear_wrap_wrap);
    	linear_child.setOrientation(LinearLayout.VERTICAL);
    	
    	LinearLayout linear_top = new LinearLayout(context);
    	linear_top.setLayoutParams(linear_match_wrap);
    	
    	btn_setDate = new Button(context);
    	btn_setDate.setLayoutParams(button_params);
    	btn_setDate.setText(getDate(calendar_date, "yyyy-MM-dd"));
    	btn_setDate.setId(SET_DATE);
    	btn_setDate.setOnClickListener(this);
    	btn_setDate.setGravity(Gravity.CENTER);
    	btn_setDate.setBackgroundResource(R.drawable.datetime_btn_selector);
    	
    	btn_setTime = new Button(context);
    	btn_setTime.setLayoutParams(button_params);
    	btn_setTime.setText(getDate(calendar_date, "HH:mm"));
    	btn_setTime.setId(SET_TIME);
    	btn_setTime.setOnClickListener(this);
    	btn_setTime.setGravity(Gravity.CENTER);
    	btn_setTime.setBackgroundResource(R.drawable.datetime_btn_selector);
    	
    	linear_top.addView(btn_setDate);
    	linear_top.addView(getDividerView(linear_top));
    	linear_top.addView(btn_setTime);
    	
    	viewSwitcher = new ViewSwitcher(context);
    	viewSwitcher.setLayoutParams(frame_match_wrap);
    	
    	datePicker = new DatePicker(context);
    	datePicker.setCalendarViewShown(false);
    	datePicker.init(calendar_date.get(Calendar.YEAR), calendar_date.get(Calendar.MONTH), calendar_date.get(Calendar.DAY_OF_MONTH), this);
    	timePicker = new TimePicker(context);
    	timePicker.setCurrentHour(calendar_date.get(Calendar.HOUR_OF_DAY));
    	timePicker.setCurrentMinute(calendar_date.get(Calendar.MINUTE));
    	timePicker.setOnTimeChangedListener(this);
    	datePicker.setBackgroundResource(R.drawable.selector_white);
    	timePicker.setBackgroundResource(R.drawable.selector_white);
    	
    	viewSwitcher.addView(timePicker);
    	viewSwitcher.addView(datePicker);
    	
    	LinearLayout linear_bottom = new LinearLayout(context);
    	linear_match_wrap.topMargin = 8;
    	linear_bottom.setLayoutParams(linear_match_wrap);
    	
    	btn_set = new Button(context);
    	btn_set.setLayoutParams(button_params);
    	btn_set.setText("确定");
    	btn_set.setId(SET);
    	btn_set.setOnClickListener(this);
    	btn_set.setGravity(Gravity.CENTER);
    	btn_set.setBackgroundResource(R.drawable.selector_white);
    	
    	btn_cancel = new Button(context);
    	btn_cancel.setLayoutParams(button_params);
    	btn_cancel.setText("取消");
    	btn_cancel.setId(CANCEL);
    	btn_cancel.setOnClickListener(this);
    	btn_cancel.setGravity(Gravity.CENTER);
    	btn_cancel.setBackgroundResource(R.drawable.selector_white);
    	
    	linear_bottom.addView(btn_cancel);
    	linear_bottom.addView(getDividerView(linear_bottom));
    	linear_bottom.addView(btn_set);
    	
    	linear_child.addView(linear_top);
    	linear_child.addView(getDividerView(linear_child));
    	linear_child.addView(viewSwitcher);
    	linear_child.addView(getDividerView(linear_child));
    	linear_child.addView(linear_bottom);
    	
    	linear_main.addView(linear_child);
    	
    	return linear_main;
    }
	
	public void showDialog()
	{
		if(!dialog.isShowing())
		 {
			if(calendar_date==null)
			calendar_date = Calendar.getInstance();
			
			timePicker.setIs24HourView(is24HourView);
			timePicker.setCurrentHour(calendar_date.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(calendar_date.get(Calendar.MINUTE));
			
			datePicker.updateDate(calendar_date.get(Calendar.YEAR),calendar_date.get(Calendar.MONTH), calendar_date.get(Calendar.DATE));
			
			dialog.show();
			
			btn_setDate.performClick();
		 }
	}

	public void dismissDialog()
	{
		if(!dialog.isShowing())
		 dialog.dismiss();	
	}
	
	public void setDate(Calendar calendar)
	{
		if(calendar!=null)
		calendar_date = calendar;	
	}

	public void setDate(Date date)
	{
		if(date!=null)
		{
			calendar_date = Calendar.getInstance();
			calendar_date.setTime(date);	
		}
	}
	
	public void setDate(int year,int month,int day)
	{
		if( month<12 && month>=0 && day<32 && day>=0 && year>100 && year<3000)
		{
			calendar_date = Calendar.getInstance();
			calendar_date.set(year, month, day);
		}
		
	}
	public void setTimeIn24HourFormat(int hourIn24Format,int minute)
	{
	    if(hourIn24Format<24 && hourIn24Format>=0 && minute>=0 && minute<60)
	    {
	    	if(calendar_date==null)
	    	calendar_date = Calendar.getInstance();
	    	
	    	calendar_date.set(calendar_date.get(Calendar.YEAR),calendar_date.get(Calendar.MONTH), calendar_date.get(Calendar.DAY_OF_MONTH),hourIn24Format, minute);
	        
	    	is24HourView = true;
	    }
	}
	
	public void setTimeIn12HourFormat(int hourIn12Format,int minute,boolean isAM)
	{
		if(hourIn12Format<13 && hourIn12Format>0 && minute>=0 && minute<60)
	    {
			if(hourIn12Format==12)
				 hourIn12Format = 0;
		
			int hourIn24Format = hourIn12Format;
			
			if(!isAM)
			hourIn24Format += 12;
			
			if(calendar_date==null)
		    calendar_date = Calendar.getInstance();
			
			calendar_date.set(calendar_date.get(Calendar.YEAR),calendar_date.get(Calendar.MONTH), calendar_date.get(Calendar.DAY_OF_MONTH),hourIn24Format,minute);
	       
	    	is24HourView = false;
	    }
	}
	
	public void set24HourFormat(boolean is24HourFormat)
	{
		is24HourView = is24HourFormat;
	}
	
	public interface ICustomDateTimeListener 
	{
		public void onSet(Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM);
		public void onCancel();
	}
	

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
	    {
		 case SET_DATE: btn_setTime.setEnabled(true);
         				btn_setDate.setEnabled(false);
//         				btn_setDate.setBackgroundResource(R.color.white);
//         				btn_setTime.setBackgroundResource(R.color.gray_selector);
			            viewSwitcher.showNext();
			            break;
		
		 case SET_TIME: btn_setTime.setEnabled(false);
         				btn_setDate.setEnabled(true);
//         				btn_setDate.setBackgroundResource(R.color.gray_selector);
//         				btn_setTime.setBackgroundResource(R.color.white);
			            viewSwitcher.showPrevious();
			            break;
			
		 case SET:     	if(dialog.isShowing())
             			dialog.dismiss();
			            if(iCustomDateTimeListener!=null)
			            {
			            	int month = datePicker.getMonth();
			            	int year  = datePicker.getYear();
			            	int day  = datePicker.getDayOfMonth();
			            	
			            	calendar_date.set(year, month, day);
			            	
			            	int hourOfDay = timePicker.getCurrentHour().intValue();
			            	int minute =  timePicker.getCurrentMinute().intValue();
			            	
			            	calendar_date.set(year, month, day, hourOfDay, minute);
			            	
			            	iCustomDateTimeListener.onSet(calendar_date, calendar_date.getTime(), calendar_date.get(Calendar.YEAR), getMonthFullName(calendar_date.get(Calendar.MONTH)), getMonthShortName(calendar_date.get(Calendar.MONTH)),calendar_date.get(Calendar.MONTH), calendar_date.get(Calendar.DAY_OF_MONTH),getWeekDayFullName(calendar_date.get(Calendar.DAY_OF_WEEK)),getWeekDayShortName(calendar_date.get(Calendar.DAY_OF_WEEK)), calendar_date.get(Calendar.HOUR_OF_DAY), getHourIn12Format(calendar_date.get(Calendar.HOUR_OF_DAY)),calendar_date.get(Calendar.MINUTE),calendar_date.get(Calendar.SECOND),getAMPM(calendar_date));
			            }
			            resetData();
		                break;
	
		 case CANCEL:   if(dialog.isShowing())
			            dialog.dismiss();
			            if(iCustomDateTimeListener!=null)
			            iCustomDateTimeListener.onCancel(); 
			            resetData();
		                break;
		}
	}

	
	/**
	 * @param date date in String 
	 * @param fromFormat format of your <b>date</b> eg: if your date is 2011-07-07 09:09:09 then your format will be <b>yyyy-MM-dd hh:mm:ss</b> 
	 * @param toFormat format to which you want to convert your <b>date</b> eg: if required format is 31 July 2011 then the toFormat should be <b>d MMMM yyyy</b> 
	 * @return formatted date
	 */
	public static String convertDate(String date,String fromFormat,String toFormat)
	{
		String formattedDate="";
		try 
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fromFormat);
			Date d = simpleDateFormat.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			
			simpleDateFormat = new SimpleDateFormat(toFormat);
			simpleDateFormat.setCalendar(calendar);
			formattedDate = simpleDateFormat.format(calendar.getTime());
			
		} catch (Exception e) 
		{
		    if(e!=null)
			e.printStackTrace();
		}
		
		return formattedDate;
	}
	
	/**
	 * @param monthNumber Month Number starts with 0. For <b>January</b> it is <b>0</b> and for <b>December</b> it is <b>11</b>.
	 * @return
	 */
	private String getMonthFullName(int monthNumber) 
	{
		String monthName="";
		
		if(monthNumber>=0 && monthNumber<12)
		try 
		{
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, monthNumber);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
			simpleDateFormat.setCalendar(calendar);
			monthName = simpleDateFormat.format(calendar.getTime());
		} catch (Exception e) 
		{
		    if(e!=null)
			e.printStackTrace();
		}
		
		return monthName;
	}

	/**
	 * @param monthNumber Month Number starts with 0. For <b>January</b> it is <b>0</b> and for <b>December</b> it is <b>11</b>.
	 * @return
	 */
	private String getMonthShortName(int monthNumber) 
	{
		String monthName="";
		
		if(monthNumber>=0 && monthNumber<12)
		try 
		{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, monthNumber);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
		simpleDateFormat.setCalendar(calendar);
		monthName = simpleDateFormat.format(calendar.getTime());
		}
		catch (Exception e)
		{
			 if(e!=null)
			e.printStackTrace();
		}
		return monthName;
	}

	/**
	 * @param weekDayNumber Week Number starts with 1. For <b>Sunday</b> it is <b>1</b> and for <b>Saturday</b> it is <b>7</b>.
	 * @return
	 */
	private String getWeekDayFullName(int weekDayNumber) 
	{
		String weekName = "";
		
		if(weekDayNumber>0 && weekDayNumber<8)
		{
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
			simpleDateFormat.setCalendar(calendar);
			weekName = simpleDateFormat.format(calendar.getTime());
		} catch (Exception e) 
		{
		    if(e!=null)
			e.printStackTrace();
		}
		}
		return weekName;
	}

	/**
	 * @param weekDayNumber Week Number starts with 1. For <b>Sunday</b> it is <b>1</b> and for <b>Saturday</b> it is <b>7</b>.
	 * @return
	 */
	private String getWeekDayShortName(int weekDayNumber) 
	{
		String weekName = "";
		if(weekDayNumber>0 && weekDayNumber<8)
		{
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE");
			simpleDateFormat.setCalendar(calendar);
			weekName = simpleDateFormat.format(calendar.getTime());
		} catch (Exception e) {
			if(e!=null)
			e.printStackTrace();
		}
		}
		return weekName;
	}

	private int getHourIn12Format(int hour24) 
	{
	    int  hourIn12Format = 0;
		
	    if(hour24==0)
	    hourIn12Format = 12;
	    else if(hour24<=12)
		hourIn12Format = hour24;
	    else
		hourIn12Format = hour24-12;	
			
		return hourIn12Format;
	}
	
	private String getDate(Calendar calendar, String format) {
		//将Calendar类型转换成Date类型
		Date tasktime=calendar.getTime();
		//设置日期输出的格式
		SimpleDateFormat df=new SimpleDateFormat(format, Locale.getDefault());
		//格式化输出
		return df.format(tasktime);
	}

	private String getAMPM(Calendar calendar) 
	{
		String ampm = (calendar.get(Calendar.AM_PM)==(Calendar.AM))? "AM":"PM";
	    return ampm;
	}

	private void resetData()
	{
		calendar_date = calendar_default;
		is24HourView = true;
	}
	
	public static String pad(int i)
	{
		return i<=9?"0"+i:""+i;
	}
	
	public static String getSecondsFromMillis(long milliseconds) {
		return "" + ((int) ((milliseconds / 1000) % 60));
	}

	public static String getMinutesFromMillis(long milliseconds) {
		return "" + (int) ((milliseconds / (1000 * 60)) % 60);
	}

	public static String getHoursFromMillis(long milliseconds) {
		return "" + (int) ((milliseconds / (1000 * 60 * 60)) % 24);
	}
	
	/**
	 * @param monthNumber Month Number starts with 0. For <b>January</b> it is <b>0</b> and for <b>December</b> it is <b>11</b>.
	 * @param year 
	 * @return
	 */
	public static int getDaysInMonth(int monthNumber,int year)
	{
		  int days=0;
		  if(monthNumber>=0 && monthNumber<12){
		  try 
		{
			Calendar calendar = Calendar.getInstance();
			  int date = 1;
			  calendar.set(year, monthNumber, date);
			  days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		} catch (Exception e) 
		{
		    if(e!=null)
			e.printStackTrace();
		}
		  }
		  return days;
	}
	
	/**
	 * @param monthNumber Month Number starts with 0. For <b>January</b> it is <b>0</b> and for <b>December</b> it is <b>11</b>.
	 * @return
	 */
	public static int getDaysInMonthInPresentYear(int monthNumber)
	{
		   int days=0;
		   if(monthNumber>=0 && monthNumber<12){
		   try 
			{
				Calendar calendar = Calendar.getInstance();
				  int date = 1;
				  int year = calendar.get(Calendar.YEAR); 
				  calendar.set(year, monthNumber, date);
				  days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			} catch (Exception e) 
			{
			    if(e!=null)
				e.printStackTrace();
			}
		   }
			  return days;
	}
	
	public static int getDaysDifference(Date fromDate,Date toDate)
	{
		 if(fromDate==null||toDate==null)
			 return 0;
		 
		 return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	public static int getDaysDifference(Calendar calendar1,Calendar calendar2)
	{
		 if(calendar1==null||calendar2==null)
			 return 0;
		 
		 return (int)( (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (1000 * 60 * 60 * 24));
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		calendar_date = new GregorianCalendar(year, monthOfYear, dayOfMonth, calendar_date.get(Calendar.HOUR), calendar_date.get(Calendar.MINUTE));
		btn_setDate.setText(getDate(calendar_date, "yyyy-MM-dd"));
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		calendar_date = new GregorianCalendar(calendar_date.get(Calendar.YEAR), calendar_date.get(Calendar.MONTH), calendar_date.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
		btn_setTime.setText(getDate(calendar_date, "HH:mm"));
	}
	
}