package edu.rosehulman.andersc7.androidsalewaypoint.ui.listing

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class ListingLayoutManager(context: Context) : LinearLayoutManager(context) {
	override fun canScrollVertically(): Boolean {
		return false
	}
}