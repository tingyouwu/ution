package com.wty.ution.ui.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.wty.ution.R;
import com.wty.ution.base.AppContext;
import com.wty.ution.base.UtionObjectCache;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.OnActivityResultListener;
import com.wty.ution.widget.navigation.NavigationText;
import com.wty.ution.widget.sweet.OnDismissCallbackListener;

import java.util.ArrayList;
import java.util.List;

import SweetAlert.SweetAlertDialog;

/**
 * 基础activity，所有activity都要继承它
 * @author wty
 */
public class BaseActivity extends AppCompatActivity {

    public SweetAlertDialog dialog;
    public SweetAlertDialog toastDialog;
	public boolean prohibitBreak = false;

    private NavigationText navigation;

	@Override
	protected void onCreate(Bundle saveInstance) {
		try {
			super.onCreate(saveInstance);
			if (saveInstance != null) {
				String clz = this.getClass().getSimpleName();
				Log.w(clz, clz+" 页面重新启动 ");
				onActivityRecreate(saveInstance);
			}
			
			if(UtionObjectCache.getInstance().getActivityInTree(this.getClass().getName())!=null){
				activityHasInStackTree();
			}
			UtionObjectCache.getInstance().putActivityTree(this);
			
			if (android.os.Build.VERSION.SDK_INT < 11)
				requestWindowFeature(Window.FEATURE_NO_TITLE);
			setTheme(R.style.UtionTheme);
			AppContext.setContext(this);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			setNavigation(getDefaultNavigation());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public NavigationText getDefaultNavigation(){
        if(navigation==null){
            navigation = new NavigationText(this);
        }
        return navigation;
    }

	/**
	 * 动态设置actionbar
	 */
	public void setNavigation(View navigation){
		ActionBar actionBar = getSupportActionBar();
		ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setCustomView(navigation,layoutParams);
		if(navigation instanceof  NavigationText)
			this.navigation = (NavigationText)navigation;
		actionBar.show();
	}
	
	protected void activityHasInStackTree(){}

    public void onToast(String msg){
        onToast(new OnDismissCallbackListener(msg, SweetAlertDialog.ERROR_TYPE));
    }

    public void onToast(OnDismissCallbackListener callback){
        try{
            if(dialog!=null && dialog.isShowing()){
                dialog.cancel();
            }
            if(toastDialog==null || !toastDialog.isShowing()){
                toastDialog = new SweetAlertDialog(this, callback.alertType);
                toastDialog.show();
            }
            toastDialog.setTitleText(callback.msg)
                    .setConfirmText("确定")
                    .setConfirmClickListener(callback)
                    .changeAlertType(callback.alertType);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void onToastSuccess(final String msg){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                onToast(new OnDismissCallbackListener(msg,SweetAlertDialog.SUCCESS_TYPE));
            }
        });
    }

	public void onToastErrorMsg(final String msg){
    	runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				onToast(new OnDismissCallbackListener(msg,SweetAlertDialog.ERROR_TYPE));
			}
		});
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(R.drawable.infonew);

		AppContext.setContext(this);
	}

	public View getLayoutView(int layout) {
		return LayoutInflater.from(this).inflate(layout, null);
	}




	@Override
	protected void onDestroy() {
		if (toastDialog != null) {
            toastDialog.dismiss();
		}
		if (dialog != null) {
			dialog.dismiss();
		}
		UtionObjectCache.getInstance().removeActivityInTree(this.getClass().getName());
		super.onDestroy();
	}

	/************************************************ 系统信息 ***************************************************************/
	public AlertDialog alert = null;
	public boolean alertIsShow = false;
	/**
	 * 即时按下返回键是的次数当进度时
	 */
	private int countDestroyDialogKeycodeBack = 0;
    private int i = -1;

	/**
	 * 提示进度条 必须在handler或者控件触发 调用才有效
	 * 
	 */
	public void loading(String msg) {
		try {

            if (this.isFinishing()) {
				return;
			}
			if (dialog == null || !dialog.isShowing()) {
				countDestroyDialogKeycodeBack = 0;
                dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText(msg);

                dialog.show();

                dialog.setCancelable(false);
                dialog.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialogs, int keyCode, KeyEvent event) {
						if (!prohibitBreak && keyCode == KeyEvent.KEYCODE_BACK) {
							countDestroyDialogKeycodeBack++;
							if (countDestroyDialogKeycodeBack == 6)// 3次取消
							{
								dialog.dismiss();
							}
						}
						return false;
					}
				});
                new CountDownTimer(800 * 7, 800) {
                    public void onTick(long millisUntilFinished) {
                        // you can change the progress bar color by ProgressHelper every 800 millis
                        i++;
                        switch (i){
                            case 0:
                                dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                                break;
                            case 1:
                                dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                                break;
                            case 2:
                                dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                            case 3:
                                dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                                break;
                            case 4:
                                dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                                break;
                            case 5:
                                dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                                break;
                            case 6:
                                dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                        }
                    }

                    public void onFinish() {
                        i = -1;

                    }
                }.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dismissLoading(){
		try {

			if (dialog != null && dialog.isShowing()) {
                CountDownTimer timer = new CountDownTimer(500,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                    }
                };
                timer.start();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

    public void dismissLoading(final OnDismissCallbackListener callback){
        if(dialog!=null && dialog.isShowing()){
            new CountDownTimer(500,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    dialog.setTitleText(callback.msg)
                            .setConfirmText("确定")
                            .setConfirmClickListener(callback)
                            .changeAlertType(callback.alertType);
                }
            }.start();


        }
    }



	public void finish() {
		try {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.finish();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		for(OnActivityResultListener listener : onActivityResultListener){
			if(requestCode == listener.requestCode ){
				listener.onActivityResult(requestCode, resultCode, data);
			}
		}
		
	}
	List<OnActivityResultListener> onActivityResultListener = new ArrayList<OnActivityResultListener>();
	
	public void registerOnActivityResultListener(OnActivityResultListener listener){
		onActivityResultListener.add(listener);
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
        switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

            if(onBackClickListener!=null){
                return onBackClickListener.onClick();
            }
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

    public void setOnBackClickListener(OnBackClickListener onBackClickListener){
        this.onBackClickListener = onBackClickListener;
    }
    OnBackClickListener onBackClickListener;

    public interface OnBackClickListener{
        boolean onClick();
    }

	public void keyboardControl(boolean show,View view){
	    InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
	    if(show){
	        //显示键盘
	        imm.showSoftInput(view, 0);
	    }else{
	        //隐藏键盘
	        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	    }
	}
	
	protected  List<String> validate(){
        return new ArrayList<String>();
	}
	
	protected boolean submit(){
		List<String> validate = validate();
    	if(validate.size()!=0){
    		onToast(validate.get(0));
    		return false;
    	}else if(!CommonUtil.isNetworkAvailable(this)){
			onToastErrorMsg(getString(R.string.network_failed));
			return false;
		}else{
			return true;
		}
	}
	
	protected boolean checkTask(AsyncTask<?,?,?> task,boolean interrupt){
		if(task!=null && task.getStatus()==Status.RUNNING){
			if(interrupt){
				task.cancel(true);
				return true;
			}else{
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		onActivityRecreate(savedInstanceState);
	}
	
	/**
	 * activity因系统内存回收时引起页面重启回调
	 */
	protected void onActivityRecreate(Bundle saveInstance){}
}
