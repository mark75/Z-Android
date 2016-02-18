package com.zandroid.network.lib;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.zandroid.utils.ZLogUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class ZNetworkRequest extends Request<String> {

	private String mBody;
	private ZResponseListener mListener;
	private Map<String, String> mHeaders = new HashMap<String, String>();
	private static final String interval = "|";
	private int mReqType = 0;
	private boolean mContentJSON = true;
	private boolean mGZip = true;
	private int mMethod;
	private ZRequestParams mRequestParams;

	private static final String HEADER_CHARSET = "Charset";
	private static final String HEADER_CHARSET_UTF8 = "UTF-8";
	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	private static final String HEADER_CONTENT_JSON = "application/json";
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String HEADER_ENCODING_GZIP = "gzip,deflate";

	public interface Method {
		int DEPRECATED_GET_OR_POST = -1;
		int GET = 0;
		int POST = 1;
		int PUT = 2;
		int DELETE = 3;
	}

	public ZNetworkRequest(int method, String url, ZRequestParams params,
						   ZResponseListener listener) {
		super(method, getRealUrl(method, url, params), null);
		mMethod = method;
		mRequestParams = params;
		mListener = listener;
	}

	public ZNetworkRequest(int method, String url, String body,
						   ZResponseListener listener) {
		super(method, url, null);
		mMethod = method;
		mBody = body;
		mListener = listener;
	}

	public ZNetworkRequest(int method, String url, String body) {
		this(method, url, body, null);
	}

	public ZNetworkRequest setHeader(String key, String value) {
		mHeaders.put(key, value);
		return this;
	}

	public ZNetworkRequest setHeaders(Map<String, String> headers) {
		mHeaders.putAll(headers);
		return this;
	}

	public ZNetworkRequest setGZip(boolean value) {
		mGZip = value;
		return this;
	}

	public ZNetworkRequest setContentJSON(boolean value) {
		mContentJSON = value;
		return this;
	}

	public ZNetworkRequest setReqTye(int reqType) {
		mReqType = reqType;
		return this;
	}

	public int getReqType() {
		return mReqType;
	}

	public void start(Context context) {
		ZNetworkRequestManager.addRequest(this, context.getClass()
				.getSimpleName());
	}

	public void start(Fragment fragment) {
		ZNetworkRequestManager.addRequest(this, fragment.getClass()
				.getSimpleName());
	}

	private static String getRealUrl(int method, String url,
			ZRequestParams params) {

		if (params != null && (method == Method.GET || method == Method.DELETE)) {
			if (url.indexOf("?") == -1) {
				url += "?";
			}
			url += params.getParamString();
		}

		return url;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		mHeaders.put(HEADER_CHARSET, HEADER_CHARSET_UTF8);
		if (mGZip) {
			mHeaders.put(HEADER_ACCEPT_ENCODING, HEADER_ENCODING_GZIP);
		}
		if (mContentJSON) {
			mHeaders.put(HEADER_CONTENT_TYPE, HEADER_CONTENT_JSON);
		}
		return mHeaders;
	}

	@Override
	public byte[] getBody() {

		String body = null;
		try {
			if (mBody != null) {
				body = mBody;
			} else if (mRequestParams != null
					&& (mMethod == Method.POST || mMethod == Method.PUT)) {

				body = mContentJSON ? mRequestParams.getParamsJsonString()
						: mRequestParams.getParamString();
			}
			return body == null ? null : body.getBytes(HEADER_CHARSET_UTF8);
		} catch (UnsupportedEncodingException uee) {
			ZLogUtil
					.e(this.getClass(),
							String.format(
									"Unsupported Encoding while trying to get the bytes of %s using %s",
									body, HEADER_CHARSET_UTF8));
		}
		return null;
	}

	@Override
	public void deliverError(VolleyError error) {
		if (mListener != null) {
			mListener.onResponse(this, new ZNetworkResponse(error));
		}
	}

	@Override
	protected void deliverResponse(String response) {
		if (mListener != null) {
			mListener.onResponse(this, buildFromResponse(response));
		}
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String result = getRealString(response.data);

		// get cookie for next use
		String cookie = response.headers.get("Set-Cookie");
		if (cookie != null) {
			result += cookie + interval + cookie.length();
		} else {
			result += interval + 0;
		}

		return Response.success(result,
				HttpHeaderParser.parseCacheHeaders(response));
	}

	private int getShort(byte[] data) {
		return (int) ((data[0] << 8) | data[1] & 0xFF);
	}

	private String getRealString(byte[] data) {
		byte[] h = new byte[2];
		h[0] = (data)[0];
		h[1] = (data)[1];
		int head = getShort(h);
		boolean t = head == 0x1f8b;
		InputStream in;
		StringBuilder sb = new StringBuilder();
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			if (t) {
				in = new GZIPInputStream(bis);
			} else {
				in = bis;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(in),
					1000);
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				sb.append(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private ZNetworkResponse buildFromResponse(String response) {
		String cookie = null;
		String result = null;

		int pos = response.lastIndexOf(interval);
		if (pos < 0) {
			result = response;
		} else {
			int cookieLen = Integer.parseInt(response.substring(pos + 1));
			result = response.substring(0, pos - cookieLen);
			cookie = response.substring(pos - cookieLen, pos);
		}

		return new ZNetworkResponse(result, cookie);
	}

}
