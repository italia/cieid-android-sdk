package tests

import io.kotlintest.matchers.*
import io.kotlintest.specs.StringSpec
import it.ipzs.cieidsdk.common.CieIDSdk

class Pin : StringSpec({

    val rightPin = "12345678"
    CieIDSdk.pin = rightPin

    "pint should not empty or null" {
        CieIDSdk.pin shouldNotBe ""
    }

    "length should be 8" {
        CieIDSdk.pin.length shouldBe rightPin.length
    }

    // with digits but < 8 length
    val wrongPin = "123456"
    fun setWrongPin(){CieIDSdk.pin = wrongPin}
    "should throw an illegal argument exception"{
        shouldThrow<IllegalArgumentException> {
            setWrongPin()
        }
    }

    // with a char
    val wrongPin2 = "123a5678"
    fun setWrongPin2(){CieIDSdk.pin = wrongPin2}
    "should throw an illegal argument exception"{
        shouldThrow<IllegalArgumentException> {
            setWrongPin2()
        }
    }
})