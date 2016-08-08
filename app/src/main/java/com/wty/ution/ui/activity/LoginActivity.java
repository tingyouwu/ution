package com.wty.ution.ui.activity;

import android.content.Intent;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wty.ution.R;
import com.wty.ution.base.AppContext;
import com.wty.ution.base.UtionObjectCache;
import com.wty.ution.data.dalex.UserDALEx;
import com.wty.ution.task.LoginTask;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.login.LoginInputView;
import com.wty.ution.widget.login.LoginProgressView;

import SweetAlert.SweetAlertDialog;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity{

	@Bind(R.id.login_inputview) LoginInputView inputView;
	@Bind(R.id.login_progressview) LoginProgressView progressView;
	@Bind(R.id.login_version) TextView loginVersion;

	private SweetAlertDialog loginCancelDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
		initWidget();
		initData();
        ImageLoader.getInstance().clearMemoryCache();
    }

    @Override
	protected void onDestroy() {
		super.onDestroy();
		if(loginCancelDialog!=null && loginCancelDialog.isShowing()){
			loginCancelDialog.dismiss();
		}
	}

	// 初始化数据
	private void initData() {
		loginVersion.setText("V" + CommonUtil.getVersion(this) + "." + CommonUtil.getVersionCode(this));
		//最近登录的帐号
		String lastOriginalAccount = AppContext.getInstance().getLastAccount();
		//登录密码
		String lastPsw = AppContext.getInstance().getLastPassword();
		//是否自动登录
		boolean isAutoLogin = AppContext.getInstance().isAutoLogin();

		if (isAutoLogin) {
			AppContext.getInstance().setLoginByAuto(true);
			UtionObjectCache.getInstance().getEtionDbFromUserAccunt();
			if(!checkFirstLogin(AppContext.getInstance().getLastAccount())){
				jumpToMainPage(true);
				return;
			}
		}else{
            AppContext.getInstance().writePreferences(AppContext.Preference.LastSession, "");
        }

		if (lastOriginalAccount != null) {
			inputView.setAccount(lastOriginalAccount);
			inputView.setPassword(lastPsw);

			if (!TextUtils.isEmpty(lastPsw)) {
				// 记住密码
				inputView.setIsRememberPsw(true);
			} else {
				// 不记住密码
				inputView.setIsRememberPsw(false);
			}
		} else {
			// 第一次使用，默认记住密码
			inputView.setIsRememberPsw(true);
		}
	}

	// 初始化控件
	private void initWidget() {
        loginVersion = (TextView) findViewById(R.id.login_version);
        progressView = (LoginProgressView) findViewById(R.id.login_progressview);
        inputView = (LoginInputView) findViewById(R.id.login_inputview);

        inputView.setOnLoginAction(new LoginInputView.OnLoginActionListener() {

			@Override
			public void onLogin() {
				keyboardControl(false, inputView.getAccountInput());
				String validate = inputView.validata();
				if (TextUtils.isEmpty(validate)) {
					//开始登录任务
					startLoginTask();
				} else {
					onToastErrorMsg(validate);
				}
			}
		});
	}

	/**
	 * 功能描述：跳到主页面
	 * @param isAutoLogin 是否自动登录
	 **/
	private void jumpToMainPage(boolean isAutoLogin) {
		Intent i = new Intent();
		i.setClass(LoginActivity.this, HomeActivity.class);
		i.putExtra("isAutoLogin", isAutoLogin);
		startActivity(i);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(task!=null && task.isLoginRunning()){
				//正在登陆中，点击取消登陆
				loginCancelDialog = new SweetAlertDialog(LoginActivity.this);
                loginCancelDialog.setContentText("正在获取数据，确定取消登录吗？");
                loginCancelDialog.setCancelText("取消");
                loginCancelDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                });
                loginCancelDialog.setConfirmText("确定");
                loginCancelDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        task.cancelLogin();

                    }
                });

				loginCancelDialog.show();
			}else{
				return super.onKeyDown(keyCode, event);
			}
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		return false;
	}

	LoginTask.OnLoginListener loginListener = new LoginTask.OnLoginListener() {

		@Override
		public void onLoginStart() {
			progressView.setVisibility(View.VISIBLE);
			inputView.setVisibility(View.GONE);
			progressView.setProgress(0);
			progressView.setText("帐号验证中…");
		}

		@Override
		public void onValidateSuccess() {
			System.out.println("-------------------- onValidateSuccess！ ------------------ ");
			progressView.addProgress(20);
			progressView.setText("正在努力地加载数据...");

			AppContext.getInstance().writePreferences(AppContext.Preference.LastAccount, inputView.getAccount());

			if(inputView.isRememberPsw()){
				AppContext.getInstance().writePreferences(AppContext.Preference.IsAutoLogin, true);
				AppContext.getInstance().writePreferences(AppContext.Preference.LastPassword, inputView.getPassword());
			}else{
				AppContext.getInstance().writePreferences(AppContext.Preference.IsAutoLogin, false);
				AppContext.getInstance().writePreferences(AppContext.Preference.LastPassword, "");
			}
		}

		@Override
		public void onValidateFailed(String reason) {
			progressView.setVisibility(View.GONE);
			inputView.setVisibility(View.VISIBLE);
			progressView.setProgress(0);
			if(TextUtils.isEmpty(reason)){
				onToastErrorMsg("登录失败");
			}else{
				onToastErrorMsg(reason);
			}
		}

		@Override
		public void onRegionDataSuccess() {
			//加载区域数据成功回调
			progressView.addProgress(40);
		}

		@Override
		public void onBasicDataSuccess() {
			//加载基础数据成功回调
			progressView.addProgress(40);
		}

		@Override
		public void onLogisticsDataSuccess() {
			//加载物流数据成功回调
			progressView.addProgress(20);
		}

		@Override
		public void onGlassesDataSuccess() {
			//加载眼镜销售成功回调
			progressView.addProgress(20);
		}

		@Override
		public void onLoginSuccess() {
			AppContext.getInstance().setLoginByAuto(false);
			jumpToMainPage(false);
		}

		@Override
		public void onLoginFailed(String reason) {
            System.out.println("-------------------- onLoginFailed！ ------------------ ");
            progressView.setVisibility(View.GONE);
			inputView.setVisibility(View.VISIBLE);
			progressView.setProgress(0);
			
                if(TextUtils.isEmpty(reason)){
                    onToastErrorMsg("登录失败");
                }else{
                    onToastErrorMsg(reason);
                }

		}

		@Override
		public void onLoginCancel() {
			progressView.setProgress(0);
			progressView.setVisibility(View.GONE);
			inputView.setVisibility(View.VISIBLE);
		}

	};
	
	LoginTask task;
	private void startLoginTask() {
		if (task != null && task.getStatus() == Status.RUNNING && !task.isCancelled()) {
			return;
		}
		task = new LoginTask(this, LoginTask.Type_Login);
		task.setLoginSuccessListener(loginListener);
		task.startTask(this, new Object[]{inputView.getAccount(), inputView.getPassword()});
	}
    
    /**
     * 检查是否第一次登录该用户
     */
    private boolean checkFirstLogin(String account) {
        //通过E号查找本地
        UserDALEx user = UserDALEx.get().getUserByUserName(account);
        return user == null || (user.getLoginSuccess() == 0 ? true : false);
    }

}