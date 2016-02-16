package com.zandroid.network.entity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.zandroid.network.MGRequestParams;
import com.zandroid.utils.ZDeviceInfoUtil;
import com.zandroid.utils.ZFileUtil;
import com.zandroid.utils.ZLogUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLHandshakeException;


public class MGHttpClient {

	/** ������. */
	private static Context mContext;

	/** �߳�ִ����. */
	public static Executor mExecutorService = null;

	/** ����. */
	private String encode = HTTP.UTF_8;

	/** �û�����. */
	private String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 BIDUBrowser/6.x Safari/537.31";

	private static final String HTTP_GET = "GET";
	private static final String HTTP_POST = "POST";
	private static final String USER_AGENT = "User-Agent";
	private static final String ACCEPT_ENCODING = "Accept-Encoding";

	/** CookieStore. */
	private CookieStore mCookieStore;

	/** ���������. */
	private static final int DEFAULT_MAX_CONNECTIONS = 10;

	/** ��ʱʱ��. */
	public static final int DEFAULT_SOCKET_TIMEOUT = 10000;

	/** ���Դ���. */
	private static final int DEFAULT_MAX_RETRIES = 2;

	/** �����С. */
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;

	/** �ɹ�. */
	protected static final int SUCCESS_MESSAGE = 0;

	/** ʧ��. */
	protected static final int FAILURE_MESSAGE = 1;

	/** ��������ص�ʧ��. */
	protected static final int FAILURE_MESSAGE_CONNECT = 2;

	/** �ͷ�����ص�ʧ��. */
	protected static final int FAILURE_MESSAGE_SERVICE = 3;

	/** ��ʼ. */
	protected static final int START_MESSAGE = 4;

	/** ���. */
	protected static final int FINISH_MESSAGE = 5;

	/** ������. */
	protected static final int PROGRESS_MESSAGE = 6;

	/** ����. */
	protected static final int RETRY_MESSAGE = 7;

	/** ��ʱʱ��. */
	private int mTimeout = DEFAULT_SOCKET_TIMEOUT;

	/** ͨ��֤��. ���Ҫ��HTTPS���ӣ���ʹ��SSL������ */
	private boolean mIsOpenEasySSL = true;

	/** HTTP Client */
	private DefaultHttpClient mHttpClient = null;

	/** HTTP ������ */
	private HttpContext mHttpContext = null;

	/**
	 * ��ʼ��.
	 *
	 * @param context
	 *            the context
	 */
	public MGHttpClient(Context context) {
		mContext = context;
		mExecutorService = MGThreadFactory.getExecutorService();
		mHttpContext = new BasicHttpContext();
	}

	/**
	 * ��������������get����.
	 *
	 * @param url
	 *            the url
	 * @param params
	 *            the params
	 * @param responseListener
	 *            the response listener
	 */
	public void get(final String url, final MGRequestParams params,
					final MGHttpResponseListener responseListener) {

		responseListener.setHandler(new ResponderHandler(responseListener));
		mExecutorService.execute(new Runnable() {
			public void run() {
				try {
					doGet(url, params, responseListener);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * ��������������post����.
	 *
	 * @param url
	 *            the url
	 * @param params
	 *            the params
	 * @param responseListener
	 *            the response listener
	 */
	public void post(final String url, final MGRequestParams params,
					 final MGHttpResponseListener responseListener) {
		responseListener.setHandler(new ResponderHandler(responseListener));
		mExecutorService.execute(new Runnable() {
			public void run() {
				try {
					doPost(url, params, responseListener);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * ������ִ��get����.
	 *
	 * @param url
	 *            the url
	 * @param params
	 *            the params
	 * @param responseListener
	 *            the response listener
	 */
	private void doGet(String url, MGRequestParams params,
					   MGHttpResponseListener responseListener) {
		try {

			responseListener.sendStartMessage();

			if (!ZDeviceInfoUtil.isNetworkAvailable(mContext)) {
				Thread.sleep(200);
				responseListener.sendFailureMessage(
						MGHttpStatus.CONNECT_FAILURE_CODE,
						MGHttpException.CONNECT_EXCEPTION, new MGHttpException(
								MGHttpException.CONNECT_EXCEPTION));
				return;
			}

			// HttpGet���Ӷ���
			if (params != null) {
				if (url.indexOf("?") == -1) {
					url += "?";
				}
				url += params.getParamString();
			}
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader(USER_AGENT, userAgent);
			// ѹ��
			httpGet.addHeader(ACCEPT_ENCODING, "gzip");
			// ȡ��Ĭ�ϵ�HttpClient
			HttpClient httpClient = getHttpClient();
			// ȡ��HttpResponse
			String response = httpClient.execute(httpGet,
					new RedirectionResponseHandler(url, responseListener),
					mHttpContext);
			ZLogUtil.i(mContext, "[HTTP GET]:" + url + ",result��" + response);
		} catch (Exception e) {
			e.printStackTrace();
			// ����ʧ����Ϣ
			responseListener.sendFailureMessage(MGHttpStatus.UNTREATED_CODE,
					e.getMessage(), new MGHttpException(e));
		} finally {
			responseListener.sendFinishMessage();
		}
	}

	/**
	 * ������ִ��post����.
	 *
	 * @param url
	 *            the url
	 * @param params
	 *            the params
	 * @param responseListener
	 *            the response listener
	 */
	private void doPost(String url, MGRequestParams params,
						MGHttpResponseListener responseListener) {
		try {
			responseListener.sendStartMessage();

			if (!ZDeviceInfoUtil.isNetworkAvailable(mContext)) {
				Thread.sleep(200);
				responseListener.sendFailureMessage(
						MGHttpStatus.CONNECT_FAILURE_CODE,
						MGHttpException.CONNECT_EXCEPTION, new MGHttpException(
								MGHttpException.CONNECT_EXCEPTION));
				return;
			}

			// HttpPost���Ӷ���
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader(USER_AGENT, userAgent);
			// ѹ��
			httpPost.addHeader(ACCEPT_ENCODING, "gzip");
			// �Ƿ�����ļ�
			boolean isContainFile = false;
			if (params != null) {
				// ʹ��NameValuePair������Ҫ���ݵ�Post���������ַ���
				HttpEntity httpentity = params.getEntity();
				// ����httpRequest
				httpPost.setEntity(httpentity);
				if (params.getFileParams().size() > 0) {
					isContainFile = true;
				}
			}
			String response = null;
			// ȡ��Ĭ�ϵ�HttpClient
			DefaultHttpClient httpClient = getHttpClient();
			if (isContainFile) {
				httpPost.addHeader("connection", "keep-alive");
				httpPost.addHeader(
						"Content-Type",
						"multipart/form-data; boundary="
								+ params.boundaryString());
				ZLogUtil.i(mContext, "[HTTP POST]:" + url + ",�����ļ���!");
			}
			// ȡ��HttpResponse
			response = httpClient.execute(httpPost,
					new RedirectionResponseHandler(url, responseListener),
					mHttpContext);
			ZLogUtil.i(mContext, "request��" + url + ",result��" + response);

		} catch (Exception e) {
			e.printStackTrace();
			ZLogUtil.i(mContext,
					"[HTTP POST]:" + url + ",error��" + e.getMessage());
			// ����ʧ����Ϣ
			responseListener.sendFailureMessage(MGHttpStatus.UNTREATED_CODE,
					e.getMessage(), new MGHttpException(e));
		} finally {
			responseListener.sendFinishMessage();
		}
	}

	/**
	 * �򵥵�����,ֻ֧�ַ��ص�������String����,��֧��ת���ض���
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public void doRequest(final String url, final MGRequestParams params,
						  final MGStringHttpResponseListener responseListener) {
		responseListener.setHandler(new ResponderHandler(responseListener));
		mExecutorService.execute(new Runnable() {
			public void run() {
				HttpURLConnection urlConn = null;
				try {
					responseListener.sendStartMessage();

					if (!ZDeviceInfoUtil.isNetworkAvailable(mContext)) {
						Thread.sleep(200);
						responseListener.sendFailureMessage(
								MGHttpStatus.CONNECT_FAILURE_CODE,
								MGHttpException.CONNECT_EXCEPTION,
								new MGHttpException(
										MGHttpException.CONNECT_EXCEPTION));
						return;
					}

					String resultString = null;
					URL requestUrl = new URL(url);
					urlConn = (HttpURLConnection) requestUrl.openConnection();
					urlConn.setRequestMethod("POST");
					urlConn.setConnectTimeout(mTimeout);
					urlConn.setReadTimeout(mTimeout);
					urlConn.setDoOutput(true);

					if (params != null) {
						urlConn.setRequestProperty("connection", "keep-alive");
						urlConn.setRequestProperty(
								"Content-Type",
								"multipart/form-data; boundary="
										+ params.boundaryString());
						MultipartEntity reqEntity = params.getMultiPart();
						reqEntity.writeTo(urlConn.getOutputStream());
					} else {
						urlConn.connect();
					}

					if (urlConn.getResponseCode() == HttpStatus.SC_OK) {
						resultString = readString(urlConn.getInputStream());
					} else {
						resultString = readString(urlConn.getErrorStream());
					}
					resultString = URLEncoder.encode(resultString, encode);
					urlConn.getInputStream().close();
					responseListener.sendSuccessMessage(
							MGHttpStatus.SUCCESS_CODE, resultString);
				} catch (Exception e) {
					e.printStackTrace();
					ZLogUtil.i(mContext,
							"[HTTP POST]:" + url + ",error��" + e.getMessage());
					// ����ʧ����Ϣ
					responseListener.sendFailureMessage(
							MGHttpStatus.UNTREATED_CODE, e.getMessage(),
							new MGHttpException(e));
				} finally {
					if (urlConn != null)
						urlConn.disconnect();

					responseListener.sendFinishMessage();
				}
			}
		});
	}

	/**
	 * ������д���ļ����ص�����.
	 *
	 * @param context
	 *            the context
	 * @param entity
	 *            the entity
	 * @param name
	 *            the name
	 * @param responseListener
	 *            the response listener
	 */
	public void writeResponseData(Context context, HttpEntity entity,
								  String name, MGFileHttpResponseListener responseListener) {

		if (entity == null) {
			return;
		}

		if (responseListener.getFile() == null) {
			// ���������ļ�
			responseListener.setFile(context, name);
		}

		InputStream inStream = null;
		FileOutputStream outStream = null;
		try {
			inStream = entity.getContent();
			long contentLength = entity.getContentLength();
			outStream = new FileOutputStream(responseListener.getFile());
			if (inStream != null) {

				byte[] tmp = new byte[4096];
				int l, count = 0;
				while ((l = inStream.read(tmp)) != -1
						&& !Thread.currentThread().isInterrupted()) {
					count += l;
					outStream.write(tmp, 0, l);
					responseListener.sendProgressMessage(count,
							(int) contentLength);
				}
			}
			responseListener.sendSuccessMessage(200);
		} catch (Exception e) {
			e.printStackTrace();
			// ����ʧ����Ϣ
			responseListener.sendFailureMessage(
					MGHttpStatus.RESPONSE_TIMEOUT_CODE,
					MGHttpException.SOCKET_TIMEOUT_EXCEPTION, e);
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
				if (outStream != null) {
					outStream.flush();
					outStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * ������ת��Ϊ�����Ʋ��ص�����.
	 *
	 * @param entity
	 *            the entity
	 * @param responseListener
	 *            the response listener
	 */
	public void readResponseData(HttpEntity entity,
								 MGBinaryHttpResponseListener responseListener) {

		if (entity == null) {
			return;
		}

		InputStream inStream = null;
		ByteArrayOutputStream outSteam = null;

		try {
			inStream = entity.getContent();
			outSteam = new ByteArrayOutputStream();
			long contentLength = entity.getContentLength();
			if (inStream != null) {
				int l, count = 0;
				byte[] tmp = new byte[4096];
				while ((l = inStream.read(tmp)) != -1) {
					count += l;
					outSteam.write(tmp, 0, l);
					responseListener.sendProgressMessage(count,
							(int) contentLength);

				}
			}
			responseListener.sendSuccessMessage(HttpStatus.SC_OK,
					outSteam.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			// ����ʧ����Ϣ
			responseListener.sendFailureMessage(
					MGHttpStatus.RESPONSE_TIMEOUT_CODE,
					MGHttpException.SOCKET_TIMEOUT_EXCEPTION, e);
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
				if (outSteam != null) {
					outSteam.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * �������������ӳ�ʱʱ��.
	 *
	 * @param timeout
	 *            ����
	 */
	public void setTimeout(int timeout) {
		this.mTimeout = timeout;
	}

	/**
	 * ? 2012 amsoft.cn ���ƣ�ResponderHandler.java ���������󷵻�
	 *
	 * @author ����һ����
	 * @version v1.0
	 * @date��2013-11-13 ����3:22:30
	 */
	private static class ResponderHandler extends Handler {

		/** ��Ӧ����. */
		private Object[] response;

		/** ��Ӧ��Ϣ����. */
		private MGHttpResponseListener responseListener;

		/**
		 * ��Ӧ��Ϣ����.
		 *
		 * @param responseListener
		 *            the response listener
		 */
		public ResponderHandler(MGHttpResponseListener responseListener) {
			this.responseListener = responseListener;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

				case SUCCESS_MESSAGE:
					response = (Object[]) msg.obj;

					if (response != null) {
						if (responseListener instanceof MGStringHttpResponseListener) {
							if (response.length >= 2) {
								((MGStringHttpResponseListener) responseListener)
										.onSuccess((Integer) response[0],
												(String) response[1]);
							} else {
								ZLogUtil.e(mContext, "SUCCESS_MESSAGE "
										+ MGHttpException.MISSING_PARAMETERS);
							}

						} else if (responseListener instanceof MGBinaryHttpResponseListener) {
							if (response.length >= 2) {
								((MGBinaryHttpResponseListener) responseListener)
										.onSuccess((Integer) response[0],
												(byte[]) response[1]);
							} else {
								ZLogUtil.e(mContext, "SUCCESS_MESSAGE "
										+ MGHttpException.MISSING_PARAMETERS);
							}
						} else if (responseListener instanceof MGFileHttpResponseListener) {

							if (response.length >= 1) {
								MGFileHttpResponseListener mAbFileHttpResponseListener = ((MGFileHttpResponseListener) responseListener);
								mAbFileHttpResponseListener.onSuccess(
										(Integer) response[0],
										mAbFileHttpResponseListener.getFile());
							} else {
								ZLogUtil.e(mContext, "SUCCESS_MESSAGE "
										+ MGHttpException.MISSING_PARAMETERS);
							}

						}
					}
					break;
				case FAILURE_MESSAGE:
					response = (Object[]) msg.obj;
					if (response != null && response.length >= 3) {
						// �쳣ת��Ϊ����ʾ��
						MGHttpException exception = new MGHttpException(
								(Exception) response[2]);
						responseListener.onFailure((Integer) response[0],
								(String) response[1], exception);
					} else {
						ZLogUtil.e(mContext, "FAILURE_MESSAGE "
								+ MGHttpException.MISSING_PARAMETERS);
					}
					break;
				case START_MESSAGE:
					responseListener.onStart();
					break;
				case FINISH_MESSAGE:
					responseListener.onFinish();
					break;
				case PROGRESS_MESSAGE:
					response = (Object[]) msg.obj;
					if (response != null && response.length >= 2) {
						responseListener.onProgress((Long) response[0],
								(Long) response[1]);
					} else {
						ZLogUtil.e(mContext, "PROGRESS_MESSAGE "
								+ MGHttpException.MISSING_PARAMETERS);
					}
					break;
				case RETRY_MESSAGE:
					responseListener.onRetry();
					break;
				default:
					break;
			}
		}

	}

	/**
	 * HTTP��������
	 *
	 * @return
	 */
	public BasicHttpParams getHttpParams() {

		BasicHttpParams httpParams = new BasicHttpParams();

		// ����ÿ��·�����������
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(30);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
		HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		// �����ӳ���ȡ���ӵĳ�ʱʱ�䣬����Ϊ1��
		ConnManagerParams.setTimeout(httpParams, mTimeout);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
				new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
		ConnManagerParams.setMaxTotalConnections(httpParams,
				DEFAULT_MAX_CONNECTIONS);
		// ����Ӧ���ݵĳ�ʱʱ��
		HttpConnectionParams.setSoTimeout(httpParams, mTimeout);
		HttpConnectionParams.setConnectionTimeout(httpParams, mTimeout);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams,
				DEFAULT_SOCKET_BUFFER_SIZE);

		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUserAgent(httpParams, userAgent);
		// Ĭ�ϲ���
		HttpClientParams.setRedirecting(httpParams, false);
		HttpClientParams.setCookiePolicy(httpParams,
				CookiePolicy.BROWSER_COMPATIBILITY);
		httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, null);
		return httpParams;

	}

	/**
	 * ��ȡHttpClient����ǩ����֤�飬�������ǩ���ο�AuthSSLProtocolSocketFactory��
	 *
	 * @return
	 */
	public DefaultHttpClient getHttpClient() {

		if (mHttpClient != null) {
			return mHttpClient;
		} else {
			return createHttpClient();
		}
	}

	/**
	 * ��ȡHttpClient����ǩ����֤�飬�������ǩ���ο�AuthSSLProtocolSocketFactory��
	 *
	 * @return
	 */
	public DefaultHttpClient createHttpClient() {
		BasicHttpParams httpParams = getHttpParams();
		// if (mIsOpenEasySSL) {
		// // ֧��https�� SSL��ǩ����ʵ����
		// EasySSLProtocolSocketFactory easySSLProtocolSocketFactory = new
		// EasySSLProtocolSocketFactory();
		// SchemeRegistry supportedSchemes = new SchemeRegistry();
		// SocketFactory socketFactory = PlainSocketFactory.getSocketFactory();
		// supportedSchemes.register(new Scheme("http", socketFactory, 80));
		// supportedSchemes.register(new Scheme("https",
		// easySSLProtocolSocketFactory, 443));
		// // ��ȫ��ThreadSafeClientConnManager���������õ�����HttpClient
		// ClientConnectionManager connectionManager = new
		// ThreadSafeClientConnManager(
		// httpParams, supportedSchemes);
		// // ȡ��HttpClient ThreadSafeClientConnManager
		// mHttpClient = new DefaultHttpClient(connectionManager, httpParams);
		// } else {
		// �̰߳�ȫ��HttpClient
		mHttpClient = new DefaultHttpClient(httpParams);
		// }
		// �Զ�����
		mHttpClient.setHttpRequestRetryHandler(mRequestRetryHandler);
		mHttpClient.setCookieStore(mCookieStore);
		return mHttpClient;
	}

	/**
	 * �Ƿ��ssl ��ǩ��
	 */
	public boolean isOpenEasySSL() {
		return mIsOpenEasySSL;
	}

	/**
	 * ��ssl ��ǩ��
	 *
	 * @param isOpenEasySSL
	 */
	public void setOpenEasySSL(boolean isOpenEasySSL) {
		this.mIsOpenEasySSL = isOpenEasySSL;
	}

	/**
	 * ʹ��ResponseHandler�ӿڴ�����Ӧ,֧���ض���
	 */
	private class RedirectionResponseHandler implements ResponseHandler<String> {

		private MGHttpResponseListener mResponseListener = null;
		private String mUrl = null;

		public RedirectionResponseHandler(String url,
										  MGHttpResponseListener responseListener) {
			super();
			this.mUrl = url;
			this.mResponseListener = responseListener;
		}

		@Override
		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			HttpUriRequest request = (HttpUriRequest) mHttpContext
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			// ����ɹ�
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			String responseBody = null;
			// 200ֱ�ӷ��ؽ��
			if (statusCode == HttpStatus.SC_OK) {

				// �������ȡresponse body
				// ����request��abort����
				// request.abort();

				if (entity != null) {
					if (mResponseListener instanceof MGStringHttpResponseListener) {
						// entity�е�����ֻ�ܶ�ȡһ��,����Content has been consumed
						// ���ѹ��Ҫ��ѹ
						Header header = entity.getContentEncoding();
						if (header != null) {
							String contentEncoding = header.getValue();
							if (contentEncoding != null) {
								if (contentEncoding.contains("gzip")) {
									entity = new MGGzipDecompressingEntity(
											entity);
								}
							}
						}
						String charset = EntityUtils.getContentCharSet(entity) == null ? encode
								: EntityUtils.getContentCharSet(entity);
						responseBody = new String(
								EntityUtils.toByteArray(entity), charset);

						((MGStringHttpResponseListener) mResponseListener)
								.sendSuccessMessage(statusCode, responseBody);
					} else if (mResponseListener instanceof MGBinaryHttpResponseListener) {
						responseBody = "Binary";
						readResponseData(
								entity,
								((MGBinaryHttpResponseListener) mResponseListener));
					} else if (mResponseListener instanceof MGFileHttpResponseListener) {
						// ��ȡ�ļ���
						String fileName = ZFileUtil.getCacheFileNameFromUrl(
								mUrl, response);
						writeResponseData(
								mContext,
								entity,
								fileName,
								((MGFileHttpResponseListener) mResponseListener));
					}
					// ��Դ�ͷ�!!!
					try {
						entity.consumeContent();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return responseBody;
				}

			}
			// 301 302�����ض�������
			else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY
					|| statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
				// ��ͷ��ȡ��ת��ĵ�ַ
				Header locationHeader = response.getLastHeader("location");
				String location = locationHeader.getValue();
				if (request.getMethod().equalsIgnoreCase(HTTP_POST)) {
					doPost(location, null, mResponseListener);
				} else if (request.getMethod().equalsIgnoreCase(HTTP_GET)) {
					doGet(location, null, mResponseListener);
				}
			} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
				// 404
				mResponseListener
						.sendFailureMessage(statusCode,
								MGHttpException.NOT_FOUND_EXCEPTION,
								new MGHttpException(
										MGHttpException.NOT_FOUND_EXCEPTION));
			} else if (statusCode == HttpStatus.SC_FORBIDDEN) {
				// 403
				mResponseListener
						.sendFailureMessage(statusCode,
								MGHttpException.FORBIDDEN_EXCEPTION,
								new MGHttpException(
										MGHttpException.FORBIDDEN_EXCEPTION));
			} else {
				mResponseListener.sendFailureMessage(statusCode,
						MGHttpException.REMOTE_SERVICE_EXCEPTION,
						new MGHttpException(
								MGHttpException.REMOTE_SERVICE_EXCEPTION));
			}
			return null;
		}
	}

	/**
	 * �Զ����Դ���
	 */
	private HttpRequestRetryHandler mRequestRetryHandler = new HttpRequestRetryHandler() {

		// �Զ���Ļָ�����
		public boolean retryRequest(IOException exception, int executionCount,
									HttpContext context) {
			// ���ûָ����ԣ��ڷ����쳣ʱ���Զ�����DEFAULT_MAX_RETRIES��
			if (executionCount >= DEFAULT_MAX_RETRIES) {
				// �������������Դ�������ô�Ͳ�Ҫ������
				ZLogUtil.d(mContext, "����������Դ�����������");
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// ������������������ӣ���ô������
				ZLogUtil.d(mContext, "���������������ӣ�����");
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// SSL�����쳣��������
				ZLogUtil.d(mContext, "ssl �쳣 ������");
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// ���������Ϊ���ݵȵģ���ô������
				ZLogUtil.d(mContext, "������Ϊ���ݵȵģ�����");
				return true;
			}
			if (exception != null) {
				return true;
			}
			return false;
		}
	};

	private String readString(InputStream is) {
		StringBuffer rst = new StringBuffer();

		byte[] buffer = new byte[1048576];
		int len = 0;

		try {
			while ((len = is.read(buffer)) > 0)
				for (int i = 0; i < len; ++i)
					rst.append((char) buffer[i]);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rst.toString();
	}

	/**
	 * ��ȡ�û�����
	 *
	 * @return
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * �����û�����
	 *
	 * @param userAgent
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * ��ȡ����
	 *
	 * @return
	 */
	public String getEncode() {
		return encode;
	}

	/**
	 * ���ñ���
	 *
	 * @param encode
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}

	/**
	 * �ر�HttpClient
	 */
	public void shutdown() {
		if (mHttpClient != null && mHttpClient.getConnectionManager() != null) {
			mHttpClient.getConnectionManager().shutdown();
		}
	}

	public CookieStore getCookieStore() {
		if (mHttpClient != null) {
			mCookieStore = mHttpClient.getCookieStore();
		}
		return mCookieStore;
	}

	public void setCookieStore(CookieStore cookieStore) {
		this.mCookieStore = cookieStore;
	}

}
