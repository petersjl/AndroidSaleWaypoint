package edu.rosehulman.andersc7.androidsalewaypoint.ui.browse

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.StoreType

class FilterStore(private val store: StoreType) : GameFilter {
	override fun getTitle(context: Context): String {
		return ""
	}

	override fun getSorted(games: CollectionReference, user: DocumentReference): Query {
		return games
	}
}
