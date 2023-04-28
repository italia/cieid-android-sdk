package it.ipzs.cieidsdk.network

import it.ipzs.cieidsdk.BuildConfig
import it.ipzs.cieidsdk.common.CieIDSdk
import it.ipzs.cieidsdk.network.service.IdpService
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


internal class NetworkClient(private val certificate : ByteArray) {

    private val okHttpClient: OkHttpClient by lazy { okhttpInitializer() }

    var certificatePinner: CertificatePinner? = null

    private fun okhttpInitializer(): OkHttpClient {

        val spec : ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_3)
            .build()

        val trustManagerFactory: TrustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.getTrustManagers()
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            ("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers))
        }
        val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager

        val builder =  OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(sslInterceptor)
            .certificatePinner(certificatePinner!!)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            //.// Custom SSL socket factory to add TLS 1.3 support
            .sslSocketFactory(InternalSSLSocketFactory(), trustManager)
            .connectionSpecs(Collections.singletonList(spec))

        return builder.build()
    }

    private val sslInterceptor: SSLInterceptor
            by lazy {
                val interceptor = SSLInterceptor()
                interceptor
            }

    private val retrofitWithRx: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_IDP)
            .client(okHttpClient)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    private val loggingInterceptor: HttpLoggingInterceptor
            by lazy {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level =
                    if (CieIDSdk.enableLog) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                interceptor
            }


    val idpService: IdpService by lazy { retrofitWithRx.create(IdpService::class.java) }


}