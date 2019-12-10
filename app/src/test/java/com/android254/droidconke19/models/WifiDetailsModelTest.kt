package com.android254.droidconke19.models

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class WifiDetailsModelTest {

    @Test
    fun `test isPropertiesNotEmpty when one property is empty returns false`() {
        val wifiDetailsModel = WifiDetailsModel("", "password")
        assertThat(wifiDetailsModel.isPropertiesNotEmpty, `is`(false))
    }

    @Test
    fun `test isPropertiesNotEmpty when both properties are empty returns false`() {
        val wifiDetailsModel = WifiDetailsModel("", "")
        assertThat(wifiDetailsModel.isPropertiesNotEmpty, `is`(false))
    }

    @Test
    fun `test isPropertiesNotEmpty when both properties are not empty returns true`() {
        val wifiDetailsModel = WifiDetailsModel("ssid", "password")
        assertThat(wifiDetailsModel.isPropertiesNotEmpty, `is`(true))
    }

}