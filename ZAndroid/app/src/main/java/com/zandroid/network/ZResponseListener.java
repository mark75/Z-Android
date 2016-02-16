package com.zandroid.network;


public abstract interface ZResponseListener
{
    public abstract void onResponse(ZNetworkRequest request, ZNetworkResponse response);
}
