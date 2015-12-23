package me.wztc.api;

import android.annotation.SuppressLint;
import android.net.Uri;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import me.wztc.api.response.APIResponse;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.util.AppUtils;
import me.wztc.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.entity.StringEntity;

public abstract class CallAPI<RESPONSE extends APIResponse> {

    private static final String TAG = CallAPI.class.getSimpleName();

    private boolean isCancelled = false;

    protected static final String CHAR_SET = "utf-8";

//    protected static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
//    protected static final String HTTP_HEADER_CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    protected static final String HTTP_HEADER_CONTENT_TYPE_JSON = "application/json";

//    protected static final String HTTP_HEADER_VERSION = "version";
//    protected static final String HTTP_HEADER_OS = "os";
//    protected static final String HTTP_HEADER_OS_ANDROID = "android";
//
//    protected static final String HTTP_HEADER_DEVICE_NO = "X-DeviceNo";
//    protected static final String HTTP_HEADER_LNG = "X-Lng";
//    protected static final String HTTP_HEADER_LAT = "X-Lat";
//    protected static final String HTTP_HEADER_IP = "X-Ip";
    protected APIResponseHandler<RESPONSE> responseHandler;

    private ProgressListener progressListener;

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public CallAPI() {
    }

    @SuppressLint("NewApi")
    public void start(APIResponseHandler<RESPONSE> handler) {
        this.responseHandler = handler;
        doRequest();

    }

    protected abstract String getServiceSchema();

    protected abstract String getServiceHost();

    protected abstract HttpMethod getHttpMethod();

    protected int getServicePort() {
        return -1;
    }

    protected abstract String getServiceHostPath();

    protected int getServiceTimeout() {
        return 45000;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new LinkedHashMap<String, String>();
        // append default header here
//        headers.put(HTTP_HEADER_VERSION, DeviceInfo.getVersion());
//        headers.put(HTTP_HEADER_OS, HTTP_HEADER_OS_ANDROID);
//
//        headers.put(HTTP_HEADER_IP, DeviceInfo.getIp());
//        headers.put(HTTP_HEADER_DEVICE_NO, DeviceInfo.getDeviceId());
//        Location location = DeviceInfo.getLocation();
//        if (location != null) {
//            headers.put(HTTP_HEADER_LNG, String.valueOf(location.getLongitude()));
//            headers.put(HTTP_HEADER_LAT, String.valueOf(location.getLatitude()));
//        }
        return headers;
    }

    protected abstract String serviceComponent();

    private HashMap<String, String> getParameters() {
        HashMap<String, String> parameters = new LinkedHashMap<String, String>();
        return parameters;
    }

    protected RequestParams getRequestParams() {
        RequestParams params = new RequestParams();
        return params;
    }

    protected String getJsonParams() {
        return null;
    }

    public String getUrl() {
        StringBuilder requestString = new StringBuilder();

        requestString.append(getServiceSchema());
        requestString.append("://");

        requestString.append(getServiceHost());

        // port
        int port = getServicePort();
        if (port >= 0)
            requestString.append(":").append(String.valueOf(port));

        String path = getServiceHostPath();
        if (!AppUtils.isEmpty(path)) {
            requestString.append("/");
            requestString.append(path);
        }

        String serviceComponent = serviceComponent();
        if (!AppUtils.isEmpty(serviceComponent)) {
            requestString.append(serviceComponent);
        }

        boolean isFirst = true;
        HashMap<String, String> parameters = getParameters();
        appendParameters(requestString, parameters, isFirst, true);

        return requestString.toString();
    }

    private void appendParameters(StringBuilder requestString, Map<String, String> parameters, boolean initialFirst,
                                  boolean escape) {
        boolean first = initialFirst;
        // Append each of the parameters onto the URL string
        for (Entry<String, String> entry : parameters.entrySet()) {
            if (first) {
                requestString.append("?");
                first = false;
            } else {
                requestString.append("&");
            }

            if (escape) {
                requestString.append(Uri.encode(entry.getKey()) + "=" + Uri.encode(entry.getValue()));
            } else {
                requestString.append(entry.getKey() + "=" + entry.getValue());
            }
        }
    }

    protected void doRequest() {

        String url = getUrl();
        Logger.logI(TAG, url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());

        // set request header
        HashMap<String, String> headers = getHeaders();
        if (headers != null)
            for (Entry<String, String> header : headers.entrySet())
                client.addHeader(header.getKey(), header.getValue());

        // client.setMaxRetriesAndTimeout(3, getServiceTimeout());
        client.setTimeout(getServiceTimeout());
        if (getHttpMethod() == HttpMethod.get) {
            client.get(url, jsonHttpResponseHandler);
        } else {
            String jsonParams = getJsonParams();
            if (jsonParams == null) {
                RequestParams params = getRequestParams();
                Logger.logI("post body", params.toString());
                client.post(url, params, jsonHttpResponseHandler);
            } else {
                Logger.logI("post body", jsonParams);
                client.post(null, url, new StringEntity(jsonParams, CHAR_SET), HTTP_HEADER_CONTENT_TYPE_JSON,
                        jsonHttpResponseHandler);
            }

        }

    }

    private JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            if (isCancelled)
                return;

            Logger.logI("response", response.toString());
            if (statusCode == HttpStatus.SC_OK) {
                try {
                    JSONObject error = response.optJSONObject("error");

                    if (error == null)
                        onResponseReceived(response);
                    else
                        error(String.valueOf(statusCode), response);
                } catch (Exception e) {
                    e.printStackTrace();
                    error(String.valueOf(statusCode), response);
                }
            } else {
                error(String.valueOf(statusCode), response);
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            super.onSuccess(statusCode, headers, responseString);
            if (isCancelled)
                return;
            Logger.logI("responseString", responseString);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            if (isCancelled)
                return;
            error(String.valueOf(statusCode), errorResponse);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            if (isCancelled)
                return;
            Logger.logE("onFailure", responseString);
            try {
                error(String.valueOf(statusCode), new JSONObject(responseString));
            } catch (JSONException e) {
                error(String.valueOf(statusCode), null);
            }
        }

        @Override
        protected Object parseResponse(byte[] responseBody) throws JSONException {
            return super.parseResponse(responseBody);
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            if (isCancelled) {
                return;
            }
            if (progressListener != null) {
                progressListener.onProgress(bytesWritten, totalSize);
            }
        }

    };

    protected void success(int statusCode, JSONObject json) {
        try {
            String status = json.optString("status");

            if (status != null && status.equals("0"))
                onResponseReceived(json);
            else
                error(String.valueOf(statusCode), json);
        } catch (Exception e) {
            e.printStackTrace();
            error(String.valueOf(statusCode), json);
        }
    }

    protected void error(String errorCode, JSONObject json) {

        try {
            Logger.logE("json", json.toString());
            JSONObject errorJson = json.getJSONObject("error");
            responseError(errorJson.getString("errCode"), errorJson.getString("message"));
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            responseError(errorCode, null);
        }

    }

    protected void responseError(String code, String data) {
        if (!isCancelled) {
            if (responseHandler != null)
                responseHandler.handleError(code, data);
        }
    }

    protected abstract void onResponseReceived(JSONObject json) throws JSONException;

    public interface ProgressListener {
        void onProgress(long bytesWritten, long totalSize);
    }

}