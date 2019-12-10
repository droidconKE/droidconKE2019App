package com.android254.droidconke19.models

data class WifiDetailsModel (
        var wifiSsid: String,
        var wifiPassword: String
)



val WifiDetailsModel.isPropertiesNotEmpty
    get() = wifiSsid.isNotEmpty() and wifiPassword.isNotEmpty()