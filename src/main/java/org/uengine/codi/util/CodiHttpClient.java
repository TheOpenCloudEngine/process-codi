package org.uengine.codi.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
//import org.oce.webservices.sso.CasServerService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class CodiHttpClient implements Serializable, DisposableBean{

	public static final String RESULT_KEY = "result";

	/** Unique Id for serialization. */
	private static final long serialVersionUID = -5306738686476129516L;

	/** The default status codes we accept. */
	private static final int[] DEFAULT_ACCEPTABLE_CODES = new int[] {
		HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_NOT_MODIFIED,
		HttpURLConnection.HTTP_MOVED_TEMP, HttpURLConnection.HTTP_MOVED_PERM,
		HttpURLConnection.HTTP_ACCEPTED};

	private static final Logger log = Logger.getLogger(CodiHttpClient.class);

	private static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(100);

	/** List of HTTP status codes considered valid by this AuthenticationHandler. */
	@NotNull
	@Size(min=1)
	private int[] acceptableCodes = DEFAULT_ACCEPTABLE_CODES;

	@Min(0)
	private int connectionTimeout = 5000;

	@Min(0)
	private int readTimeout = 5000;

	private boolean followRedirects = true;


	/**
	 * Note that changing this executor will affect all httpClients.  While not ideal, this change was made because certain ticket registries
	 * were persisting the HttpClient and thus getting serializable exceptions.
	 * @param executorService
	 */
	public void setExecutorService(final ExecutorService executorService) {
		Assert.notNull(executorService);
		EXECUTOR_SERVICE = executorService;
	}

	public HashMap sendMessageToEndPoint(final String url, final HashMap<String, String> params, final String method) {
		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender(method, url, params, this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {
			return result.get();
		} catch (final Exception e) {
			return null;
		}
	}

	public boolean isValidEndPoint(final String url) {
		try {
			final URL u = new URL(url);
			return isValidEndPoint(u);
		} catch (final MalformedURLException e) {
			log.error(e.getMessage(),e);
			return false;
		}
	}

	public boolean isValidEndPoint(final URL url) {
		HttpURLConnection connection = null;
		InputStream is = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(this.connectionTimeout);
			connection.setReadTimeout(this.readTimeout);
			connection.setInstanceFollowRedirects(this.followRedirects);

			connection.connect();

			final int responseCode = connection.getResponseCode();

			for (final int acceptableCode : this.acceptableCodes) {
				if (responseCode == acceptableCode) {
					if (log.isDebugEnabled()) {
						log.debug("Response code from server matched " + responseCode + ".");
					}
					return true;
				}
			}

			if (log.isDebugEnabled()) {
				log.debug("Response Code did not match any of the acceptable response codes.  Code returned was " + responseCode);
			}

			// if the response code is an error and we don't find that error acceptable above:
			if (responseCode == 500) {
				is = connection.getInputStream();
				final String value = IOUtils.toString(is);
				log.error(String.format("There was an error contacting the endpoint: %s; The error was:\n%s", url.toExternalForm(), value));
			}
		} catch (final IOException e) {
			log.error(e.getMessage(),e);
		} finally {
			IOUtils.closeQuietly(is);
			if (connection != null) {
				connection.disconnect();
			}
		}
		return false;
	}

	/**
	 * Set the acceptable HTTP status codes that we will use to determine if the
	 * response from the URL was correct.
	 * 
	 * @param acceptableCodes an array of status code integers.
	 */
	public final void setAcceptableCodes(final int[] acceptableCodes) {
		this.acceptableCodes = acceptableCodes;
	}

	public void setConnectionTimeout(final int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setReadTimeout(final int readTimeout) {
		this.readTimeout = readTimeout;
	}

	/**
	 * Determines the behavior on receiving 3xx responses from HTTP endpoints.
	 *
	 * @param follow True to follow 3xx redirects (default), false otherwise.
	 */
	public void setFollowRedirects(final boolean follow) {
		this.followRedirects = follow;
	}

	public void destroy() throws Exception {
		EXECUTOR_SERVICE.shutdown();
	}

	private static final class MessageSender implements Callable<HashMap> {
		private String method;

		private String url;

		private HashMap<String, String> params;

		private int readTimeout;

		private int connectionTimeout;

		private boolean followRedirects;

		public MessageSender(final String method, final String url, final HashMap<String, String> params, final int readTimeout, final int connectionTimeout, final boolean followRedirects) {
			this.method = method;
			this.url = url;
			this.params = params;
			this.readTimeout = readTimeout;
			this.connectionTimeout = connectionTimeout;
			this.followRedirects = followRedirects;
		}

		public HashMap call() throws Exception {
			HttpURLConnection connection = null;
			BufferedReader in = null;
			HashMap mapResult = new HashMap();
			try {

				if (log.isDebugEnabled()) {
					log.debug("Attempting to access " + url);
				}
				final URL sendUrl = new URL(url);
				//                final String output = "logoutRequest=" + URLEncoder.encode(message, "UTF-8");

				boolean bFlag = false;
				StringBuffer sb = new StringBuffer();
				if(this.params != null){
					Iterator<String> iter = this.params.keySet().iterator();
					while(iter.hasNext()){
						if(!bFlag)
							bFlag = true;
						else
							sb.append("&");

						String key = iter.next();
						sb.append(URLEncoder.encode(key, "UTF-8"));
						sb.append("=");
						sb.append(URLEncoder.encode(this.params.get(key), "UTF-8"));
					}
				}

				connection = (HttpURLConnection) sendUrl.openConnection();

				if(this.method.equals("DELETE")){
					connection.setRequestMethod(this.method);
					connection.setRequestProperty("Accept", "application/json");
				}else{
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setRequestMethod(this.method);
					connection.setReadTimeout(this.readTimeout);
					connection.setConnectTimeout(this.connectionTimeout);
					connection.setInstanceFollowRedirects(this.followRedirects);
					connection.setRequestProperty("Content-Length", Integer.toString(sb.toString().getBytes().length));
					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					final DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
					printout.writeBytes(sb.toString());
					printout.flush();
					printout.close();
				}
				
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				if(connection.getResponseCode() == 200){
					StringBuffer sbRet = new StringBuffer();
					String line; 
					while ((line = in.readLine()) != null) {
						sbRet.append(line);			
					}
					mapResult.put(RESULT_KEY, sbRet.toString());
				}else if(connection.getResponseCode() == 201)
				{
					mapResult.put(RESULT_KEY, connection.getHeaderField("location"));
				}else{
					return null;
				}

				if (log.isDebugEnabled()) {
					log.debug("Finished sending message to" + url);
				}


				return mapResult;
			} catch (final SocketTimeoutException e) {
				e.printStackTrace();
				log.warn("Socket Timeout Detected while attempting to send message to [" + url + "].");
				return null;
			} catch (final Exception e) {
				e.printStackTrace();
				log.warn("Error Sending message to url endpoint [" + url + "].  Error is [" + e.getMessage() + "]");
				return null;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (final IOException e) {
						// can't do anything
					}
				}
				if (connection != null) {
					connection.disconnect();
				}
			}
		}

	}
}
