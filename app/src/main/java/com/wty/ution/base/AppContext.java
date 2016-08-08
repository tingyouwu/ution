package com.wty.ution.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.io.Serializable;
import java.lang.ref.WeakReference;

public class AppContext implements Serializable{

	public static final String Bmob_ApplicationId = "2a8f321c77413e52837e17b111974c2a";
	public static final String Bmob_RESTApiKey = "34b2e3d083abb66bfe0674960dd32bbd";
	public static final String Bmob_SecretKey = "b566e3a32fecc9a5";
	public static final String Bmob_MasterKey = "ee5f038a0018ea700ea29ea57f1a9e77";

	public static final String ApiSecretKey = "MCRM@AUTH";
	public static final String FileSecretKey = "6130crmxtionnet2222";
	public static final String APPID = "SYS00011";
	public static String PREFERENCES_CRM = "Preferences_ution";
	public static final String SD_PATH = "com.wty.ution";
	public static final String PATH = android.os.Environment.getExternalStorageDirectory().getPath() + "/"+SD_PATH;
	/** 录音文件下载后保存目录全路径名'mnt/sdcard/com.wty.ution/tempRadio' */
	public static final String SAVEIMAGEPATH = PATH + "/SaveImages";
	public static final String IMAGEPATH = PATH + "/Images";
	public static final String VOICEPATH = PATH  + "/voice";
	public static final String UPDATEPATH = PATH  + "/update";
	public static final String NOTICEPATH = PATH  + "/notice";
	public static final String PLUGINPATH = PATH  + "/plugin";
	public static final String REPOSITORYPATH = PATH  + "/repository";
    public static final String BIZDOCPATH = PATH  + "/bizDoc";
	public static final String DOCPATH = PATH  + "/file";

	/** 系统状态表示系统是否正常运行 **/
	public static boolean systemState = true;
	private WeakReference<Context> context;
	private static AppContext instance = null;
	private String nowChaterId;

    private boolean isAppRunning ;
    private String serviceTime;
    private boolean LoginByAuto ; //是否经过登录页面进入应用

    public synchronized void setAppExit(){
        isAppRunning = false;
    }

    public synchronized void setAppRunning(){
        isAppRunning = true;
    }

    public synchronized boolean isAppRunning(){
        return isAppRunning;
    }

	public static synchronized AppContext getInstance() {
		if (instance == null) {
			instance = new AppContext();
		}
		return instance;
	}

	private SharedPreferences preferences;

	/**
	 * 获取 系统上下文
	 */
	public static Context getContext() {
		if (getInstance().context == null) {
			return null;
		}
		return getInstance().context.get();
	}

	/**
	 * 设置 系统上下文
	 */
	public static void setContext(Context contextx) {
        if(getInstance().context!=null){
            getInstance().context.clear();
        }
		getInstance().context = new WeakReference<Context>(contextx);
	}

	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}

	public SharedPreferences getPreferences(){
		if(preferences == null){
			if(getContext() != null)
				preferences = getContext().getSharedPreferences(PREFERENCES_CRM, Context.MODE_PRIVATE);
		}
		return preferences;
	}

	/**
	 *	保存到sharedPreferences
	 */
	public void writePreferences(String key,String value){
		Editor editor = getPreferences().edit();// 获取编辑器
		editor.putString(key, value);
		editor.commit();// 提交修改
	}

	public void writePreferences(String key,Boolean value){
		Editor editor = getPreferences().edit();// 获取编辑器
		editor.putBoolean(key, value);
		editor.commit();// 提交修改
	}

	public static int getDbVersion(Context context){
		ApplicationInfo appInfo;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
			return appInfo.metaData.getInt("dbversion");
		} catch (NameNotFoundException e) {

			e.printStackTrace();
			return 1;
		}
	}

	public String getLastPassword(){
        return this.getPreferences().getString(AppContext.Preference.LastPassword, null);
    }

	public String getEnterpriseNumber(){
        return this.getPreferences().getString(AppContext.Preference.EnterpriseNumber, "");
    }

	public String getLastAccount(){
	    return this.getPreferences().getString(AppContext.Preference.LastAccount, null);
	}

	public String getLastOriginalAccount(){
	    return this.getPreferences().getString(AppContext.Preference.LastOriginalAccount, null);
	}

	public String getLastSession(){
	    return this.getPreferences().getString(AppContext.Preference.LastSession, null);
	}

	public boolean isAutoLogin(){
	    return this.getPreferences().getBoolean(AppContext.Preference.IsAutoLogin, false);
	}

	public void clearSession(){
		this.writePreferences(AppContext.Preference.LastSession, "");
	}

	public String getLastAppVersion(){
		return this.getPreferences().getString(AppContext.Preference.LastAppVersion, null);
	}

	public void updateLastAppVersion(String version){
		this.writePreferences(AppContext.Preference.LastAppVersion, version);
	}

	public String getDBname() {
		return "UtionDB_"+ AppContext.getInstance().getLastAccount();
	}

	public void exitApp(){
		UtionObjectCache.getInstance().finishAllActivity();
		dealBeforeExitApp();
	}

	private void dealBeforeExitApp(){
		AppContext.getInstance().setAppExit();
		UtionObjectCache.getInstance().closeAllDB();
		UtionObjectCache.getInstance().clearCache();
	}

	enum Host{
		//测试环境
		Test("http://120.132.147.141:8084",
				"http://120.132.147.140:8082",
				"120.132.147.140",
				"1883"),
		//开发环境
		Development("http://120.132.147.141:8801",
					"http://120.132.147.140:8082",
					"120.132.147.140",
					"1883");

		String apiHost;
		String fileHost;
		String pushHost;
		String pushPort;
		Host(String apiHost,String fileHost,String pushHost,String pushPort) {
			this.apiHost = apiHost;
			this.fileHost = fileHost;
			this.pushHost = pushHost;
			this.pushPort = pushPort;
		}
	}
	
	public static class Api {
		
		//服务器地址
		public static final Host host = Host.Development;
		
		public static final String Push_Host = host.pushHost;
		public static final String Push_Port = host.pushPort;
		
		public static final String API_Host = host.apiHost;
		public static final String API_FileHost = host.fileHost;
		
		//基础路径
		public static final String API_BASE = API_Host +"/api/";

		//百度推送绑定服务
		public static final String API_BaiduPushBind = API_BASE +"baidupush/bind";

		//基础数据服务
		public static final String API_Basicdatainfo = API_BASE +"basicdata/basicdatainfo";
		public static final String API_Incrementdata = API_BASE +"basicdata/Incrementdata";
		public static final String API_Countdata 	 = API_BASE +"basicdata/countdata";
		
	}

	public static class Constant{

		public final static int ActivityResult_FieldLabel_RegionSelect = 1;
		public final static int ActivityResult_FieldLabel_MultiSelect = 2;
		public final static int ActivityResult_Camera_with_data = 3;
		public final static int ActivityResult_Camera_crop_with_data = 4;
		public final static int ActivityResult_Crop_Small_Picture = 5;
		public final static int ActivityResult_Choose_Small_Picture = 6;
		public final static int ActivityResult_Choose_Album = 7;
		public final static int ActivityResult_AlbumIndex =  8;
		public final static int ActivityResult_AlbumIndexCrop =  9;
		public final static int ActivityResult_AlbumChoice = 10;
		public final static int ActivityResult_GotoMapview= 11;

	}
	
	public static class Preference{
		public static String LastPassword = "mcrm_lastPassword";
		public static String LastAccount = "mcrm_lastAccount";
		public static String LastOriginalAccount = "mcrm_lastOriginalAccount";
	    public static String IsAutoLogin = "mcrm_IsAutoLogin";
	    public static String LastSession = "mcrm_LastSession";
	    public static String LastAppVersion = "LastAppVersion";
	    
	    public static String Baidu_User = "baidu_userid";
		public static String Baidu_Channelid = "baidu_channelid";
		
		public static String EnterpriseNumber = "EnterpriseNumber";
		
		public final static String IflytekIsvAccount = "IflytekIsvAccount";
		public final static String IflytekIsvPswId = "IflytekIsvPswId";
	}
	
	public boolean isLoginByAuto() {
		return LoginByAuto;
	}

	public void setLoginByAuto(boolean loginByAuto) {
		LoginByAuto = loginByAuto;
	}

}
