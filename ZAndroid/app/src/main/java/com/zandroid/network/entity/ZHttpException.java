package com.zandroid.network.entity;

import com.zandroid.utils.ZStringUtil;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ZHttpException extends Exception {

	/** The Constant CONNECTEXCEPTION. */
	public static String CONNECT_EXCEPTION = "�޷����ӵ�����";

	/** The Constant UNKNOWNHOSTEXCEPTION. */
	public static String UNKNOWN_HOST_EXCEPTION = "����Զ�̵�ַʧ��";

	/** The Constant SOCKETEXCEPTION. */
	public static String SOCKET_EXCEPTION = "�������ӳ���������";

	/** The Constant SOCKETTIMEOUTEXCEPTION. */
	public static String SOCKET_TIMEOUT_EXCEPTION = "���ӳ�ʱ��������";

	/** The Constant NULLPOINTEREXCEPTION. */
	public static String NULL_POINTER_EXCEPTION = "��Ǹ��Զ�̷��������";

	/** The Constant NULLMESSAGEEXCEPTION. */
	public static String NULL_MESSAGE_EXCEPTION = "��Ǹ�����������";

	/** The Constant CLIENTPROTOCOLEXCEPTION. */
	public static String CLIENT_PROTOCOL_EXCEPTION = "Http�����������";

	/** ������������. */
	public static String MISSING_PARAMETERS = "����û�а����㹻��ֵ";

	/** The Constant REMOTESERVICEEXCEPTION. */
	public static String REMOTE_SERVICE_EXCEPTION = "��Ǹ��Զ�̷��������";

	/** ��Դδ�ҵ�. */
	public static String NOT_FOUND_EXCEPTION = "ҳ��δ�ҵ�";

	/** û��Ȩ�޷�����Դ. */
	public static String FORBIDDEN_EXCEPTION = "û��Ȩ�޷�����Դ";

	/** �����쳣. */
	public static String UNTREATED_EXCEPTION = "δ������쳣";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1;

	/** �쳣��Ϣ. */
	private String msg = null;

	/**
	 * �����쳣��.
	 *
	 * @param e
	 *            �쳣
	 */
	public ZHttpException(Exception e) {
		super();

		try {
			if (e instanceof HttpHostConnectException) {
				msg = UNKNOWN_HOST_EXCEPTION;
			} else if (e instanceof ConnectException) {
				msg = CONNECT_EXCEPTION;
			} else if (e instanceof ConnectTimeoutException) {
				msg = CONNECT_EXCEPTION;
			} else if (e instanceof UnknownHostException) {
				msg = UNKNOWN_HOST_EXCEPTION;
			} else if (e instanceof SocketException) {
				msg = SOCKET_EXCEPTION;
			} else if (e instanceof SocketTimeoutException) {
				msg = SOCKET_TIMEOUT_EXCEPTION;
			} else if (e instanceof NullPointerException) {
				msg = NULL_POINTER_EXCEPTION;
			} else if (e instanceof ClientProtocolException) {
				msg = CLIENT_PROTOCOL_EXCEPTION;
			} else {
				if (e == null || ZStringUtil.isEmpty(e.getMessage())) {
					msg = NULL_MESSAGE_EXCEPTION;
				} else {
					msg = e.getMessage();
				}
			}
		} catch (Exception e1) {
		}

	}

	/**
	 * ��һ����Ϣ�����쳣��.
	 *
	 * @param message
	 *            �쳣����Ϣ
	 */
	public ZHttpException(String message) {
		super(message);
		msg = message;
	}

	/**
	 * ��������ȡ�쳣��Ϣ.
	 * 
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return msg;
	}

}
