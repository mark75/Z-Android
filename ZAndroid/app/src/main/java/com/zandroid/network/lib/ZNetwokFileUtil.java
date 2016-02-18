package com.zandroid.network.lib;

import android.content.Context;

import com.zandroid.ZAppApplication;
import com.zandroid.network.lib.entity.ZFileHttpResponseListener;
import com.zandroid.network.lib.entity.ZHttpClient;
import com.zandroid.network.lib.entity.ZStringHttpResponseListener;

import java.io.File;

public class ZNetwokFileUtil {

	private ZHttpClient mClient = null;
	private static ZNetwokFileUtil mIntance = null;

	public ZNetwokFileUtil(Context context) {
		this.mClient = new ZHttpClient(context);
	}

	public static ZNetwokFileUtil getInstance() {

		if (mIntance == null) {
			mIntance = new ZNetwokFileUtil(ZAppApplication.getContext());
		}
		return mIntance;
	}

	public void download(String url, File dstFile,
			ZFileHttpResponseListener respListener) {
		respListener.setFile(dstFile);
		mClient.get(url, null, respListener);
	}

	public void download(String url, String dstFile,
			ZFileHttpResponseListener respListener) {
		download(url, new File(dstFile), respListener);
	}

	public void upload(String url, ZRequestParams params,
			ZStringHttpResponseListener respListener) {
		mClient.post(url, params, respListener);
	}

}
