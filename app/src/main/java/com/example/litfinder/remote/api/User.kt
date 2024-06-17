package com.example.litfinder.remote.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

    @Parcelize
    data class User(
        var id: Int = -1,
        var token: String? = null,
        var name: String? = null,
        var username: String? = null
    ): Parcelable
