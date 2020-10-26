package it.ipzs.cieidsdk.sampleapp.redirection

enum class RedirectionError(val code : Int) {

    GENERIC_ERROR(0),
    CIE_NOT_REGISTERED(1),
    AUTHENTICATION_ERROR(2),
    NO_SECURE_DEVICE(3)
}