package com.coolweather.app.service;

import com.coolweather.app.receiver.AutoUpdateReceiver;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent bull) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent,int flags,int startId){
		new Thread(new Runnable(){
			@Override
			public void run() {
				updateWeather();
			}	
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 8*60*60*1000;	//这是8小时内的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
		Intent i= new Intent(this,AutoUpdateService.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return startId;
	}

	/*
	 * 更新天气信息
	 */
	private void updateWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code", "");
		//String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		String address = "http://api.k780.com:88/?app=weather.today&weaid="+weatherCode
				+"&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
			
		});
	}

}
