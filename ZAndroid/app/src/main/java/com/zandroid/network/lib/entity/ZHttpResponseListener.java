/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zandroid.network.lib.entity;

import android.os.Handler;
import android.os.Message;



// TODO: Auto-generated Javadoc

/**
 * ? 2012 amsoft.cn
 * ���ƣ�AbHttpResponseListener.java
 * ������Http��Ӧ������
 *
 * @author ����һ����
 * @version v1.0
 * @date��2013-11-13 ����9:00:52
 */
public abstract class ZHttpResponseListener {

    /** The handler. */
    private Handler mHandler;

    /**
     * ����.
     */
    public ZHttpResponseListener() {
		super();
	}

	/**
	 * ��������ȡ���ݿ�ʼ.
	 */
    public abstract void onStart();


    /**
	 * ��ɺ���ã�ʧ�ܣ��ɹ�������.
	 */
    public abstract void onFinish();

    /**
	 * ����.
	 */
    public void onRetry() {}

    /**
     * ������ʧ�ܣ�����.
     *
     * @param statusCode the status code
     * @param content the content
     * @param error the error
     */
    public abstract void onFailure(int statusCode, String content,Throwable error);

    /**
     * ����.
     *
     * @param bytesWritten the bytes written
     * @param totalSize the total size
     */
    public void onProgress(long bytesWritten, long totalSize) {}

    /**
     * ��ʼ��Ϣ.
     */
    public void sendStartMessage(){
    	sendMessage(obtainMessage(ZHttpClient.START_MESSAGE, null));
    }

    /**
     * �����Ϣ.
     */
    public void sendFinishMessage(){
    	sendMessage(obtainMessage(ZHttpClient.FINISH_MESSAGE,null));
    }

    /**
     * ������Ϣ.
     *
     * @param bytesWritten the bytes written
     * @param totalSize the total size
     */
    public void sendProgressMessage(long bytesWritten, long totalSize) {
        sendMessage(obtainMessage(ZHttpClient.PROGRESS_MESSAGE, new Object[]{bytesWritten, totalSize}));
    }

    /**
     * ʧ����Ϣ.
     *
     * @param statusCode the status code
     * @param content the content
     * @param error the error
     */
    public void sendFailureMessage(int statusCode,String content,Throwable error){
    	sendMessage(obtainMessage(ZHttpClient.FAILURE_MESSAGE, new Object[]{statusCode,content, error}));
    }

    /**
     * ������Ϣ.
     */
    public void sendRetryMessage() {
        sendMessage(obtainMessage(ZHttpClient.RETRY_MESSAGE, null));
    }

    /**
     * ������Ϣ.
     * @param msg the msg
     */
    public void sendMessage(Message msg) {
        if (msg != null) {
        	msg.sendToTarget();
        }
    }

    /**
     * ����Message.
     * @param responseMessage the response message
     * @param response the response
     * @return the message
     */
    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg;
        if (mHandler != null) {
            msg = mHandler.obtainMessage(responseMessage, response);
        } else {
            msg = Message.obtain();
            if (msg != null) {
                msg.what = responseMessage;
                msg.obj = response;
            }
        }
        return msg;
    }

	/**
	 * Gets the handler.
	 *
	 * @return the handler
	 */
	public Handler getHandler() {
		return mHandler;
	}

	/**
	 * ����������Handler.
	 *
	 * @param handler the new handler
	 */
	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}

}
