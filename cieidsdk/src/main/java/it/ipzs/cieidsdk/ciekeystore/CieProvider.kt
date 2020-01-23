package it.ipzs.cieidsdk.ciekeystore

import java.security.Provider


internal class CieProvider : Provider(CieProvider::class.java.simpleName, 1.0, "Provider per cie") {

    companion object{
        const val PROVIDER = "CIE"
    }
    init {
        put("KeyStore.$PROVIDER", CieKeyStore::class.java.name)
        put("Cipher.RSA/ECB/PKCS1Padding", Cipher::class.java.name)
    }
}
