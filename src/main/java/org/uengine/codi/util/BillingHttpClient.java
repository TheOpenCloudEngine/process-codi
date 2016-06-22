package org.uengine.codi.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;
import org.uengine.codi.mw3.billing.model.*;
import org.uengine.kernel.GlobalContext;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * For Billing System (e.g. KillBIll) RESTFul Communication
 */
public class BillingHttpClient implements Serializable, DisposableBean{

	public static final String RESULT_KEY = "result";

	public static final String BILLING_SYSTEM_DOMAIN = GlobalContext.getPropertyString("billing.system.domain", "http://192.168.0.2:8080");
	public static final String BILLING_SYSTEM_USER = GlobalContext.getPropertyString("billing.system.admin", "addmin");
	public static final String BILLING_SYSTEM_PASSWORD = GlobalContext.getPropertyString("billing.system.password", "password");
	public static final String BILLING_SYSTEM_APIKEY = GlobalContext.getPropertyString("billing.system.apikey", "essencia");
	public static final String BILLING_SYSTEM_APISECRET = GlobalContext.getPropertyString("billing.system.apisecret", "uengine");

	/** The default status codes we accept. */
	private static final int[] DEFAULT_ACCEPTABLE_CODES = new int[] {
			HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_NOT_MODIFIED,
			HttpURLConnection.HTTP_MOVED_TEMP, HttpURLConnection.HTTP_MOVED_PERM,
			HttpURLConnection.HTTP_ACCEPTED};

	/** Unique Id for serialization. */
	private static final long serialVersionUID = 1057417041509767387L;

	private static final Logger log = Logger.getLogger(BillingHttpClient.class);

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

	public HashMap sendMessageToEndPoint(final String url, final String params, final String method, final String createUser) {

		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender(method, url, createUser, params, this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {
			return result.get();
		} catch (final Exception e) {
			return null;
		}
	}

	public AccountTimeline getAccountTimeline(final String accountId) {

		final String url = BILLING_SYSTEM_DOMAIN + BillingResource.ACCOUNTS_PATH + "/" + accountId + "/" + BillingResource.TIMELINE+"?audit=FULL";

		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender("GET", url, "", "", this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {

			HashMap resultMap = result.get();
			String resultString = (String)resultMap.get(BillingHttpClient.RESULT_KEY);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<AccountTimeline> typeRef = new TypeReference<AccountTimeline>() {};
			AccountTimeline resultAccountTimeline = mapper.readValue(resultString, typeRef);
			return resultAccountTimeline;

		} catch (final Exception e) {
			return null;
		}
	}

	public Subscriptions cancelSubscription(final String subscriptonId, final String createUser) {

		final String url = BILLING_SYSTEM_DOMAIN + BillingResource.SUBSCRIPTIONS_PATH + "/" + subscriptonId;

		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender("DELETE", url, createUser, "", this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {

			HashMap resultMap = result.get();
			String resultString = (String)resultMap.get(BillingHttpClient.RESULT_KEY);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Subscriptions> typeRef = new TypeReference<Subscriptions>() {};
			Subscriptions subscription = mapper.readValue(resultString, typeRef);
			return subscription;

		} catch (final Exception e) {
			return null;
		}
	}

	public Subscriptions updateSubscription(final Subscriptions subscription, final String createUser) {

		final String url = BILLING_SYSTEM_DOMAIN + BillingResource.SUBSCRIPTIONS_PATH + "/" + subscription.getSubscriptionId().toString();

		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender("PUT", url, createUser, subscription, this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {

			HashMap resultMap = result.get();
			String resultString = (String)resultMap.get(BillingHttpClient.RESULT_KEY);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Subscriptions> typeRef = new TypeReference<Subscriptions>() {};
			Subscriptions resultSubscription = mapper.readValue(resultString, typeRef);
			return resultSubscription;

		} catch (final Exception e) {
			return null;
		}
	}

	public Subscriptions getSubscription(final String subscriptionId) {

		final String url = BILLING_SYSTEM_DOMAIN + BillingResource.SUBSCRIPTIONS_PATH + "/" +subscriptionId;

		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender("GET", url, "", "", this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {

			HashMap resultMap = result.get();
			String resultString = (String)resultMap.get(BillingHttpClient.RESULT_KEY);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Subscriptions> typeRef = new TypeReference<Subscriptions>() {};
			Subscriptions subscription = mapper.readValue(resultString, typeRef);
			return subscription;

		} catch (final Exception e) {
			return null;
		}
	}

	public List<Invoice> getInvoicesForAccount(final String accountId) {

		final String url = BILLING_SYSTEM_DOMAIN + BillingResource.ACCOUNTS_PATH + "/" +accountId + "/" + BillingResource.INVOICES;

		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender("GET", url, "", "", this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {

			HashMap resultMap = result.get();
			String resultString = (String)resultMap.get(BillingHttpClient.RESULT_KEY);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Invoice>> typeRef = new TypeReference<List<Invoice>>() {};
			List<Invoice> invoiceList = mapper.readValue(resultString, typeRef);
			return invoiceList;

		} catch (final Exception e) {
			return null;
		}
	}

	public List<Payment> getPaymentForAccount(final String accountId) {

		final String url = BILLING_SYSTEM_DOMAIN + BillingResource.ACCOUNTS_PATH + "/" +accountId + "/" + BillingResource.PAYMENTS+"?audit=FULL";

		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender("GET", url, "", "", this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {

			HashMap resultMap = result.get();
			String resultString = (String)resultMap.get(BillingHttpClient.RESULT_KEY);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Payment>> typeRef = new TypeReference<List<Payment>>() {};
			List<Payment> paymentList = mapper.readValue(resultString, typeRef);
			return paymentList;

		} catch (final Exception e) {
			return null;
		}
	}

	public Invoice getInvoiceById(final String invoiceId) {

		final String url = BILLING_SYSTEM_DOMAIN + BillingResource.INVOICES_PATH + "/" + invoiceId;

		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender("GET", url, "", "", this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {

			HashMap resultMap = result.get();
			String resultString = (String)resultMap.get(BillingHttpClient.RESULT_KEY);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Invoice> typeRef = new TypeReference<Invoice>() {};
			Invoice resultInvoice = mapper.readValue(resultString, typeRef);
			return resultInvoice;

		} catch (final Exception e) {
			return null;
		}
	}

	public Account createAccount(final Account account, final String createUser) {

		final String url = BILLING_SYSTEM_DOMAIN + BillingResource.ACCOUNTS_PATH;

		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender("POST", url, createUser, account, this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {

			HashMap resultMap = result.get();
			String resultString = (String)resultMap.get(BillingHttpClient.RESULT_KEY);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Account> typeRef = new TypeReference<Account>() {};
			Account resultAccount = mapper.readValue(resultString, typeRef);
			return resultAccount;

		} catch (final Exception e) {
			return null;
		}
	}

	public RolledUpUsage getRolledUpUsage(final String subscriptionId, final String unitType, final String startDate, final String endDate) {

		final String url = BILLING_SYSTEM_DOMAIN + BillingResource.USAGES_PATH + "/" +subscriptionId + "/"+unitType + "?startDate="+startDate+"&endDate="+endDate;


		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender("GET", url, "", "", this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {

			HashMap resultMap = result.get();
			String resultString = (String)resultMap.get(BillingHttpClient.RESULT_KEY);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<RolledUpUsage> typeRef = new TypeReference<RolledUpUsage>() {};
			RolledUpUsage rolledUpUsage = mapper.readValue(resultString, typeRef);
			return rolledUpUsage;

		} catch (final Exception e) {
			return null;
		}
	}

	public SubscriptionUsageRecord createSubscriptionUsageRecord(final SubscriptionUsageRecord subscriptionUsageRecord, final String createUser) {

		final String url = BILLING_SYSTEM_DOMAIN + BillingResource.USAGES_PATH;

		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender("POST", url, createUser, subscriptionUsageRecord, this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {

			HashMap resultMap = result.get();
			String resultString = (String)resultMap.get(BillingHttpClient.RESULT_KEY);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<SubscriptionUsageRecord> typeRef = new TypeReference<SubscriptionUsageRecord>() {};
			SubscriptionUsageRecord resultSubscriptionUsageRecord = mapper.readValue(resultString, typeRef);
			return resultSubscriptionUsageRecord;

		} catch (final Exception e) {
			return null;
		}
	}

	public Subscriptions createSubscription(final Subscriptions subscription, final String createUser) {

		final String url = BILLING_SYSTEM_DOMAIN + BillingResource.SUBSCRIPTIONS_PATH;

		final Future<HashMap> result = EXECUTOR_SERVICE.submit(new MessageSender("POST", url, createUser, subscription, this.readTimeout, this.connectionTimeout, this.followRedirects));

		//        if (async) {
		//            return new HashMap();
		//        }

		try {

			HashMap resultMap = result.get();
			String resultString = (String)resultMap.get(BillingHttpClient.RESULT_KEY);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Subscriptions> typeRef = new TypeReference<Subscriptions>() {};
			Subscriptions resultSubscription = mapper.readValue(resultString, typeRef);
			return resultSubscription;

		} catch (final Exception e) {
			return null;
		}
	}

	public boolean isValidEndPoint(final String url) {
		try {
			final URL u = new URL(url);
			return isValidEndPoint(u);
		} catch (final MalformedURLException e) {
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
					return true;
				}
			}

			// if the response code is an error and we don't find that error acceptable above:
			if (responseCode == 500) {
				is = connection.getInputStream();
				final String value = IOUtils.toString(is);
			}
		} catch (final IOException e) {
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

		private String createUser;

		private Object params;

		private int readTimeout;

		private int connectionTimeout;

		private boolean followRedirects;

		public MessageSender(final String method, final String url, final String createUser, final Object params, final int readTimeout, final int connectionTimeout, final boolean followRedirects) {
			this.method = method;
			this.url = url;
			this.createUser = createUser;
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

				final URL sendUrl = new URL(url);

				connection = (HttpURLConnection) sendUrl.openConnection();

				if(this.method.equals("DELETE")){
					connection.setRequestMethod(this.method);
					connection.setRequestProperty("Accept", "application/json");
					connection.setReadTimeout(this.readTimeout);
					connection.setConnectTimeout(this.connectionTimeout);
					connection.setInstanceFollowRedirects(this.followRedirects);
					connection.setRequestProperty("Content-Type", "application/json");
					String userSetting = BILLING_SYSTEM_USER + ":" + BILLING_SYSTEM_PASSWORD;
					String basicAuth = "Basic " + new String(new Base64().encode(userSetting.getBytes())).replaceAll("\n", "");
					connection.setRequestProperty(BillingResource.AUTHORIZATION, basicAuth);
					connection.setRequestProperty(BillingResource.HDR_API_KEY, BILLING_SYSTEM_APIKEY);
					connection.setRequestProperty(BillingResource.HDR_API_SECRET, BILLING_SYSTEM_APISECRET);
					connection.setRequestProperty(BillingResource.HDR_CREATED_BY, this.createUser);
				} else if(this.method.equals("GET")) {
					connection.setRequestMethod(this.method);
					connection.setReadTimeout(this.readTimeout);
					connection.setConnectTimeout(this.connectionTimeout);
					connection.setInstanceFollowRedirects(this.followRedirects);
					connection.setRequestProperty("Content-Type", "application/json");
					String userSetting = BILLING_SYSTEM_USER + ":" + BILLING_SYSTEM_PASSWORD;
					String basicAuth = "Basic " + new String(new Base64().encode(userSetting.getBytes())).replaceAll("\n", "");
					connection.setRequestProperty(BillingResource.AUTHORIZATION, basicAuth);
					connection.setRequestProperty(BillingResource.HDR_API_KEY, BILLING_SYSTEM_APIKEY);
					connection.setRequestProperty(BillingResource.HDR_API_SECRET, BILLING_SYSTEM_APISECRET);
				} else {
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setRequestMethod(this.method);
					connection.setReadTimeout(this.readTimeout);
					connection.setConnectTimeout(this.connectionTimeout);
					connection.setInstanceFollowRedirects(this.followRedirects);
					connection.setRequestProperty("Content-Type", "application/json");
					String userSetting = BILLING_SYSTEM_USER + ":" + BILLING_SYSTEM_PASSWORD;
					String basicAuth = "Basic " + new String(new Base64().encode(userSetting.getBytes())).replaceAll("\n", "");
					connection.setRequestProperty(BillingResource.AUTHORIZATION, basicAuth);
					connection.setRequestProperty(BillingResource.HDR_API_KEY, BILLING_SYSTEM_APIKEY);
					connection.setRequestProperty(BillingResource.HDR_API_SECRET, BILLING_SYSTEM_APISECRET);
					connection.setRequestProperty(BillingResource.HDR_CREATED_BY, createUser);
					final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

					ObjectMapper mapper = new ObjectMapper();
					String paramsString = mapper.writeValueAsString(params);

					wr.writeBytes(paramsString);
					wr.flush();
					wr.close();
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
					String location = connection.getHeaderField("location");
					if(location != null && !"".equals(location)) {
						BillingHttpClient locationHClient = new BillingHttpClient();
						mapResult = locationHClient.sendMessageToEndPoint(location, null, "GET", createUser);
					}
				}else{
					return null;
				}

				return mapResult;
			} catch (final SocketTimeoutException e) {
				e.printStackTrace();
				return null;
			} catch (final Exception e) {
				e.printStackTrace();
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