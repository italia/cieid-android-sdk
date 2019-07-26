package it.ipzs.cieidsdk.ciekeystore

import it.ipzs.cieidsdk.nfc.NFCCore.init
import java.security.Provider


internal class CieProvider : Provider(CieProvider::class.java.simpleName, 1.0, "Provider per cie") {

    companion object{
        const val PROVIDER = "CIE"
    }
    init {
        put("KeyStore.$PROVIDER", CieKeyStore::class.java.name)
        put("Signature.NONEwithRSA", CieSignatureImpl.None::class.java.name)
    }
}
