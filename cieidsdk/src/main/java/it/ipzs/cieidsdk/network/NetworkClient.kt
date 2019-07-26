package it.ipzs.cieidsdk.network

import it.ipzs.cieidsdk.BuildConfig
import it.ipzs.cieidsdk.ciekeystore.CieProvider
import it.ipzs.cieidsdk.common.CieIDSdk
import it.ipzs.cieidsdk.network.service.IdpService
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayInputStream
import java.security.KeyStore
import java.security.Security
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


internal class NetworkClient private constructor() {


    companion object {

        private var instance: NetworkClient? = null
        private lateinit var certificate : ByteArray

        fun get(certificate : ByteArray): NetworkClient {
            if (instance == null) {
                val sslContext = SSLContext.getInstance("TLSv1.2")
                sslContext.init(null, null, null)
                instance = NetworkClient()
                Companion.certificate = certificate
            }
            return instance!!
        }

        fun clear() {
            instance = null
        }
    }

    private val okHttpClient: OkHttpClient by lazy { okhttpInitializer() }

    private fun okhttpInitializer(): OkHttpClient {

        Security.removeProvider(CieProvider.PROVIDER)
        val cieProvider  = CieProvider()
        Security.insertProviderAt(cieProvider, 1)

       val certificatePinner = CertificatePinner.Builder()
            .add(Endpoints.BASE_URL_CERTIFICATE, Endpoints.PIN_ROOT)
            .add(Endpoints.BASE_URL_CERTIFICATE, Endpoints.PIN_LEAF)
            .build()

        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .certificatePinner(certificatePinner)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)

        val cieKeyStore : KeyStore = KeyStore.getInstance(CieProvider.PROVIDER)
        cieKeyStore.load(ByteArrayInputStream(certificate),null)

        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        kmf.init(cieKeyStore, null)
        val keyManagers = kmf.keyManagers

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
        }
        val trustManager = trustManagers[0] as X509TrustManager
        val sslContext = SSLContext.getInstance("TLSv1.2")
        sslContext.init(keyManagers, null, null)


        return builder.sslSocketFactory(sslContext.socketFactory,trustManager).build()


    }

    private var retrofitWithRx: Retrofit? = null


    private val loggingInterceptor: HttpLoggingInterceptor
            by lazy {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = if (CieIDSdk.enableLog) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                interceptor
            }


    private fun <T> createService(serviceClass: Class<T>): T {
        return createService(serviceClass, okHttpClient)
    }


    private fun <T> createService(serviceClass: Class<T>, client: OkHttpClient): T {
        if (retrofitWithRx == null) {
            retrofitWithRx = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_IDP)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }
        return retrofitWithRx!!.create(serviceClass)
    }


    val idpService: IdpService by lazy { createService(IdpService::class.java) }


}