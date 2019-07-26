package it.ipzs.cieidsdk.exceptions

import android.nfc.TagLostException

internal open class CieException : TagLostException()

internal class BlockedPinException : CieException()

internal class NoCieException : CieException()

internal class PinNotValidException(val tentativi: Int) : CieException()
