package com.zandroid.network.lib.entity;

import android.content.Context;

import com.zandroid.utils.ZFileUtil;

import java.io.File;

public abstract class ZFileHttpResponseListener extends ZHttpResponseListener {

	/** ��ǰ�����ļ�. */
	private File mFile;

	/**
	 * �����ļ��Ĺ���,��Ĭ�ϵĻ��淽ʽ.
	 *
	 * @param url
	 *            the url
	 */
	public ZFileHttpResponseListener(String url) {
		super();
	}

	/**
	 * Ĭ�ϵĹ���.
	 */
	public ZFileHttpResponseListener() {
		super();
	}

	/**
	 * �����ļ��Ĺ���,ָ�������ļ�����.
	 *
	 * @param file
	 *            �����ļ�����
	 */
	public ZFileHttpResponseListener(File file) {
		super();
		this.mFile = file;
	}

	/**
	 * �����������ļ��ɹ����������.
	 *
	 * @param statusCode
	 *            the status code
	 * @param file
	 *            the file
	 */
	public void onSuccess(int statusCode, File file) {
	};

	/**
	 * ���������ļ��ϴ��ɹ�����.
	 *
	 * @param statusCode
	 *            the status code
	 */
	public void onSuccess(int statusCode) {
	};

	/**
	 * �ɹ���Ϣ.
	 *
	 * @param statusCode
	 *            the status code
	 */
	public void sendSuccessMessage(int statusCode) {
		sendMessage(obtainMessage(ZHttpClient.SUCCESS_MESSAGE,
				new Object[] { statusCode }));
	}

	/**
	 * ʧ����Ϣ.
	 *
	 * @param statusCode
	 *            the status code
	 * @param error
	 *            the error
	 */
	public void sendFailureMessage(int statusCode, Throwable error) {
		sendMessage(obtainMessage(ZHttpClient.FAILURE_MESSAGE, new Object[] {
				statusCode, error }));
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return mFile;
	}

	/**
	 * Sets the file.
	 *
	 * @param file
	 *            the new file
	 */
	public void setFile(File file) {
		this.mFile = file;
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the file.
	 *
	 * @param context
	 *            the context
	 * @param name
	 *            the name
	 */
	public void setFile(Context context, String name) {
		// ���ɻ����ļ�
		File file = new File(ZFileUtil.getDownloadPath(context) + name);
		setFile(file);

	}

}
