package com.android254.droidconke19.utils

 inline fun <T : Enum<T>> T.enumToInt(): Int = this.ordinal

 inline fun <reified T : Enum<T>> Int.toEnum(): T = enumValues<T>()[this]