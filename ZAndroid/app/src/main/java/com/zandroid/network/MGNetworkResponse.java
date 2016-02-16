package com.zandroid.network;

import android.text.TextUtils;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MGNetworkResponse {

	private VolleyError mError;
	private String mResult;
	private String mCookie;

	public MGNetworkResponse(String result, String cookie,
			MGJsonParserInterface jsonParser) {
		this.mResult = result;
		this.mCookie = cookie;
	}

	public MGNetworkResponse(String result, String cookie) {
		this(result, cookie, null);
	}

	public MGNetworkResponse(String result) {
		this(result, null, null);
	}

	public MGNetworkResponse(VolleyError error) {
		this.mError = error;
	}

	public <T> T getModel(Class<T> clazz) {
		if (TextUtils.isEmpty(mResult)) {
			return null;
		}
		return MGJsonHelper.instance().jsonToModel(mResult, clazz);
	}

	public Map<String, Object> getMap() {
		if (TextUtils.isEmpty(mResult)) {
			return null;
		}
		return MGJsonHelper.instance().jsonToMap(mResult);
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
