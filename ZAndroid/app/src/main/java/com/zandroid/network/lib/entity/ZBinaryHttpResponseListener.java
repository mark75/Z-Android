package com.zandroid.network.lib.entity;

public abstract class ZBinaryHttpResponseListener extends
		ZHttpResponseListener {

	/**
	 * construct
	 */
	public ZBinaryHttpResponseListener() {
		super();
	}

	/**
	 * get data ok callback
	 *
	 * @param statusCode
	 *            the status code
	 * @param content
	 *            the content
	 */
	public abstract void onSuccess(int statusCode, byte[] content);

	/**
	 * succesful message
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
