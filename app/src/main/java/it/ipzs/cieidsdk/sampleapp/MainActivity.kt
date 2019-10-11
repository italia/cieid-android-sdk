package it.ipzs.cieidsdk.sampleapp

import android.os.Bundle
import android.util.Log
import it.ipzs.cieidsdk.common.Callback
import it.ipzs.cieidsdk.common.CieIDSdk
import it.ipzs.cieidsdk.common.Event
import kotlinx.android.synthetic.main.activity_main.*
import android.text.InputType
import android.widget.EditText
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), Callback {

    override fun onEvent(event: Event) {
        Log.d("onEvent",event.toString())
        runOnUiThread {
            if(event.attempts==0){
                text.text = "EVENT : $event"
            }else {
                text.text = "EVENT : $event\nTentativi : ${event.attempts}"
            }
        }


    }

    override fun onError(e: Throwable) {
        Log.d("onError",e.localizedMessage)
        runOnUiThread {
            text.text = "ERROR : $e.localizedMessage"
        }
    }

    override fun onSuccess(url: String) {
        //rimostro la webview e gli passo la url da caricare
        webView.visibility = VISIBLE
        webView.loadUrl(url)
        text.visibility = GONE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //opzioni sicurezza webview
        webView.settings.apply {
            javaScriptEnabled = true
            allowContentAccess = false
            allowFileAccess = false
            allowFileAccessFromFileURLs = false
            allowUniversalAccessFromFileURLs = false
        }

        //url service provider regione toscana collaudo
        webView.loadUrl("https://servizi.torinofacile.it/cgi-bin/accesso/base/index.cgi")
        //webView.loadUrl("https://accessosicuro-trial.rete.toscana.it/portal/accessError?targetSeviceUrl=https%3A%2F%2Firistest.rete.toscana.it%3A443%2Fprivate&errorCode=auth.access.error.message.noURLCertAuth&minRequiredAuthLevel=2&goToUrl=https%3A%2F%2Faccessosicuro-trial.rete.toscana.it%3A443%2Fopensso%2FSSOPOST%2FmetaAlias%2Fidp%3FReqID%3Ds259b63f03ead27188c8876de2b443ed99e9636638%26spEntityId%3Dhttps%253A%252F%252Firistest.rete.toscana.it%253A443%252Fprivate")


        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // The webView is about to navigate to the specified host.
                if (url.toString().contains("OpenApp")) {
                    //settare la url caricata dalla webview su /OpenApp
                    CieIDSdk.setUrl(url.toString())
                    insertPin()
                    return true

                }
                return super.shouldOverrideUrlLoading(view, url)
            }

        }

    }

    private fun startNFC() {
        //configurazione cieidsdk
        CieIDSdk.start(this, this)
        CieIDSdk.startNFCListening(this)
        webView.visibility = GONE
        //abilitare i log
        CieIDSdk.enableLog = true
    }

    fun insertPin(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Inserisci PIN")

        // Set up the input
        val input = EditText(this)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
            CieIDSdk.pin = input.text.toString()
            startNFC()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
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