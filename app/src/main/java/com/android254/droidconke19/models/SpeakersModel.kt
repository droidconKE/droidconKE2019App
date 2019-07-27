package com.android254.droidconke19.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpeakersModel(
        var id: Int = 0,
        var name: String = "",
        var bio: String = "",
        var company: String = "",
        var photoUrl: String = ""
) : Parcelable

