package it.ipzs.cieidsdk.network

import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class InternalX509TrustManager : X509TrustManager {

    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String?) {
        try {
            chain[0].checkValidity()
        } catch (e: Exception) {
            e.printStackTrace()
            throw CertificateException("Certificate not valid or trusted.")
        }
    }

    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String?) {
        try {
            chain[0].checkValidity()
        } catch (e: Exception) {
            e.printStackTrace()
            throw CertificateException("Certificate not valid or trusted.")
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
        return arrayOfNulls(0)
    }

}