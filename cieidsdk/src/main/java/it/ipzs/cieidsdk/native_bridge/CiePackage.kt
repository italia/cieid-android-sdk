
package it.ipzs.cieidsdk.native_bridge

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptModule
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import java.util.ArrayList


class CiePackage : ReactPackage {
    override fun createJSModules(): MutableList<Class<out JavaScriptModule>> {
        return ArrayList()
    }

    override fun createNativeModules(reactApplicationContext: ReactApplicationContext): List<NativeModule> {
        val modules = ArrayList<NativeModule>()

        modules.add(CieModule(reactApplicationContext))
        return modules
    }

    override fun createViewManagers(reactApplicationContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}
