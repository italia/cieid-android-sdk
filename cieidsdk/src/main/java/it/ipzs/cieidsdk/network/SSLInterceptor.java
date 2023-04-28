package it.ipzs.cieidsdk.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.CipherSuite;
import okhttp3.Handshake;
import okhttp3.Response;
import okhttp3.TlsVersion;

/** Prints TLS Version and Cipher Suite for SSL Calls through OkHttp3 */
public class SSLInterceptor implements okhttp3.Interceptor {

    private static final String TAG = "OkHttp3-SSLHandshake";

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        printTlsAndCipherSuiteInfo(response);
        return response;
    }

    private void printTlsAndCipherSuiteInfo(Response response) {
        if (response != null) {
            Handshake handshake = response.handshake();
            if (handshake != null) {
                final CipherSuite cipherSuite = handshake.cipherSuite();
                final TlsVersion tlsVersion = handshake.tlsVersion();
                Log.v(TAG, "TLS: " + tlsVersion + ", CipherSuite: " + cipherSuite);
            }
        }
    }
}
