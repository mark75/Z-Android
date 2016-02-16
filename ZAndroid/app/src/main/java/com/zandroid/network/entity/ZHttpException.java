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
	public static String CONNECT_EXCEPTION = "无法连接到网络";

	/** The Constant UNKNOWNHOSTEXCEPTION. */
	public static String UNKNOWN_HOST_EXCEPTION = "连接远程地址失败";

	/** The Constant SOCKETEXCEPTION. */
	public static String SOCKET_EXCEPTION = "网络连接出错，请重试";

	/** The Constant SOCKETTIMEOUTEXCEPTION. */
	public static String SOCKET_TIMEOUT_EXCEPTION = "连接超时，请重试";

	/** The Constant NULLPOINTEREXCEPTION. */
	public static String NULL_POINTER_EXCEPTION = "抱歉，远程服务出错了";

	/** The Constant NULLMESSAGEEXCEPTION. */
	public static String NULL_MESSAGE_EXCEPTION = "抱歉，程序出错了";

	/** The Constant CLIENTPROTOCOLEXCEPTION. */
	public static String CLIENT_PROTOCOL_EXCEPTION = "Http请求参数错误";

	/** 参数个数不够. */
	public static String MISSING_PARAMETERS = "参数没有包含足够的值";

	/** The Constant REMOTESERVICEEXCEPTION. */
	public static String REMOTE_SERVICE_EXCEPTION = "抱歉，远程服务出错了";

	/** 资源未找到. */
	public static String NOT_FOUND_EXCEPTION = "页面未找到";

	/** 没有权限访问资源. */
	public static String FORBIDDEN_EXCEPTION = "没有权限访问资源";

	/** 其他异常. */
	public static String UNTREATED_EXCEPTION = "未处理的异常";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1;

	/** 异常消息. */
	private String msg = null;

	/**
	 * 构造异常类.
	 *
	 * @param e
	 *            异常
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
	 * 用一个消息构造异常类.
	 *
	 * @param message
	 *            异常的消息
	 */
	public ZHttpException(String message) {
		super(message);
		msg = message;
	}

	/**
	 * 描述：获取异常信息.
	 * 
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return msg;
	}

}
