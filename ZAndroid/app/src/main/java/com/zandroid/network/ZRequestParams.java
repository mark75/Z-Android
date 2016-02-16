package com.zandroid.network;


import com.zandroid.network.entity.HttpMultipartMode;
import com.zandroid.network.entity.MultipartEntity;
import com.zandroid.network.entity.mine.content.ByteArrayBody;
import com.zandroid.network.entity.mine.content.ContentBody;
import com.zandroid.network.entity.mine.content.FileBody;
import com.zandroid.network.entity.mine.content.StringBody;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ZRequestParams {

	/** url����. */
	protected ConcurrentHashMap<String, Object> urlParams;
	/** �ļ�����. */
	protected ConcurrentHashMap<String, ContentBody> fileParams;
	private MultipartEntity multiPart = null;
	private final static int boundaryLength = 32;
	private final static String boundaryAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
	private String boundary;

	/**
	 * auto generate boundary string
	 *
	 * @return a boundary string
	 */
	private String getBoundary() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < boundaryLength; ++i)
			sb.append(boundaryAlphabet.charAt(random.nextInt(boundaryAlphabet
					.length())));
		return sb.toString();
	}

	/**
	 * @return get MultipartEntity (apache)
	 */
	public MultipartEntity getMultiPart() {
		return multiPart;
	}

	/**
	 * default boundary is auto generate {@link #getBoundary()}
	 */
	public ZRequestParams() {
		super();
		boundary = getBoundary();
		urlParams = new ConcurrentHashMap<String, Object>();
		fileParams = new ConcurrentHashMap<String, ContentBody>();
		multiPart = new MultipartEntity(HttpMultipartMode.STRICT, boundary,
				Charset.forName("UTF-8"));
	}

	/**
	 * @return multipart boundary string
	 */
	public String boundaryString() {
		return boundary;
	}

	/**
	 * ���һ���ļ�����
	 *
	 * @param attr
	 *            ������
	 * @param file
	 *            �ļ�
	 */
	public void putFile(String attr, File file) {
		if (attr != null && file != null) {
			fileParams.put(attr, new FileBody(file));
		}

	}

	public void putFile(String attr, File file, String mimeType) {
		if (attr != null && file != null) {
			fileParams.put(attr, new FileBody(file, mimeType));
		}

	}

	/**
	 * ���һ��byte[]����
	 *
	 * @param attr
	 *            ������
	 * @param fileName
	 *            �ļ���
	 * @param data
	 *            �ֽ�����
	 */
	public void put(String attr, String fileName, byte[] data) {
		if (attr != null && fileName != null) {
			fileParams.put(attr, new ByteArrayBody(data, fileName));
		}
	}

	/**
	 * ���һ��Object����
	 *
	 * @param attr
	 * @param str
	 */
	public void put(String attr, Object value) {
		try {
			if (attr != null && value != null) {
				urlParams.put(attr, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�����ַ���.
	 *
	 * @return the param string
	 */
	public String getParamString() {
		List<BasicNameValuePair> paramsList = new LinkedList<BasicNameValuePair>();
		for (ConcurrentHashMap.Entry<String, Object> entry : urlParams
				.entrySet()) {
			paramsList.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue().toString()));
		}
		return URLEncodedUtils.format(paramsList, HTTP.UTF_8);
	}

	public String getParamsJsonString() {
		return urlParams == null ? null : ZJsonHelper.instance().objToJson(
				urlParams);
	}

	/**
	 * ��ȡ�����б�.
	 *
	 * @return the params list
	 */
	public List<BasicNameValuePair> getParamsList() {
		List<BasicNameValuePair> paramsList = new LinkedList<BasicNameValuePair>();
		for (ConcurrentHashMap.Entry<String, Object> entry : urlParams
				.entrySet()) {
			paramsList.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue().toString()));
		}
		return paramsList;
	}

	/**
	 *
	 * ��ȡHttpEntity.
	 */
	public HttpEntity getEntity() {

		if (fileParams.isEmpty()) {
			// �������ļ���
			return createFormEntity();
		} else {
			// �����ļ��Ͳ�����
			return createMultipartEntity();
		}
	}

	/**
	 * ����HttpEntity.
	 *
	 * @return the http entity
	 */
	public HttpEntity createFormEntity() {
		try {
			return new UrlEncodedFormEntity(getParamsList(), HTTP.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �����������ļ���HttpEntity.
	 *
	 * @return
	 * @throws IOException
	 */
	private HttpEntity createMultipartEntity() {

		try {
			// Add string params
			for (ConcurrentHashMap.Entry<String, Object> entry : urlParams
					.entrySet()) {
				multiPart.addPart(entry.getKey(), new StringBody(entry
						.getValue().toString(), Charset.forName("UTF-8")));
			}

			// Add file params
			for (ConcurrentHashMap.Entry<String, ContentBody> entry : fileParams
					.entrySet()) {
				ContentBody contentBody = entry.getValue();
				multiPart.addPart(entry.getKey(), contentBody);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return multiPart;
	}

	/**
	 * ��ȡurl����.
	 *
	 * @return the url params
	 */
	public ConcurrentHashMap<String, Object> getUrlParams() {
		return urlParams;
	}

	/**
	 * ��ȡ�ļ�����.
	 * 
	 * @return the file params
	 */
	public ConcurrentHashMap<String, ContentBody> getFileParams() {
		return fileParams;
	}

}
