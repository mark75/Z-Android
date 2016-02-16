package com.zandroid.network;

import android.text.TextUtils;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ZNetworkResponse {

	private VolleyError mError;
	private String mResult;
	private String mCookie;

	public ZNetworkResponse(String result, String cookie,
							ZJsonParserInterface jsonParser) {
		this.mResult = result;
		this.mCookie = cookie;
	}

	public ZNetworkResponse(String result, String cookie) {
		this(result, cookie, null);
	}

	public ZNetworkResponse(String result) {
		this(result, null, null);
	}

	public ZNetworkResponse(VolleyError error) {
		this.mError = error;
	}

	public <T> T getModel(Class<T> clazz) {
		if (TextUtils.isEmpty(mResult)) {
			return null;
		}
		return ZJsonHelper.instance().jsonToModel(mResult, clazz);
	}

	public Map<String, Object> getMap() {
		if (TextUtils.isEmpty(mResult)) {
			return null;
		}
		return ZJsonHelper.instance().jsonToMap(mResult);
	}

	public JSONObject getJSONObject() {
		if (TextUtils.isEmpty(mResult)) {
			return null;
		}

		try {
			return new JSONObject(mResult);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getCookie() {
		return mCookie;
	}

	public String getErrorMessage() {
		if (mError != null) {
			return mError.getMessage();
		}
		return null;
	}

}
