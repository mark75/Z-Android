package com.zandroid.network.entity;

public abstract class MGBinaryHttpResponseListener extends
		MGHttpResponseListener {

	/**
	 * 构造.
	 */
	public MGBinaryHttpResponseListener() {
		super();
	}

	/**
	 * 描述：获取数据成功会调用这里.
	 *
	 * @param statusCode
	 *            the status code
	 * @param content
	 *            the content
	 */
	public abstract void onSuccess(int statusCode, byte[] content);

	/**
	 * 成功消息.
	 * 
	 * @param statusCode
	 *            the status code
	 * @param content
	 *            the content
	 */
	public void sendSuccessMessage(int statusCode, byte[] content) {
		sendMessage(obtainMessage(MGHttpClient.SUCCESS_MESSAGE, new Object[] {
				statusCode, content }));
	}

}
