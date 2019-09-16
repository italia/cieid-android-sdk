package it.ipzs.cieidsdk.sampleapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import it.ipzs.cieidsdk.common.Callback
import it.ipzs.cieidsdk.common.CieIDSdk
import it.ipzs.cieidsdk.common.Event
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Callback {

    companion object {
        const val KEY_URL = "KEY_URL"
    }

    override fun onEvent(event: Event) {
        Log.d("onEvent",event.toString())
        text.text = "EVENT : $event"
    }

    override fun onError(e: Throwable) {
        Log.d("onError",e.localizedMessage)
        text.text = "ERROR : $e.localizedMessage"
    }

    override fun onSuccess(url: String) {
        //richiamo l'activity contenente la webview e gli passo la url
        val intent = Intent()
        intent.putExtra(KEY_URL, url)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val url = intent.getStringExtra(KEY_URL)

        //configurazione cieidsdk
        CieIDSdk.start(this, this)
        //passare il pin inserito dall'utente
        CieIDSdk.pin = "81813213"
        //settare la url caricata dalla webview su /OpenApp
        CieIDSdk.setUrl(url)
        //abilitare i log
        CieIDSdk.enableLog = true
    }

    override fun onResume() {
        super.onResume()
        //faccio partire l'ascolto dell'nfc
        CieIDSdk.startNFCListening(this)

    }

    override fun onPause() {
        super.onPause()
        //stop l'ascolto dell'nfc
        CieIDSdk.stopNFCListening(this)
    }


}