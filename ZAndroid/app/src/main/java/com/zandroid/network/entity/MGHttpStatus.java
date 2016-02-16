package com.zandroid.network.entity;

public class MGHttpStatus {

	/** 成功返回码. */
	public static final int SUCCESS_CODE = 200;

	/** 连接失败的HTTP返回码. */
	public static final int CONNECT_FAILURE_CODE = 600;

	/** 连接超时的HTTP返回码. */
	public static final int CONNECT_TIMEOUT_CODE = 601;

	/** 响应失败的HTTP返回码. */
	public static final int RESPONSE_TIMEOUT_CODE = 602;

	/** 未处理的HTTP返回码. */
	public static final int UNTREATED_CODE = 900;

	/** 服务出错的HTTP返回码. */
	public static final int SERVER_FAILURE_CODE = 500;

}
