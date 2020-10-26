package it.ipzs.cieidsdk.sampleapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.ipzs.cieidsdk.sampleapp.internal.InternalActivity
import it.ipzs.cieidsdk.sampleapp.redirection.RedirectionActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        button_internal.setOnClickListener {

            startActivity(Intent (this, InternalActivity::class.java))
        }

        button_redirection.setOnClickListener {

            startActivity(Intent (this, RedirectionActivity::class.java))
        }
    }

}