package com.coolweather.app.util;

//使用接口来回调服务器返回的结果
public interface HttpCallbackListener {
	void onFinish(String response);	//服务器成功响应我们的请求时调用
	void onError(Exception e);	//进行网络操作错误的时候调用
}
