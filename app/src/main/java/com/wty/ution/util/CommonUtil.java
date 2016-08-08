package com.wty.ution.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.wty.ution.base.AppContext;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class CommonUtil {

	public static final String DataFormat_list_year = "yyyy-MM-dd";
	public static final String DataFormat_list_month = "yyyy-MM";
	public static final String DataFormat_list_day = "MM-dd";
	public static final String DataFormat_list_time = "HH:mm";
	public static final String DateFormat_list_year_time = "yyyy-MM-dd HH:mm";

	// 完整的日期格式
	public static final String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";
	public static String getTime() {
		return getTime(defaultDateFormat);
	}
	public static String getTime(String dateFormat) {
		return new SimpleDateFormat(dateFormat).format(new Date());
	}

	public static Date getDate(String datetime){
		DateFormat df = new SimpleDateFormat(defaultDateFormat);
		Date date = null;
		try{
			date = df.parse(datetime);
		}catch (Exception e){

		}
		return date;
	}

	/**
	 * @return 比较结果 0=相等 ，-1:d1比d2时间早 ，1: d1比 d2时间后
	 */
	public static int compare_date(String date1, String date2) {
		return compare_date(date1, date2,defaultDateFormat);
	}
	
	/**
	 * @return 比较结果 0=相等 ，-1:d1比d2时间早 ，1: d1比 d2时间后
	 */
	public static int compare_date(String date1, String date2,String dateFormat) {

		DateFormat df = new SimpleDateFormat(dateFormat);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				// System.out.println("d1 比 d2 大");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				// System.out.println("d1 比 d2 小");
				return -1;
			} else {
				// System.out.println("时间相等");
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 将时间转换成long型作比较
	 */
	public static long date2Long(String inVal) { // 此方法计算时间毫秒
		Date date = null; // 定义时间类型
		SimpleDateFormat inputFormat = new SimpleDateFormat(defaultDateFormat);
		try {
			date = inputFormat.parse(inVal); // 将字符型转换成日期型
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date.getTime(); // 返回毫秒数
	}

	/**
	 * 日期转换为"2014-5-10 10:15"
	 **/
	public static String dateToYYYYMMddHHMM(String date) {
		DateFormat df = new SimpleDateFormat(defaultDateFormat);
		try {
			return new SimpleDateFormat(DateFormat_list_year_time).format(df.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}
	/**
	 * 日期转换成 "2014-5-10" 的格式
	 */
	public static String dateToYYYYMMdd(String date) {
		DateFormat df = new SimpleDateFormat(defaultDateFormat);
		try {
			return new SimpleDateFormat(DataFormat_list_year).format(df.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}

	private static String[] WEEK={
		"星期日",
		"星期一",
		"星期二",
		"星期三",
		"星期四",
		"星期五",
		"星期六"
	};

	private static String[] WEEK_DAY={
			"周日",
			"周一",
			"周二",
			"周三",
			"周四",
			"周五",
			"周六"
	};
	/** * 获取指定日期是星期几
	 * 参数为null时表示获取当前日期是星期几
	 */
	public static String dateToEEEE(String date) {
		Calendar calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(DataFormat_list_year);
		try {
			calendar.setTime(df.parse(date));
			//return new SimpleDateFormat(DataFormat_list_week).format(df.parse(date));
			int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
			if(dayIndex <1 || dayIndex > 8)
				return "";
			return WEEK[dayIndex-1];
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}

	/** * 获取指定日期是星期几
	 * 参数为null时表示获取当前日期是星期几
	 */
	public static String dateToWeekDay(String date) {
		Calendar calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(DataFormat_list_year);
		try {
			calendar.setTime(df.parse(date));
			int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
			if(dayIndex <1 || dayIndex > 8)
				return "";
			return WEEK_DAY[dayIndex-1];
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}

	/**
	 * 日期转换成 "5-10" 的格式
	 */
	public static String dateToMMdd(String date) {
		DateFormat df = new SimpleDateFormat(defaultDateFormat);
		try {
			return new SimpleDateFormat(DataFormat_list_day).format(df.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}

	/**
	 * 日期转换成 "14点12分" 的格式
	 * 
	 */
	public static String dateToHHmm(String date) {
		DateFormat df = new SimpleDateFormat(defaultDateFormat);
		try {
			return new SimpleDateFormat(DataFormat_list_time).format(df
					.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}

	/**
	 * 日期转换成 "2月" 的格式
	 *
	 */
	public static String dateToMM(String date) {
		String dateFormat = "M月";
		DateFormat df = new SimpleDateFormat(defaultDateFormat);
		try {
			return new SimpleDateFormat(dateFormat).format(df.parse(date));
		} catch (Exception e) {
			e.printStackTrace();
			return date;
		}
	}

	/**
	 * 日期转换成 "2016年02月" 的格式
	 *
	 */
	public static String dateToYYYYMM(String date) {
		String dateFormat = "yyyy年MM月";
		DateFormat df = new SimpleDateFormat(defaultDateFormat);
		try {
			return new SimpleDateFormat(dateFormat).format(df.parse(date));
		} catch (Exception e) {
			e.printStackTrace();
			return date;
		}
	}

	/**
	 * 例： 传入 2014-06-27 今年： 06-27 ，不是今年 : 2014-06-27
	 * 2014年6月27日 15:07
	 * 
	 */
	public static String parseNoticeDate(String createTime,String originalSDF) {

		String df_result_today = "HH:mm";
		String df_result_thisYear = "MM月dd日 HH:mm";
		String df_result_otherYear = "yyyy-MM-dd";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(originalSDF);
			Date createDate = sdf.parse(createTime);
			
			String today = getTime("yyyy-MM-dd");
			String createDay = new SimpleDateFormat("yyyy-MM-dd").format(createDate); 
			
			if(today.equals(createDay)){
				//ret = "今天"
				return new SimpleDateFormat(df_result_today).format(createDate);
			}
			
			String createYear = new SimpleDateFormat("yyyy").format(createDate);
			String nowYear = new SimpleDateFormat("yyyy").format(new Date());
			if (createYear.equals(nowYear)) {
				// ret= "本年";
				return new SimpleDateFormat(df_result_thisYear).format(createDate);
			} else {
				// ret= "不是本年";
				return new SimpleDateFormat(df_result_otherYear).format(createDate);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 例： 传入 2014-06-27 今年： 06-27 ，不是今年 : 2014-06-27
	 * 2014年6月27日 15:07
	 * 
	 */
	public static String parseDate(String createTime,String originalSDF) {

		String df_result_lastYesterday = "MM-dd";
		String df_result_lastYear = "yyyy-MM-dd";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(originalSDF);

			Date createDate = sdf.parse(createTime);
			String createYear = new SimpleDateFormat("yyyy").format(createDate);
			String nowYear = new SimpleDateFormat("yyyy").format(new Date());
			if (createYear.equals(nowYear)) {
				// ret= "本年";
				return new SimpleDateFormat(df_result_lastYesterday).format(createDate);
			} else {
				// ret= "不是本年";
				return new SimpleDateFormat(df_result_lastYear).format(createDate);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 例： 传入 2014-06-27 今年： 06-27 ，不是今年 : 2014-06-27
	 * 2014年6月27日 15:07
	 */
	public static String parseOfficeDate(String createTime,String originalSDF) {

        try {
            String df_result_lastYesterday = "MM月dd日";
            String df_result_lastYear = "yyyy年MM月dd日";
			SimpleDateFormat sdf = new SimpleDateFormat(originalSDF);

			Date createDate = sdf.parse(createTime);
			String createYear = new SimpleDateFormat("yyyy").format(createDate);
			String nowYear = new SimpleDateFormat("yyyy").format(new Date());
			if (createYear.equals(nowYear)) {
				// ret= "本年";
				return new SimpleDateFormat(df_result_lastYesterday).format(createDate);
			} else {
				// ret= "不是本年";
				return new SimpleDateFormat(df_result_lastYear).format(createDate);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 例： 传入 2014-06-27 15:07:30 今年： 06-27 15:07 上年：2014-06-27 15:07 
	 */
	public static String parseOfficeSendtime(String createTime) {

		String df_result_thisYear = "MM-dd HH:mm";
		String df_result_lastYear = "yyyy-MM-dd HH:mm";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(defaultDateFormat);
			Date createDate = sdf.parse(createTime);
			
			String createYear = new SimpleDateFormat("yyyy").format(createDate);
			String nowYear = new SimpleDateFormat("yyyy").format(new Date());
			if (createYear.equals(nowYear)) {
				// ret= "本年";
				return new SimpleDateFormat(df_result_thisYear).format(createDate);
			} else {
				// ret= "不是本年";
				return new SimpleDateFormat(df_result_lastYear).format(createDate);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 例： 传入 2014-06-27 15:07 当天： 15:07 昨天： 昨天 15:07 昨天以前： 6月27日 15:07 一年以前：
	 * 2014年6月27日 15:07
	 * 
	 */
	public static String parseDate(String createTime) {

		String df_result_today = "HH:mm";
		String df_result_yesterday = "昨天 HH:mm";
		String df_result_lastYesterday = "MM月dd日 HH:mm";
		String df_result_lastYear = "yyyy年MM月dd日 HH:mm";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(defaultDateFormat);

			Date createDate = sdf.parse(createTime);

			long create = createDate.getTime();
			Calendar now = Calendar.getInstance();
			long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
					+ now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 毫秒数
			long ms_now = now.getTimeInMillis();
			if (ms_now - create < ms) {
				// ret = "今天";
				return new SimpleDateFormat(df_result_today).format(createDate);
			} else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
				// ret = "昨天";
				return new SimpleDateFormat(df_result_yesterday)
						.format(createDate);
			} else {
				String createYear = new SimpleDateFormat("yyyy")
						.format(createDate);
				String nowYear = new SimpleDateFormat("yyyy")
						.format(new Date());
				if (createYear.equals(nowYear)) {
					// ret= "本年";
					return new SimpleDateFormat(df_result_lastYesterday)
							.format(createDate);
				} else {
					// ret= "不是本年";
					return new SimpleDateFormat(df_result_lastYear)
							.format(createDate);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 例： 传入 2014-06-27 15:07 当天： 15:07 昨天： 昨天 15:07 昨天以前： 6月27日 15:07 一年以前：
	 * 2014年6月27日 15:07
	 * 
	 */
	public static String parseRecentlyDate(String createTime) {

		String df_result_today = "HH:mm";
		String df_result_other = "MM月dd日";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(defaultDateFormat);

			Date createDate = sdf.parse(createTime);

			long create = createDate.getTime();
			Calendar now = Calendar.getInstance();
			long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
					+ now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 毫秒数
			long ms_now = now.getTimeInMillis();
			if (ms_now - create < ms) {
				// ret = "今天";
				return new SimpleDateFormat(df_result_today).format(createDate);
			} else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
				// ret = "昨天";
				return "昨天";
			} else {
					return new SimpleDateFormat(df_result_other).format(createDate);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取从date开始的N天
	 **/
	public static List<String> getNextNDate(String date,int N){
		List<String> result = new ArrayList<>();
		long time_day = 24 * 3600 * 1000;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(defaultDateFormat);
			Date createDate = sdf.parse(date + " 00:00:00");
			long create = createDate.getTime();

			for(int i = 0;i<N;i++){
				String day_after = CommonUtil.transformLongToDate(CommonUtil.DataFormat_list_year, create+i*time_day);
				result.add(day_after);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获得当前时间的最近N天
	 * 如今天是3月14号  只能返回12 13 14号
	 **/
	public static List<String> getRecentlyNDate(String date,int N){
		List<String> result = new ArrayList<String>();
		long time_day = 24 * 3600 * 1000;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(defaultDateFormat);
			Date createDate = sdf.parse(date);
			long create = createDate.getTime();

			for(int i = 0;i<N;i++){
				String day_after = CommonUtil.transformLongToDate(CommonUtil.DataFormat_list_year, create - i*time_day);
				result.add(day_after);
			}

		} catch (ParseException e) {
			e.printStackTrace();
			return result;
		}

		Collections.reverse(result);
		return result;
	}

	public static Calendar get1980() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse("1980-01-01");
			c.setTime(date);
			// System.out.println(date.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return c;
	}

    public static String dateToFormat(String inFormat,String outFormat,String dateStr){
        Calendar calendar = dateToCalendar(dateStr,inFormat);
        return calendarToDate(calendar,outFormat);
    }

	public static Calendar dateStrToCalendar(String dateStr) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(dateStr);
			c.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
			c.setTime(new Date());
		}
		return c;
	}
	
	public static Calendar dateToCalendar(String dateStr) {
		return dateToCalendar(dateStr,"yyyy-MM-dd");
	}
	
	public static Calendar dateToCalendar(String dateStr,String format) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = sdf.parse(dateStr);
			c.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
			c.setTime(new Date());
		}
		return c;
	}
	
	public static String calendarToDate(Calendar calendar){
		return calendarToDate(calendar, "yyyy-MM-dd");
	}
	
	public static String calendarToDate(Calendar calendar,String format){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(calendar.getTime());
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

	}
	
	public static String calendarToDateTime(Calendar calendar){
		return calendarToDate(calendar, "yyyy-MM-dd HH:mm");
	}
	
    /**
     * 把毫秒转化成日期
     * @param dateFormat(日期格式，例如：MM/ dd/yyyy HH:mm:ss)
     * @param millSec(毫秒数)
     */
    public static String transformLongToDate(String dateFormat,Long millSec){
     SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
     Date date= new Date(millSec);
            return sdf.format(date);
    }
	
    /**
	 * 日期转换成 任意 的格式
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToFormat(String dateFormat,String date) {
		DateFormat df = new SimpleDateFormat(defaultDateFormat);
		try {
			return new SimpleDateFormat(dateFormat).format(df.parse(date));
		} catch (Exception e) {
			e.printStackTrace();
			return date;
		}
	}
	
	public static String dateToFormat(String dateFormat, Date date){
		String result = new SimpleDateFormat(dateFormat).format(date);
		return result;
	}

    /**
     * 获取当月第一天日期
     *
     * @param
     * @param
     * @return px
     */
    public static String dateGetMonthFirst(String dateStr){

        try {
            if(TextUtils.isEmpty(dateStr))return "";
            //把yyyy-MM-dd HH:mm:ss  -> yyyy-MM-dd
            Date date = new SimpleDateFormat(defaultDateFormat).parse(dateStr);
            String dayStr = new SimpleDateFormat("yyyy-MM").format(date);

            //把yyyy-MM -> yyyy-MM-dd
            date = new SimpleDateFormat("yyyy-MM").parse(dayStr);
            return new SimpleDateFormat(defaultDateFormat).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateStr;
        }
    }

	/**
	 * 将dp转换成不同分辨率下的px 适配各种机型
	 * 
	 * @param context
	 * @param dpValue
	 * @return px
	 */
	public static int dip2px(Context context, double dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取该手机屏幕宽高
	 * 
	 * @return
	 */
	public static DisplayMetrics getWidHei(Activity context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics;
	}

	/**
	 * 把整形数组字符串组装成字符串，带分隔符
	 * 
	 * @param array
	 * @return
	 */
	public static String transformArrayString(int[] array) {
		String[] sarray = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			sarray[i] = String.valueOf(array[i]);
		}
		return transformArrayString(sarray, ",");
	}

	/**
	 * 把数组字符串组装成字符串，带分隔符
	 * 
	 * @param array
	 * @return
	 */
	public static String transformArrayString(String[] array) {
		return transformArrayString(array, ",");
	}

	public static String transformArrayString(String[] array, String symbol) {
		return transformArrayString(array, 0, array.length - 1, symbol);
	}

	public static String transformArrayString(String[] array, int start, int end) {
		return transformArrayString(array, start, end, ",");
	}

	public static String transformArrayString(String[] array, int start,
			int end, String symbol) {
		String result = "";

		if (array.length == 0)
			return result;
		if (start > array.length || end < 0)
			return result;

		if (start < 0)
			start = 0;
		if (end > array.length - 1)
			end = array.length - 1;

		for (int i = 0; i < (end - start) + 1; i++) {
			result = result + array[i + start];

			if (i < end - start) {
				result = result + symbol;
			}

		}
		return result;
	}

	public static int getVersionCode(Context context)// 获取版本号(内部识别号)
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static String getVersion(Context context)// 获取版本号
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取应用名称
	 * @param context
	 * @return
	 */
	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * 是否有网络
	 *
	 */
	public static boolean isConnectInternet(Context context) {
		if (context == null) {
			context = AppContext.getContext();
		}
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	public static boolean isWifiAvailable(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi  = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if(wifi == State.CONNECTED || wifi == State.CONNECTING)
			return true;
		return false;
	}

	public static String ToSBC(String input) {
		// 半角转全角：
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}

	/**
	 * 全角转换为半角
	 *
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 去除特殊字符或将所有中文标号替换为英文标号
	 *
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]")
				.replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
     * 网络是否可用
     *
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == State.CONNECTED) {
                        return true;  
                    }  
                }  
            }  
        }  
        return false;  
    }  
    
    public static String[] deleteInStringArray(int index,String[] src){
    	if(src.length == 0 || src.length-1==0){
    		return new String[0];
    	}
		String[] result = new String[src.length-1];
		System.arraycopy(src, 0, result, 0, index);
		System.arraycopy(src, index+1, result, index, result.length-index);
		return result;
    }
    
    public static String deleteInStringArray(int index,String source,String spile){
    	
    	String[] src = source.split(spile);
    	if(source==null || source.equals("") || src.length == 0 || src.length -1==0){
    		return "";
    	}
		String[] target = deleteInStringArray(index, src);
		String result = "";
		for(int i=0;i<target.length;i++){
			result = result + target[i];
			if(i!=target.length-1){
				result = result +",";
			}
		}
		return result;
    }
    
    public static String get21DaysAfter(){
    	long t_now = System.currentTimeMillis();
		long dayTimes = 1000*60*60*24*21;
		long t_21DaysAfter = t_now+dayTimes;//同一时间21日后的时间
		String ts_21DaysAfter = CommonUtil.transformLongToDate(CommonUtil.defaultDateFormat, t_21DaysAfter);
		return ts_21DaysAfter;
    }
    
//    public static String getTomorrow(){
//    	long t_now = System.currentTimeMillis();
//		long dayTimes = 1000*60*60*24;
//		long t_21DaysAfter = t_now+dayTimes;//同一时间21日后的时间
//		String ts_tomorrow = CommonUtil.transformLongToDate(CommonUtil.defaultDateFormat, t_21DaysAfter);
//		return ts_tomorrow;
//    }
    
    public static String getDateAfter(int count){
    	long t_now = System.currentTimeMillis();
		long dayTimes = 1000*60*60*24 * count;
		long t_after = t_now+dayTimes;//同一时间21日后的时间
		String ts_after = CommonUtil.transformLongToDate(CommonUtil.defaultDateFormat, t_after);
		return ts_after;
    }
    
    public static String replaceSymble(String source){
    	try {
			return URLEncoder.encode(source.replace(".", "").replace("/", "").trim(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return source.replace(".", "").replace("/", "");
		}
    }
    
    public static String loadJsonFile(Context context,String fileName){
    	 String json="";
    	 AssetManager am = context.getAssets();
    	 InputStreamReader reader = null;
    	 BufferedReader bufReader = null;
    	 InputStream is = null;
         try {
             is = am.open(fileName);
             reader = new InputStreamReader(is);
             bufReader = new BufferedReader(reader);
             String line="";
             while((line = bufReader.readLine()) != null){
                 json += line;
             }
         } catch (Exception e) {
             e.printStackTrace();
         } finally{
				try {
					if(bufReader!=null)
					if(is!=null)is.close();
					if(reader!=null)reader.close();
					bufReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
         }
         return json;
    }

	/**
	 * 当月第一天
	 * @return
	 */
	public static String getFirstDay(String source) {
		try{
			SimpleDateFormat df = new SimpleDateFormat(defaultDateFormat);
			Calendar calendar = new GregorianCalendar();
			Date theDate = df.parse(source);
			calendar.setTime(theDate);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return df.format(calendar.getTime());
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 当月最后一天
	 * @return
	 */
	public static String getLastDay(String source) {
		try{
			SimpleDateFormat df = new SimpleDateFormat(defaultDateFormat);
			Calendar calendar = new GregorianCalendar();
			Date theDate = df.parse(source);
			calendar.setTime(theDate);
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			return df.format(calendar.getTime());
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}


	}

	/**
	 * 转成年月
	 * @return
	 */
	public static String getMonth(String source) {
		try{
			SimpleDateFormat df = new SimpleDateFormat(defaultDateFormat);
			Calendar calendar = new GregorianCalendar();
			Date theDate = df.parse(source);
			calendar.setTime(theDate);
			df = new SimpleDateFormat(DataFormat_list_month);
			return df.format(calendar.getTime());
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

    /**
     * 将元->万元，保留两位小数，并去掉小数点末位的0
     * @param source
     * @return
     */
    public static String tranformMoney(String source){
		if(source == null || source.equals(""))return "";
		Double d = Double.valueOf(source);
		DecimalFormat df = new DecimalFormat("#0.00"); 
		String value = df.format(d/10000);
		if(value.endsWith("0")){
			df = new DecimalFormat("#0.0");
			value = df.format(d/10000);
			if(value.endsWith("0")){
				df = new DecimalFormat("#0");
				value = df.format(d/10000);
			}
		}
//		System.out.println(value);
		return value;
	}

    /**
     * 整形数组里面增加
     * @param source
     * @param member
     * @return
     */
    public static int[] intArrayAdd(int[] source,int member){
	
		int[] result = new int[source.length + 1];
		for (int i = 0; i < source.length; i++) {
			result[i] = source[i];
			if(source[i] == member){
				return source;
			}
		}
		result[source.length] = member;
		return result;
	}

	/**
	 * 整形数组里面减对象
	 * @param source
	 * @param member
	 * @return
	 */
	public static int[] intArrayReduce(int[] source, int member) {
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<source.length;i++){
			list.add(source[i]);
		}
		
		if(!list.contains(member)){
			return source;
		}else{
			
			list.remove(list.indexOf(member));
			int[] result = new int[list.size()];
			for(int i=0;i<list.size();i++){
				result[i] = list.get(i);
			}
			return result;
		}
	}

	public static String getDeviceUUID(Context context){
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    String uniqueId = deviceUuid.toString();
	    return uniqueId;
	}
	
	public static void printBigJson(String json){
		if(json==null)return;
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(json.getBytes(Charset.forName("utf8"))),
				Charset.forName("utf8")));
//		int line;
		char[] buffer = new char[256];
		try {
			while (br.read(buffer) != -1) {
				System.out.println(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param  sex
	 * @return true(男)
	 *          false(女)
	 **/
	public static boolean isMale(String sex){

		return  ("男".equals(sex))?true:false;

	}


    public static String getManifestMetal(Context context,String key){
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (NameNotFoundException e) {

            e.printStackTrace();
            return "";
        }
    }

}
