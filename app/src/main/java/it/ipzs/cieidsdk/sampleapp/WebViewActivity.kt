package it.ipzs.cieidsdk.sampleapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_webview.*


class WebViewActivity : AppCompatActivity() {


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        //opzioni sicurezza webview
        webView.settings.apply {
            javaScriptEnabled = true
            allowContentAccess = false
            allowFileAccess = false
            allowFileAccessFromFileURLs = false
            allowUniversalAccessFromFileURLs = false
        }

        //url service provider regione toscana collaudo
        webView.loadUrl("https://accessosicuro-trial.rete.toscana.it/portal/accessError?targetSeviceUrl=https%3A%2F%2Firistest.rete.toscana.it%3A443%2Fprivate&errorCode=auth.access.error.message.noURLCertAuth&minRequiredAuthLevel=2&goToUrl=https%3A%2F%2Faccessosicuro-trial.rete.toscana.it%3A443%2Fopensso%2FSSOPOST%2FmetaAlias%2Fidp%3FReqID%3Ds25d022e6ac6f15bceb8a60daf47bd4559a04d8ccc%26spEntityId%3Dhttps%253A%252F%252Firistest.rete.toscana.it%253A443%252Fprivate")


        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // The webView is about to navigate to the specified host.
                if (url.toString().contains("OpenApp")) {

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.putExtra(MainActivity.KEY_URL,url.toString())
                    startActivityForResult(intent, 0)
                    return true

                }
                return super.shouldOverrideUrlLoading(view, url)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        webView.loadUrl(data?.getStringExtra(MainActivity.KEY_URL))

    }

}
