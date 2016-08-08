package com.wty.ution.data.dalex;

import android.content.ContentValues;
import android.database.Cursor;

import com.wty.ution.data.UtionDB;
import com.wty.ution.data.annotation.DatabaseField;
import com.wty.ution.data.annotation.DatabaseField.FieldType;
import com.wty.ution.data.annotation.SqliteBaseDALEx;
import com.wty.ution.data.annotation.SqliteDao;

/**
 * 功能描述:用户基本信息存储
 */
public class UserDALEx extends SqliteBaseDALEx {
	
	private static final long serialVersionUID = 1L;
	private static final String USERNUMBER = "usernumber";			//个人E号 INT4
	private static final String ACCOUNTNO = "accountno";            //帐号
	private static final String LOGINSUCCESS = "loginSuccess";            //帐号
	private static final String ENTERPRISETYPE = "enterprisetype";
	private static final String USERNAME = "username";
	
	@DatabaseField(Type= FieldType.VARCHAR,primaryKey=true)
	private String usernumber;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String username;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String sex;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String birthday;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String mobilephone;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String tel;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String email;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String logourl;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String xwcreateby;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String xwcreatename;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String xwupdateby;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String xwupdatetime;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String password;
	@DatabaseField(Type=FieldType.VARCHAR)
	private String accountno;
	@DatabaseField(Type=FieldType.INT)
	private int loginSuccess;
	@DatabaseField(Type=FieldType.INT)
	private int enterprisetype;
	public static UserDALEx get() {
		return SqliteDao.getDao(UserDALEx.class);
	}
	
	/**
	 * 功能描述：保存用户信息至数据库，有相同的e号即更新，没有就做插入操作
	 */
	public void save(final UserDALEx dalex){
		operatorWithTransaction(new OnTransactionListener() {

			@Override
			public boolean onTransaction(UtionDB db) {
				ContentValues values = dalex.tranform2Values();
				if (isExist(USERNUMBER, dalex.getUsernumber())) {
					db.update(TABLE_NAME, values, USERNUMBER + "=?", new String[]{dalex.getUsernumber()});
				} else {
					db.save(TABLE_NAME, values);
				}
				return true;
			}
		});
	}

	/**
	 * 通过获取e号获取用户
	 */
	public UserDALEx getUserByUsernumber(String usernumber) {
		UserDALEx user = null;
		Cursor cursor = null;
		try {
			UtionDB db = getDB();
			if (db.isTableExits(TABLE_NAME)) {
				cursor = db.find("select * from "+ TABLE_NAME + "  where " + USERNUMBER + "=? ",new String[] { usernumber });
				if (cursor.moveToNext()) {
					user = new UserDALEx();
					user.setAnnotationField(cursor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return user;
	}

	/**
	 * 通过获取用户名获取用户
	 */
	public UserDALEx getUserByUserName(String username) {
		UserDALEx user = null;
		Cursor cursor = null;
		try {
			UtionDB db = getDB();
			if (db.isTableExits(TABLE_NAME)) {
				cursor = db.find("select * from "+ TABLE_NAME + "  where " + USERNAME + "=? ",new String[] { username });
				if (cursor.moveToNext()) {
					user = new UserDALEx();
					user.setAnnotationField(cursor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return user;
	}

	public void updateLoginSuccess(String username){
        UtionDB db = null;
        try {
            db = getDB();
            db.getConnection().beginTransaction();
            ContentValues values = new ContentValues();
            values.put(LOGINSUCCESS, 1);
            db.update(TABLE_NAME,values, USERNAME+"=? ", new String[]{username});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			if (db != null) {
                db.getConnection().setTransactionSuccessful();
                db.getConnection().endTransaction();
            }
        }
    }
	
	public int getLoginSuccess() {
		return loginSuccess;
	}

	public void setLoginSuccess(int loginSuccess) {
		this.loginSuccess = loginSuccess;
	}

	public int getEnterprisetype() {
		return enterprisetype;
	}

	public void setEnterprisetype(int enterprisetype) {
		this.enterprisetype = enterprisetype;
	}

	public String getUsernumber() {
		return usernumber;
	}

	public void setUsernumber(String usernumber) {
		this.usernumber = usernumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	public String getXwcreateby() {
		return xwcreateby;
	}

	public void setXwcreateby(String xwcreateby) {
		this.xwcreateby = xwcreateby;
	}

	public String getXwcreatename() {
		return xwcreatename;
	}

	public void setXwcreatename(String xwcreatename) {
		this.xwcreatename = xwcreatename;
	}

	public String getXwupdateby() {
		return xwupdateby;
	}

	public void setXwupdateby(String xwupdateby) {
		this.xwupdateby = xwupdateby;
	}

	public String getXwupdatetime() {
		return xwupdatetime;
	}

	public void setXwupdatetime(String xwupdatetime) {
		this.xwupdatetime = xwupdatetime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccountno() {
		return accountno;
	}

	public void setAccountno(String accountno) {
		this.accountno = accountno;
	}
}
