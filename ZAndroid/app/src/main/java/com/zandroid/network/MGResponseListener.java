package com.zandroid.network;


public abstract interface MGResponseListener
{
    public abstract void onResponse(MGNetworkRequest request, MGNetworkResponse response);
}
