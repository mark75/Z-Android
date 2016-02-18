package com.zandroid.network;

import com.android.volley.AuthFailureError;
import com.zandroid.network.lib.ZNetworkRequest;
import com.zandroid.network.lib.ZRequestParams;
import com.zandroid.network.lib.ZResponseListener;

import java.util.Map;

/**
 * Created by prin on 2015/11/23.
 */
public class ZRequest extends ZNetworkRequest {

    private boolean mEnableCookie = true;

    public ZRequest(int method, String url, ZRequestParams params, ZResponseListener listener) {
        super(method, url, params, listener);
    }

    public ZRequest(int method, String url, String body, ZResponseListener listener) {
        super(method, url, body, listener);
    }

    public ZRequest(int method, String url, String body) {
        super(method, url, body);
    }

    public void setEnableCookie(boolean enable) {
        this.mEnableCookie = enable;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }
}
