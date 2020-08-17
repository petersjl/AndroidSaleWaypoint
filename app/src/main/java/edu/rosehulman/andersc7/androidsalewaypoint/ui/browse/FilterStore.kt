package edu.rosehulman.andersc7.androidsalewaypoint.ui.browse

import android.content.Context
import com.google.firebase.firestore.*
import edu.rosehulman.andersc7.androidsalewaypoint.Constants
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.StoreType

class FilterStore(private val store: StoreType) : GameFilter {
	override fun getTitle(context: Context): String {
		return ""
	}

	override fun getSorted(games: CollectionReference, user: DocumentReference, userID: String ): Query {
		return games.whereArrayContains(Constants.FIELD_STORES, store.toString())
	}
}
