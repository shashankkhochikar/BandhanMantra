package com.bandhan.mantra.volley;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.bandhan.mantra.BandhanMantraApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;


/**
 * Basic request using Gson's reflection.
 * Usable for authorized and unauthorized requests.
 * Allowing for an optional {@link String} to be passed in as part of the request body.
 *
 * @param <T> object which is serializable from request.
 */
public class GsonRequest<T> extends Request<T> {
    /**
     * Default charset for JSON request.
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Class of object which is serializable from request.
     */
    private final Class<T> clazz;
    /**
     * User token for authorized requests.
     */
    private final Long accessToken;
    private final String requestBody;
    private final Response.Listener<T> successListener;
    private String requestUrl;
    private int requestStatusCode;
    private FragmentManager fragmentManager;
    private static Gson gson;

    /**
     * Create a new authorized request.
     *
     * @param method          The HTTP method to use
     * @param requestUrl      URL to fetch the JSON from
     * @param requestBody     A {@link String} to post with the request. Null is allowed and
     *                        indicates no parameters will be posted along with request.
     * @param clazz           Relevant class object, for Gson's reflection
     * @param successListener Listener to retrieve successful response
     * @param errorListener   Error listener, or null to ignore errors
     * @param fragmentManager Manager to create re-login dialog on HTTP status 403. Null is allowed.
     * @param accessToken     Token identifying user used for user specific requests.
     */
    public GsonRequest(int method, String requestUrl, String requestBody, Class<T> clazz, Response.Listener<T> successListener,
                       Response.ErrorListener errorListener, FragmentManager fragmentManager, Long accessToken) {
        super(method, requestUrl, errorListener);
        this.clazz = clazz;
        this.requestUrl = requestUrl;
        this.requestBody = requestBody;
        this.successListener = successListener;
        this.fragmentManager = fragmentManager;
        this.accessToken = accessToken;
    }

    /**
     * Create a new unauthorized request.
     *
     * @param method          The HTTP method to use
     * @param requestUrl      URL to fetch the JSON from
     * @param requestBody     A {@link String} to post with the request. Null is allowed and
     *                        indicates no parameters will be posted along with request.
     * @param clazz           Relevant class object, for Gson's reflection
     * @param successListener Listener to retrieve successful response
     * @param errorListener   Error listener, or null to ignore errors
     */
    public GsonRequest(int method, String requestUrl, String requestBody, Class<T> clazz, Response.Listener<T> successListener,
                       Response.ErrorListener errorListener) {
        this(method, requestUrl, requestBody, clazz, successListener, errorListener, null, null);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Client-Version", BandhanMantraApp.APP_VERSION);
        headers.put("Device-Token", BandhanMantraApp.ANDROID_ID);

        // Determine if request should be authorized.
        if (accessToken != null && accessToken != 0) {
            String credentials = accessToken + ":";
            String encodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", "Basic " + encodedCredentials);
        }
        return headers;
    }

    @Override
    public byte[] getBody() {
        try {
            return requestBody == null ? null : requestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, PROTOCOL_CHARSET);
            return null;
        }
    }

    @Override
    protected void deliverResponse(@NonNull T response) {
        successListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            requestStatusCode = response.statusCode;

            // Parse response and return obtained object
            String json = new String(response.data, PROTOCOL_CHARSET);
            T result =getGsonParser().fromJson(json, clazz);
            if (result == null) return Response.error(new ParseError(new NullPointerException()));
            else return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError.networkResponse != null) {
            // Save request status code
            requestStatusCode = volleyError.networkResponse.statusCode;

            // If AccessToken expired. Logout user and redirect to home page.
            if (getStatusCode() == HttpURLConnection.HTTP_FORBIDDEN && fragmentManager != null) {
//                SettingsMy.setActiveUser(null);

            }
        } else {
            requestStatusCode = 9999;
        }
        return super.parseNetworkError(volleyError);
    }

    /**
     * Method returns result statusCode of invoked request.
     *
     * @return HTTP standard code.
     */
    public int getStatusCode() {
        return requestStatusCode;
    }

    /**
     * Add specific parsing to gson
     *
     * @return new instance of {@link Gson}
     */
    public static Gson getGsonParser() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            //gsonBuilder.registerTypeAdapter(Filters.class, new DeserializerFilters());
            gson = gsonBuilder.create();
        }
        return gson;
    }
}