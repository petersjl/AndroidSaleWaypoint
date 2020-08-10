package edu.rosehulman.andersc7.androidsalewaypoint.ui.listing

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Listing(
	val store: StoreType,
	val price: Float,
	val sale: Float
) : Parcelable