package tests

import io.kotlintest.specs.FreeSpec
import io.kotlintest.specs.StringSpec
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import android.os.Build
import io.kotlintest.matchers.shouldBe
import it.ipzs.cieidsdk.common.CieIDSdk


@Throws(Exception::class)
fun setFinalStatic(field: Field, newValue: Any) {
    field.setAccessible(true)

    val modifiersField = Field::class.java!!.getDeclaredField("modifiers")
    modifiersField.setAccessible(true)
    modifiersField.setInt(field, field.getModifiers() and Modifier.FINAL.inv())

    field.set(null, newValue)
}

class ApiLevel : FreeSpec({

    // arrange
    setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 123)
    // act
    val apiLevel = CieIDSdk.hasApiLevelSupport()
    //assert
    "should api 123 level be supported"{
        apiLevel shouldBe true
    }

    // arrange
    setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 23)
    // act
    val apiLevel23 = CieIDSdk.hasApiLevelSupport()
    //assert
    "should api 23 level be supported"{
        apiLevel23 shouldBe true
    }

    // arrange
    setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 22)
    // act
    val apiLevel22 = CieIDSdk.hasApiLevelSupport()
    //assert
    "should api 22 level not be supported"{
        apiLevel22 shouldBe false
    }
})