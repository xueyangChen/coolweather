package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable(){
			
			@Override
			public void run() {
				//使用HttpURLConnection请求方式解析
				HttpURLConnection connection = null;
				try{
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();	//获取服务器返回的输入流
					//下面对获取到的输入流进行读取
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while((line = reader.readLine()) != null){
						response.append(line);
					}
					if(listener != null){
						//回调onFinish()方法
						listener.onFinish(response.toString());
					}
				}catch(Exception e){
					if(listener != null){
						//回调onError()方法
						listener.onError(e);
					}
				}finally{
					if(connection != null){
						connection.disconnect();	//将Http连接关闭
					}
				}
			}
			
		}).start();
	}
}
