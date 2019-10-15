package it.ipzs.cieidsdk.native_bridge

import com.facebook.react.bridge.*
import it.ipzs.cieidsdk.common.Callback
import it.ipzs.cieidsdk.common.CieIDSdk
import it.ipzs.cieidsdk.common.Event
import com.facebook.react.modules.core.RCTNativeAppEventEmitter
import com.facebook.react.bridge.Arguments.createMap


class CieModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext),
    Callback {


    private var cieInvalidPinAttempts: Int = 0

    /**
     * onSuccess is called when the CIE authentication is successfully completed.
     * @param[url] the form consent url
     */
    override fun onSuccess(url: String) {
        this.sendEvent(successChannel, url)
    }

    /**
     * onError is called if some errors occurred during CIE authentication
     * @param[error] the error occurred
     */
    override fun onError(error: Throwable) {
        this.sendEvent(errorChannel, error.message ?: "generic error")
    }

    /**
     * onEvent is called if an event occurs
     */
    override fun onEvent(event: Event) {
        cieInvalidPinAttempts = event.attempts;
        this.sendEvent(eventChannel, event.toString())
    }

    override fun getName(): String {
        return "NativeCieModule"
    }

    private fun getWritableMap(eventValue: String): WritableMap {
        val writableMap = createMap()
        writableMap.putString("event", eventValue)
        writableMap.putInt("attempts", cieInvalidPinAttempts)
        return writableMap
    }

    private fun sendEvent(channel: String, eventValue: String) {
        reactApplicationContext
            .getJSModule(RCTNativeAppEventEmitter::class.java)
            .emit(channel, getWritableMap(eventValue))
    }


    @ReactMethod
    fun start(callback: com.facebook.react.bridge.Callback) {
        try {
            CieIDSdk.start(getCurrentActivity()!!, this)
            callback.invoke(null, null)
        } catch (e: RuntimeException) {
            callback.invoke(e.message, null)
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
    fun setPin(pin: String, callback: com.facebook.react.bridge.Callback) {
        try {
            CieIDSdk.pin = pin
            callback.invoke()
        } catch (e: IllegalArgumentException) {
            callback.invoke(e.message)
        }
    }

    @ReactMethod
    fun setAuthenticationUrl(url: String) {
        CieIDSdk.setUrl(url)
    }

    @ReactMethod
    fun startListeningNFC(callback: com.facebook.react.bridge.Callback) {
        try {
            CieIDSdk.startNFCListening(getCurrentActivity()!!)
            callback.invoke(null, null)
        } catch (e: RuntimeException) {
            callback.invoke(e.message, null)
        }
    }

    @ReactMethod
    fun stopListeningNFC(callback: com.facebook.react.bridge.Callback) {
        try {
            CieIDSdk.stopNFCListening(getCurrentActivity()!!)
            callback.invoke(null, null)
        } catch (e: RuntimeException) {
            callback.invoke(e.message, null)
        }
    }

    companion object {
        const val eventChannel: String = "onEvent"
        const val errorChannel: String = "onError"
        const val successChannel: String = "onSuccess"
    }

    @ReactMethod
    fun openNFCSettings(callback: com.facebook.react.bridge.Callback) {
        val currentActivity = getCurrentActivity()
        if (currentActivity == null) {
            callback.invoke("fail to get current activity");
        } else {
            CieIDSdk.openNFCSettings(currentActivity);
            callback.invoke();
        }
    }

    @ReactMethod
    fun hasApiLevelSupport(callback: com.facebook.react.bridge.Callback) {
        callback.invoke(CieIDSdk.hasApiLevelSupport())
    }

}
