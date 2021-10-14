package com.mokhtarabadi.secureapi.android.interceptor;

import android.util.Base64;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mokhtarabadi.secureapi.android.utility.EncryptionUtility;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;

@Slf4j
@RequiredArgsConstructor
public class SignatureInterceptor implements Interceptor {

    @NonNull private String apiKey;
    @NonNull private String apiSecret;

    @NonNull private Gson gson;
    @NonNull private SecureRandom secureRandom;

    @Override
    public Response intercept(Chain chain) throws IOException {
        val originalRequest = chain.request();

        if (originalRequest.body() == null && originalRequest.header("X-API-KEY") != null) {
            return chain.proceed(originalRequest);
        }

        val builder = originalRequest.newBuilder().header("X-API-KEY", apiKey);

        if (!originalRequest.method().equals("POST") && !originalRequest.method().equals("PUT")) {
            return chain.proceed(builder.build());
        }

        val compressedRequest =
                builder.method(originalRequest.method(), signature(originalRequest.body())).build();

        return chain.proceed(compressedRequest);
    }

    private RequestBody signature(RequestBody body) {
        return new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() throws IOException {
                return -1;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                val buffer = new Buffer();
                body.writeTo(buffer);
                val object = gson.fromJson(buffer.readUtf8(), JsonObject.class);
                buffer.close();

                val nonce = new byte[32];
                secureRandom.nextBytes(nonce);
                object.addProperty("nonce", Base64.encodeToString(nonce, Base64.NO_WRAP));

                val timestamp = System.currentTimeMillis();
                object.addProperty("timestamp", timestamp);

                val receiveWindows = 5000;
                object.addProperty("receive_windows", receiveWindows);

                val body = object.toString();
                val signature = EncryptionUtility.generateHashWithHmac256(body, apiSecret);

                sink.writeUtf8(String.format("%s.%s", URLEncoder.encode(body), signature));
                sink.close();
            }
        };
    }
}
