package com.zandroid.network.entity;

public abstract class ZBinaryHttpResponseListener extends
		ZHttpResponseListener {

	/**
	 * 构造.
	 */
	public ZBinaryHttpResponseListener() {
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
		sendMessage(obtainMessage(ZHttpClient.SUCCESS_MESSAGE, new Object[] {
				statusCode, content }));
	}

}
