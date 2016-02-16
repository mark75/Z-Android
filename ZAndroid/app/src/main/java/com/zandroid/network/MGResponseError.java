package com.zandroid.network;

import com.android.volley.VolleyError;

public class MGResponseError extends Exception {

	public final VolleyError volleyError;

	public MGResponseError() {
		this.volleyError = null;
	}

	public MGResponseError(VolleyError error) {
		this.volleyError = error;
	}

	public MGResponseError(String exceptionMessage) {
		super(exceptionMessage);
		this.volleyError = null;
	}

	public MGResponseError(String exceptionMessage, Throwable reason) {
		super(exceptionMessage, reason);
		this.volleyError = null;
	}

	public MGResponseError(Throwable cause) {
		super(cause);
		this.volleyError = null;
	}
}
