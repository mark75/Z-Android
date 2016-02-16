package com.zandroid.network;

import android.content.Context;

import com.zandroid.ZAppApplication;
import com.zandroid.network.entity.MGFileHttpResponseListener;
import com.zandroid.network.entity.MGHttpClient;
import com.zandroid.network.entity.MGStringHttpResponseListener;

import java.io.File;

public class MGNetwokFileUtil {

	private MGHttpClient mClient = null;
	private static MGNetwokFileUtil mIntance = null;

	public MGNetwokFileUtil(Context context) {
		this.mClient = new MGHttpClient(context);
	}

	public static MGNetwokFileUtil getInstance() {

		if (mIntance == null) {
			mIntance = new MGNetwokFileUtil(ZAppApplication.getContext());
		}
		return mIntance;
	}

	public void download(String url, File dstFile,
			MGFileHttpResponseListener respListener) {
		respListener.setFile(dstFile);
		mClient.get(url, null, respListener);
	}

	public void download(String url, String dstFile,
			MGFileHttpResponseListener respListener) {
		download(url, new File(dstFile), respListener);
	}

	public void upload(String url, MGRequestParams params,
			MGStringHttpResponseListener respListener) {
		mClient.post(url, params, respListener);
	}

}
