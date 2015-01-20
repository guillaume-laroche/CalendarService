package fr.axin.gservices.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public final class GoogleAuth {

	/**
	 * Please provide a value for the CLIENT_ID constant before proceeding, set
	 * this up at https://code.google.com/apis/console/
	 */
	private static String CLIENT_ID;
	/**
	 * Please provide a value for the CLIENT_SECRET constant before proceeding,
	 * set this up at https://code.google.com/apis/console/
	 */
	private static String CLIENT_SECRET;

	private static String CLIENT_CODE;

	private static String CLIENT_EMAIL;

	/**
	 * Callback URI that google will redirect to after successful authentication
	 */
	private static String CALLBACK_URI;

	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static HttpTransport HTTP_TRANSPORT = null;

	private static String stateToken;

	private static GoogleAuthorizationCodeFlow flow;

	private static Set<String> scopes;
	
	private static Calendar client;
	
	private static Credential credential;
	private static GoogleTokenResponse response;	
	private static boolean service;	
	
	private static String p12File;

	/**
	 * Constructor initializes the Google Authorization Code Flow with CLIENT
	 * ID, SECRET, and SCOPE
	 * 
	 * @throws IOException
	 */
	public GoogleAuth(String redirect, String httpHost, String clientId, String clientSecret)
			throws IOException {
		HTTP_TRANSPORT = new NetHttpTransport();
		service = false;
		CLIENT_ID = clientId;
		CLIENT_SECRET = clientSecret;
		CALLBACK_URI = httpHost + "/CalendarService/" + redirect + ".jsp";
		scopes = new HashSet<String>();
		scopes.add(CalendarScopes.CALENDAR);
		scopes.add(CalendarScopes.CALENDAR_READONLY);
		scopes.add(Oauth2Scopes.USERINFO_EMAIL);		
		
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
				JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, scopes).build();	
		
		generateStateToken();
	}

	public GoogleAuth(String accountId, String p12file)
			throws IOException, GeneralSecurityException {
		HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		service = true;
		scopes = new HashSet<String>();
		scopes.add(CalendarScopes.CALENDAR);
		scopes.add(CalendarScopes.CALENDAR_READONLY);
		scopes.add(Oauth2Scopes.USERINFO_EMAIL);		
		p12File = p12file;
		CLIENT_ID = accountId;
	}
	
	public static String getClientId() {
		return CLIENT_ID;
	}

	public static void setCode(String c) {
		CLIENT_CODE = c;
	}

	public static String getCode() {
		return CLIENT_CODE;
	}

	public static Calendar getClient() {
		return client;
	}

	/**
	 * Builds a login URL based on client ID, secret, callback URI, and scope
	 */
	public static String buildLoginUrl() {

		final GoogleAuthorizationCodeRequestUrl url = flow
				.newAuthorizationUrl();

		return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
	}

	/**
	 * Generates a secure state token
	 */
	private static void generateStateToken() {

		SecureRandom sr1 = new SecureRandom();

		stateToken = "google;" + sr1.nextInt();

	}

	/**
	 * Accessor for state token
	 */
	public static String getStateToken() {
		return stateToken;
	}

	public static String getEmail() {
		return CLIENT_EMAIL;
	}

	public static void setCredentialWeb() throws IOException {					
		response = flow.newTokenRequest(CLIENT_CODE).setRedirectUri(CALLBACK_URI).execute();	
		credential = flow.createAndStoreCredential(response, null);
	}
		
	public static void setCredentialService() throws IOException, GeneralSecurityException {			
	      credential = new GoogleCredential.Builder()
	      		.setTransport(HTTP_TRANSPORT)
	              .setJsonFactory(JSON_FACTORY)
	              .setServiceAccountId(CLIENT_ID)
	              .setServiceAccountScopes(scopes)
	              .setServiceAccountPrivateKeyFromP12File(new File(p12File))
	              .build();		
	}
	
	public static void createCalendarInstance() throws IOException {
		  		
		if(!service)
			client = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.build();		
		else 
			client = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("CalendarService").build();		
						
		
		CLIENT_EMAIL = new Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				credential).build().userinfo().get().execute().getEmail();

	}

}