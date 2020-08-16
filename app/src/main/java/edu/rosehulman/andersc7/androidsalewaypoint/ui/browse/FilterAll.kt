package edu.rosehulman.andersc7.androidsalewaypoint.ui.browse

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import edu.rosehulman.andersc7.androidsalewaypoint.R

class FilterAll : GameFilter {
	override fun getTitle(context: Context): String = context.getString(R.string.nav_all)
	override fun getSorted(games: CollectionReference, user: DocumentReference): Query = games
}
