package com.zandroid.network.lib;


public abstract interface ZResponseListener
{
    public abstract void onResponse(ZNetworkRequest request, ZNetworkResponse response);
}
