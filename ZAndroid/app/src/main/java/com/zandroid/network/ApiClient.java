package com.zandroid.network;

import android.app.Activity;

import com.zandroid.global.NetworkConfig;
import com.zandroid.network.lib.ZNetworkRequest;
import com.zandroid.network.lib.ZRequestParams;
import com.zandroid.network.lib.ZResponseListener;

/**
 * Created by prin on 2015/12/02.
 *
 */
public class ApiClient {
    public final static int GET = ZNetworkRequest.Method.GET;
    public final static int POST = ZNetworkRequest.Method.POST;

    public final static String nURL = NetworkConfig.BoxServiceAddress;

    /**
     * 设备信息上传
     */
    public static void reportDeviceInfo(Activity tag,ZRequestParams params,ZResponseListener listener){
        String action="";
        new ZRequest(POST,nURL+action,params,listener).setContentJSON(true).start(tag);
    }


}
