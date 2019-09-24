
package it.ipzs.cieidsdk.native_bridge

import com.facebook.react.bridge.*
import it.ipzs.cieidsdk.common.Callback
import it.ipzs.cieidsdk.common.CieIDSdk
import it.ipzs.cieidsdk.common.Event
import com.facebook.react.modules.core.RCTNativeAppEventEmitter
import com.facebook.react.bridge.Arguments.createMap


class CieModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), Callback {


    private var eventCallback: com.facebook.react.bridge.Callback? = null


    override fun onSuccess(url: String) {
        this.sendEvent("SUCCESS ->${url}")
    }

    override fun onError(e: Throwable) {
        this.sendEvent("ERROR ->${e.message}")
    }

    override fun onEvent(event: Event) {
        this.sendEvent("EVENT ->${event}")
    }

    override fun getName(): String {
        return "NativeCieModule"
    }

    private fun sendEvent(
        eventName: String
    ) {
        val writableMap = createMap()
        writableMap.putString("description", eventName)
        reactApplicationContext
            .getJSModule(RCTNativeAppEventEmitter::class.java)
            .emit("event", writableMap)
    }


    @ReactMethod
    fun start(callback: com.facebook.react.bridge.Callback){
        try {
            CieIDSdk.start(getCurrentActivity()!!, this)
            callback.invoke(null, null)
        } catch (e: RuntimeException) {
            callback.invoke(e.message,null)
        }
    }

    @ReactMethod
    fun isNFCEnabled(callback: com.facebook.react.bridge.Callback) {
        callback.invoke(CieIDSdk.isNFCEnabled(getCurrentActivity()!!))
    }

    @ReactMethod
    fun hasNFCFeature(callback: com.facebook.react.bridge.Callback) {
        callback.invoke(CieIDSdk.hasFeatureNFC(getCurrentActivity()!!))
    }

    @ReactMethod
    fun setPin(pin: String) {
        CieIDSdk.pin = pin
    }

    @ReactMethod
    fun setAuthenticationUrl(url: String) {
        CieIDSdk.setUrl(url)
    }

    @ReactMethod
    fun startListeningNFC(callback: com.facebook.react.bridge.Callback) {
        try {
            CieIDSdk.startNFCListening(getCurrentActivity()!!)
            callback.invoke(null,null)
        } catch (e: RuntimeException) {
            callback.invoke(e.message,null)
        }
    }

    @ReactMethod
    fun stopListeningNFC(callback: com.facebook.react.bridge.Callback) {
        try {
            CieIDSdk.stopNFCListening(getCurrentActivity()!!)
            callback.invoke(null, null)
        } catch (e: RuntimeException) {
            callback.invoke(e.message,null)
        }
    }

    @ReactMethod
    fun setEventListner(callback: com.facebook.react.bridge.Callback) {
        this.eventCallback = callback
    }


}
