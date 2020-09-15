package org.d3if1008.fundamentals222.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserItems (
    var id: Int?=0,
    var name: String?=null,
    var username: String?=null,
    var avatar_url: String? = null,
    var html_url: String? =null,
    var followers: Int?=0,
    var following: Int?=0,
    var public_repos: Int?=0
) : Parcelable