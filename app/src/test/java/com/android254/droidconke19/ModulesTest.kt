package com.android254.droidconke19

import com.android254.droidconke19.di.appModule
import com.android254.droidconke19.di.dataModule
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class ModulesTest : KoinTest {

    @Test
    fun `check dependencies`() {
        koinApplication { modules(listOf(testContext, appModule, dataModule, testApp)) }.checkModules()
    }
}