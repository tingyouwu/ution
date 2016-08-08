package com.wty.ution.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.wty.ution.base.AppContext;
import com.wty.ution.data.dalex.IncUpdateDALEx;
import com.wty.ution.data.dalex.UserDALEx;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginTask extends BaseBackgroundTask{
	
	public static final int Type_Login = 10101;

	Context context;
	int taskType;
	boolean isCancel = false;

	CountDownLatch latch;
	
	private String eAccount;
	private String password;
	public LoginTask(Context context, int taskType) {
	    super(context);
		this.context=context;
		this.taskType = taskType;
		
		latch = new CountDownLatch(2);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(onLoginListener!=null){
			onLoginListener.onLoginStart();
		}
	}
	
	@Override
	protected String doInBackground(Object... params) {
		String result = null;
		
		switch (taskType) {
			case Type_Login:
				eAccount = (String) params[0];
				password = (String) params[1];

				BmobUser bu = new BmobUser();
				bu.setUsername(eAccount);
				bu.setPassword(password);
				bu.login(context, new SaveListener() {
					@Override
					public void onSuccess() {
						if(onLoginListener!=null){
							onLoginListener.onValidateSuccess();
						}
						//登录成功后加载数据
						validateSuccessTask.execute();
					}

					@Override
					public void onFailure(int i, String reason) {
						if(reason == null){
							reason = "当前网络不可用，请检查你的网络设置";
						}
						if(onLoginListener!=null){
							onLoginListener.onValidateFailed(reason);
						}
					}
				});

				break;
			default:
				break;
		}
		return result;
	}

    @Override
	protected void onCancelled(String result) {
		super.onCancelled(result);
		isCancel = true;
	}

	public void setLoginSuccessListener(OnLoginListener onLoginListener) {
		this.onLoginListener = onLoginListener;
	}

	//获取基础数据
	BasicDataInfoTask task_basicData = new BasicDataInfoTask(context){

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(isSuccess()){
				if(onLoginListener!=null){
					onLoginListener.onBasicDataSuccess();
				}
				System.out.println("latch --------- task_pkgBasicData success!");
			}
			latch.countDown();
			System.out.println("latch --------- task_pkgBasicData countDown!");
		}

		protected void onCancelled() {
			super.onCancelled();
			latch.countDown();
			isCancel = true;
		}

		protected void onCancelled(String result) {
			super.onCancelled(result);
			latch.countDown();
			isCancel = true;
		}
	};

//	//更新物流信息列表
//	LogisticsListTask task_logistics = new LogisticsListTask(context){
//		@Override
//		protected void onPostExecute(String result) {
//			if(isSuccess()){
//				if(onLoginListener!=null){
//					onLoginListener.onLogisticsDataSuccess();
//				}
//				System.out.println("latch --------- task_region success!");
//			}
//			latch.countDown();
//			System.out.println("latch --------- task_region countDown!");
//		}
//
//		protected void onCancelled() {
//			super.onCancelled();
//			latch.countDown();
//			isCancel = true;
//		}
//
//		protected void onCancelled(String result) {
//			super.onCancelled(result);
//			latch.countDown();
//			isCancel = true;
//		}
//	};

//	//更新眼镜信息列表
//	GlassesListTask task_glass = new GlassesListTask(context){
//		@Override
//		protected void onPostExecute(String result) {
//			if(isSuccess()){
//				if(onLoginListener!=null){
//					onLoginListener.onGlassesDataSuccess();
//				}
//				System.out.println("latch --------- task_logistics success!");
//			}
//			latch.countDown();
//			System.out.println("latch --------- task_logistics countDown!");
//		}
//
//		protected void onCancelled() {
//			super.onCancelled();
//			latch.countDown();
//			isCancel = true;
//		}
//
//		protected void onCancelled(String result) {
//			super.onCancelled(result);
//			latch.countDown();
//			isCancel = true;
//		}
//	};

	//拷贝行政区域
	RegionInfoTask task_region = new RegionInfoTask(context){
		
		@Override
		protected void onPostExecute(String result) {
			if(isSuccess()){
				if(onLoginListener!=null){
					onLoginListener.onRegionDataSuccess();
				}
				System.out.println("latch --------- task_region success!");
			}
			latch.countDown();
			System.out.println("latch --------- task_region countDown!");
		}
		
		protected void onCancelled() {
			super.onCancelled();
			latch.countDown();
			isCancel = true;
		}
		
		protected void onCancelled(String result) {
			super.onCancelled(result);
			latch.countDown();
			isCancel = true;
		}
	};
	


	public void cancelLogin(){

		//其他线程正在执行
		cancel(true);
		validateSuccessTask.cancel(true);

		task_region.cancel(true);
		//task_glass.cancel(true);
		//task_logistics.cancel(true);
		task_basicData.cancel(true);

		AppContext.getInstance().writePreferences(AppContext.Preference.IsAutoLogin, false);
		AppContext.getInstance().writePreferences(AppContext.Preference.LastSession, "");
		
		Activity activity = (Activity)context;
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(onLoginListener!=null){
					onLoginListener.onLoginCancel();
				}
			}
		});
	}
	
	public boolean isLoginRunning(){
		if( (this.getStatus()==AsyncTask.Status.RUNNING && !this.isCancelled()) 
				|| (task_region.getStatus()==Status.RUNNING && !task_region.isCancelled())
//				||(task_logistics.getStatus() == Status.RUNNING && !task_logistics.isCancelled())
//				||(task_glass.getStatus() == Status.RUNNING && !task_glass.isCancelled())
				){
			return true;
		}
		return false;
			
	}

	/**
	 * 功能描述：验证成功之后开始做数据更新操作
	 **/
	AsyncTask<String, Integer, Boolean> validateSuccessTask =  new AsyncTask<String, Integer, Boolean>() {

		protected void onPreExecute() {
			IncUpdateDALEx increment_glass = IncUpdateDALEx.get().queryByName(IncUpdateDALEx.Update_Glass);
			String glass_updatetime = (increment_glass==null?"":increment_glass.getUpdatetime());
			
			IncUpdateDALEx increment_logistics = IncUpdateDALEx.get().queryByName(IncUpdateDALEx.Update_Logistics);
			String logistics_updatetime = (increment_logistics==null?"":increment_logistics.getUpdatetime());

			task_basicData.startTask(context);
			//task_glass.startTask(context, new Object[]{glass_updatetime});
			//task_logistics.startTask(context, new Object[]{logistics_updatetime});
			task_region.startTask(context);
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			
			try {
				System.out.println(" latch ---------- await 等待所有线程执行完成后才进入主界面");
				latch.await();
				System.out.println(" latch ---------- await finish(), 开始修改界面 ");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onCancelled(Boolean result) {
			isCancel = true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			
			if(isCancel){
				if(onLoginListener!=null){
					onLoginListener.onLoginCancel();
				}
			}else if(task_region.isSuccess()
					&& task_basicData.isSuccess()
//					&& task_logistics.isSuccess()
//					&& task_glass.isSuccess()
					){
				
				try {
					UserDALEx user = UserDALEx.get().getUserByUserName(eAccount);

					if(user==null){
						user = new UserDALEx();
						user.setUsernumber(UUID.randomUUID().toString());
						user.setUsername(eAccount);
						user.setPassword(password);
					}
					user.setLoginSuccess(1);
					user.saveOrUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(onLoginListener!=null){
					onLoginListener.onLoginSuccess();
				}
			}else{
				AppContext.getInstance().writePreferences(AppContext.Preference.IsAutoLogin, false);

				List<String> reasons = new ArrayList<String>();
				if(!task_region.isSuccess())reasons.add(task_region.getReason());
				if(!task_basicData.isSuccess())reasons.add(task_basicData.getReason());
//				if(!task_logistics.isSuccess())reasons.add(task_logistics.getReason());
//				if(!task_glass.isSuccess())reasons.add(task_glass.getReason());
				if(onLoginListener!=null){
					onLoginListener.onLoginFailed(reasons.get(0));
				}
			}
		}
	};

	private OnLoginListener onLoginListener;
	public interface OnLoginListener{
		void onLoginStart();
		void onLoginSuccess();
		void onLoginFailed(String reason);
		void onLoginCancel();
		void onValidateFailed(String reason);

		void onValidateSuccess();
		void onRegionDataSuccess();
		void onBasicDataSuccess();
		void onGlassesDataSuccess();
		void onLogisticsDataSuccess();
	}

}
