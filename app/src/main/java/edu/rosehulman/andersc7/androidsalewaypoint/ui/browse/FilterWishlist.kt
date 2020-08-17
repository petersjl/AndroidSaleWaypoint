package edu.rosehulman.andersc7.androidsalewaypoint.ui.browse

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import edu.rosehulman.andersc7.androidsalewaypoint.Constants
import edu.rosehulman.andersc7.androidsalewaypoint.R

class FilterWishlist : GameFilter {
	override fun getTitle(context: Context): String = context.getString(R.string.nav_wishlist)

	override fun getSorted(games: CollectionReference, user: DocumentReference, userID: String): Query {
		return games.whereArrayContains(Constants.FIELD_WISHLISTERS, userID)
	}
}
