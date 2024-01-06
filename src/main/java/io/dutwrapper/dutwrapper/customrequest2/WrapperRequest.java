package io.dutwrapper.dutwrapper.customrequest2;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WrapperRequest {
    private static String getSessionIdFromCookieHeader(String cookieHeader) {
        if (cookieHeader != null) {
            String splitChar;
            if (cookieHeader.contains("; "))
                splitChar = "; ";
            else
                splitChar = ";";

            String[] cookieHeaderSplit = cookieHeader.split(splitChar);
            for (String item : cookieHeaderSplit) {
                if (item.contains("ASP.NET_SessionId")) {
                    String[] sessionIdSplit = item.split("=");
                    return sessionIdSplit[1];
                }
            }
        }

        return null;
    }

    public static WrapperResponse get(
            String url,
            @Nullable ArrayList<WrapperRequestHeader> headers,
            @Nullable Integer timeout) {
        try {
            if (timeout == null)
                timeout = 60;
            if (url == null)
                throw new NullPointerException("url cannot be null.");

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .build();

            Request.Builder requestBuilder = new Request.Builder().url(url)
                    .addHeader("Content-Type", "text/html; charset=utf-8");
            if (headers != null) {
                for (WrapperRequestHeader headerItem : headers) {
                    requestBuilder.addHeader(headerItem.getName(), headerItem.getValue());
                }
            }
            Request request = requestBuilder.get().build();
            Response response = client.newCall(request).execute();

            WrapperResponse result = new WrapperResponse(
                    response.code(),
                    response.body().string(),
                    null,
                    response.message(),
                    getSessionIdFromCookieHeader(response.headers().get("Set-Cookie")));

            response.close();
            return result;
        } catch (NullPointerException nullEx) {
            return new WrapperResponse(
                    null, null, nullEx, nullEx.getMessage(), null);
        } catch (Exception ex) {
            return new WrapperResponse(
                    null, null, ex, ex.getMessage(), null);
        }
    }

    public static WrapperResponse post(
            String url,
            byte[] requestBody,
            @Nullable ArrayList<WrapperRequestHeader> headers,
            @Nullable Integer timeout) {
        try {
            if (timeout == null)
                timeout = 60;
            if (url == null)
                throw new NullPointerException("url cannot be null.");
            if (requestBody == null)
                throw new NullPointerException("requestBody cannot be null.");

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .build();

            RequestBody body = RequestBody.create(requestBody);

            Request.Builder requestBuilder = new Request.Builder().url(url)
                    .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
            if (headers != null) {
                for (WrapperRequestHeader headerItem : headers) {
                    requestBuilder.addHeader(headerItem.getName(), headerItem.getValue());
                }
            }
            Request request = requestBuilder.post(body).build();
            Response response = client.newCall(request).execute();

            WrapperResponse result = new WrapperResponse(
                    response.code(),
                    response.body().string(),
                    null,
                    response.message(),
                    getSessionIdFromCookieHeader(response.headers().get("Set-Cookie")));

            response.close();
            return result;
        } catch (NullPointerException nullEx) {
            return new WrapperResponse(
                    null, null, nullEx, nullEx.getMessage(), null);
        } catch (Exception ex) {
            return new WrapperResponse(
                    null, null, ex, ex.getMessage(), null);
        }
    }
}
