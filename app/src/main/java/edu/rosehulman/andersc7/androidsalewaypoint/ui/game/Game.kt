package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
    val title: String = "",
    val price: Double = 0.0,
    val isOnItch: Boolean = false,
    val isOnNintendo: Boolean = false,
    val isOnPlayStation: Boolean = false,
    val isOnSteam: Boolean = false,
    val isOnXbox: Boolean = false,
    val otherStores: ArrayList<String> = ArrayList()
) : Parcelable {

}
