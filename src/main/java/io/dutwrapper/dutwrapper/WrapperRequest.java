package io.dutwrapper.dutwrapper;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;
import java.util.HashMap;

public class WrapperRequest {
    public static class Response {
        private @Nullable Integer statusCode;
        private @Nullable String content;
        private @Nullable Exception exception;
        private @Nullable String message;
        private Map<String, String> header;

        public Response(
                @Nullable Integer statusCode,
                @Nullable String content,
                @Nullable Exception exception,
                @Nullable String message,
                @Nullable Map<String, String> header) {
            this.statusCode = statusCode;
            this.content = content;
            this.exception = exception;
            this.message = message;
            this.header = header == null ? new HashMap<String, String>() : header;
        }

        public Integer getStatusCode() {
            return statusCode == null ? 0 : statusCode;
        }

        public @Nullable String getContent() {
            return content;
        }

        public @Nullable Exception getException() {
            return exception;
        }

        public @Nullable String getMessage() {
            return message;
        }

        public Map<String, String> getHeader() {
            return header;
        }
    }

    public static class RequestGet {
        private Map<String, String> header;
        private String url;
        private Integer timeout;

        public RequestGet(
                String url,
                @Nullable Map<String, String> header,
                @Nullable Integer timeout) {
            this.url = url;
            this.header = header == null ? new HashMap<>() : header;
            this.timeout = timeout == null ? 60 : timeout;
        }

        public Map<String, String> getHeader() {
            return header;
        }

        public String getUrl() {
            return url;
        }

        public Integer getTimeout() {
            return timeout;
        }

    }

    public static class RequestPost extends RequestGet {
        private @Nullable byte[] requestBody;

        public RequestPost(
                String url,
                @Nullable byte[] requestBody,
                @Nullable Map<String, String> header,
                @Nullable Integer timeout) {
            super(url, header, timeout);
            this.requestBody = requestBody;
        }

        public @Nullable byte[] getRequestBody() {
            return requestBody;
        }
    }

    public static Response get(RequestGet request) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(request.getTimeout(), TimeUnit.SECONDS)
                    .readTimeout(request.getTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(request.getTimeout(), TimeUnit.SECONDS)
                    .build();

            Request.Builder requestBuilder = new Request.Builder().url(request.getUrl());
            request.getHeader().forEach((k, v) -> {
                requestBuilder.addHeader(k, v);
            });
            if (!request.getHeader().containsKey("Content-Type")) {
                requestBuilder.addHeader("Content-Type", "text/html; charset=utf-8");
            }

            Request request1 = requestBuilder.get().build();
            okhttp3.Response response = client.newCall(request1).execute();
            Map<String, String> setHeader = new HashMap<>();
            response.headers().toMultimap().forEach((k, v) -> {
                setHeader.put(k, String.join("; ", v));
            });

            Response result = new Response(
                    response.code(),
                    response.body().string(),
                    null,
                    response.message(),
                    setHeader);

            response.close();
            return result;
        } catch (Exception ex) {
            return new Response(0, null, ex, ex.getMessage(), null);
        }
    }

    public static Response post(RequestPost request) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(request.getTimeout(), TimeUnit.SECONDS)
                    .readTimeout(request.getTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(request.getTimeout(), TimeUnit.SECONDS)
                    .build();

            Request.Builder requestBuilder = new Request.Builder().url(request.getUrl());
            request.getHeader().forEach((k, v) -> {
                requestBuilder.addHeader(k, v);
            });
            if (!request.getHeader().containsKey("Content-Type")) {
                requestBuilder.addHeader("Content-Type", "text/html; charset=utf-8");
            }

            Request request1;

            if (request.getRequestBody() != null) {
                RequestBody body = RequestBody.create(request.getRequestBody());
                request1 = requestBuilder.post(body).build();

            } else {
                request1 = requestBuilder.build();
            }
            okhttp3.Response response = client.newCall(request1).execute();
            Map<String, String> setHeader = new HashMap<>();
            response.headers().toMultimap().forEach((k, v) -> {
                setHeader.put(k, String.join("; ", v));
            });

            Response result = new Response(
                    response.code(),
                    response.body().string(),
                    null,
                    response.message(),
                    setHeader);

            response.close();
            return result;
        } catch (Exception ex) {
            return new Response(0, null, ex, ex.getMessage(), null);
        }
    }
}
