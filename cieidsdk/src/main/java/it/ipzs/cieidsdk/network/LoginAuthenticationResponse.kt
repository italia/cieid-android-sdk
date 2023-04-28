package it.ipzs.cieidsdk.network

import com.squareup.moshi.Json

data class LoginAuthenticationResponse(@field:Json(name = "opText") val opText: String,
                                       @field:Json(name = "opType") val opType: String,
                                       @field:Json(name = "opId") val opId: String,
                                       @field:Json(name = "SpName") val SpName: String,
                                       @field:Json(name = "IdpName") val IdpName: String) : BaseResponse()