package com.zandroid.network;

import com.android.volley.VolleyError;

public class ZResponseError extends Exception {

	public final VolleyError volleyError;

	public ZResponseError() {
		this.volleyError = null;
	}

	public ZResponseError(VolleyError error) {
		this.volleyError = error;
	}

	public ZResponseError(String exceptionMessage) {
		super(exceptionMessage);
		this.volleyError = null;
	}

	public ZResponseError(String exceptionMessage, Throwable reason) {
		super(exceptionMessage, reason);
		this.volleyError = null;
	}

	public ZResponseError(Throwable cause) {
		super(cause);
		this.volleyError = null;
	}
}
