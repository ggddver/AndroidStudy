package com.ggddver.userinterface;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	private TextView text;
	private Switch switcher;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComponent();
		switcher.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Switch swt = (Switch)v;
				if(swt.isChecked()) {
					try {
						setMobileDataEnabled(true);
					} catch (NoSuchFieldException | ClassNotFoundException
							| IllegalAccessException | IllegalArgumentException
							| NoSuchMethodException | InvocationTargetException e) {
						e.printStackTrace();
					}
				} else {
					try {
						setMobileDataEnabled(false);
					} catch (NoSuchFieldException | ClassNotFoundException
							| IllegalAccessException | IllegalArgumentException
							| NoSuchMethodException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initComponent() {
		text = (TextView) findViewById(R.id.text);
		switcher = (Switch) findViewById(R.id.button1);
	}
	
	/**
	 * 通过反射开启移动网络
	 * 需要添加网络相关权限
	 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
     * <uses-permission android:name="android.permission.INTERNET"/>
	 * @throws ClassNotFoundException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
	public void setMobileDataEnabled(boolean enabled) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
		ConnectivityManager conn = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		//获取ConnectivityManager class中的mService
		Field mServiceField = Class.forName(ConnectivityManager.class.getName()).getDeclaredField("mService");
		//获取conn中的mService实例
		Object mService = mServiceField.get(conn);
		//获取mService 即IConnectivityManager类中的setMobileDataEnabled方法 方法参数为boolean类型
		Method setMobileDataEnabled = Class.forName(mService.getClass().getName()).getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		setMobileDataEnabled.invoke(mService, enabled);
	}
}
