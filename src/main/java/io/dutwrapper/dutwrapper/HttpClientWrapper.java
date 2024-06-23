package io.dutwrapper.dutwrapper;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpClientWrapper {
    public static class Header {
        private String name;
        private String value;

        public Header(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static class Response {
        private Integer statusCode;
        private @Nullable String content;
        private @Nullable Exception exception;
        private @Nullable String message;
        private @Nullable String sessionId;

        public Response() {
        }

        public Response(@Nullable Integer statusCode, @Nullable String content, @Nullable Exception exception,
                @Nullable String message, @Nullable String sessionId) {
            this.content = content;
            this.exception = exception;
            this.message = message;
            this.sessionId = sessionId;
            if (statusCode == null) {
                this.statusCode = 0;
            } else {
                this.statusCode = statusCode;
            }
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public @Nullable String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public @Nullable Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

        public @Nullable String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public @Nullable String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public void throwExceptionIfExist() throws Exception, IOException {
            if (exception != null) throw exception;
        }
    }

    private static @Nullable String getSessionIdFromHeader(Headers header) {
        try {
            // Get cookie
            @Nullable
            String cookie = null;
            String[] cookieKey = { "Set-Cookie", "set-cookie", "Cookie", "cookie" };
            for (String key : cookieKey) {
                @Nullable
                String temp = header.get(key);
                if (temp != null) {
                    cookie = temp;
                    break;
                }
            }
            if (cookie == null) {
                throw new Exception("No cookie in header!");
            }

            // Get ASP.NET_SessionId from cookie
            String splitChar = cookie.contains("; ") ? "; " : ";";
            String[] cookieHeaderSplit = cookie.split(splitChar);
            for (String item : cookieHeaderSplit) {
                if (item.contains("ASP.NET_SessionId")) {
                    String[] sessionIdSplit = item.split("=");
                    return sessionIdSplit[1];
                }
            }
            throw new Exception("Not found session id in cookie!");
            // https://stackoverflow.com/questions/4959859/why-is-unknownhostexception-not-caught-in-exception-java
        } catch (UnknownHostException uheEx) {
            uheEx.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Response get(
            String url,
            @Nullable List<Header> headers,
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
                    .addHeader("origin", "http://sv.dut.udn.vn")
                    .addHeader("referer", "http://sv.dut.udn.vn/PageDangNhap.aspx")
                    .addHeader("content-type", "text/html; charset=utf-8")
                    .addHeader("user-agent", Variables.USER_AGENT);
            if (headers != null) {
                for (Header headerItem : headers) {
                    requestBuilder.addHeader(headerItem.getName(), headerItem.getValue());
                }
            }
            Request request = requestBuilder.get().build();
            okhttp3.Response response = client.newCall(request).execute();

            Response result = new Response(
                    response.code(),
                    response.body().string(),
                    null,
                    response.message(),
                    getSessionIdFromHeader(response.headers()));

            response.close();
            return result;
        } catch (NullPointerException nullEx) {
            return new Response(
                    null, null, nullEx, nullEx.getMessage(), null);
        } catch (UnknownHostException uheEx) {
            uheEx.printStackTrace();
            return new Response(
                    null, null, uheEx, uheEx.getMessage(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Response(
                    null, null, ex, ex.getMessage(), null);
        }
    }

    public static Response post(
            String url,
            byte[] requestBody,
            @Nullable List<Header> headers,
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
                    .addHeader("origin", "http://sv.dut.udn.vn")
                    .addHeader("referer", "http://sv.dut.udn.vn/PageDangNhap.aspx")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("user-agent", Variables.USER_AGENT);
            if (headers != null) {
                for (Header headerItem : headers) {
                    requestBuilder.addHeader(headerItem.getName(), headerItem.getValue());
                }
            }
            Request request = requestBuilder.post(body).build();
            okhttp3.Response response = client.newCall(request).execute();

            Response result = new Response(
                    response.code(),
                    response.body().string(),
                    null,
                    response.message(),
                    getSessionIdFromHeader(response.headers()));

            response.close();
            return result;
        } catch (UnknownHostException uheEx) {
            uheEx.printStackTrace();
            return new Response(
                    null, null, uheEx, uheEx.getMessage(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Response(
                    null, null, ex, ex.getMessage(), null);
        }
    }
}
