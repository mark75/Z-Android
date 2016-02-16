package com.zandroid.network.entity;

public abstract class MGBinaryHttpResponseListener extends
		MGHttpResponseListener {

	/**
	 * ����.
	 */
	public MGBinaryHttpResponseListener() {
		super();
	}

	/**
	 * ��������ȡ���ݳɹ����������.
	 *
	 * @param statusCode
	 *            the status code
	 * @param content
	 *            the content
	 */
	public abstract void onSuccess(int statusCode, byte[] content);

	/**
	 * �ɹ���Ϣ.
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
