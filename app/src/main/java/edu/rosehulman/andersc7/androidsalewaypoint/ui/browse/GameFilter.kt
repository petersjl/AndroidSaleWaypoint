package edu.rosehulman.andersc7.androidsalewaypoint.ui.browse

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

interface GameFilter {
	fun getTitle(context: Context): String
	fun getSorted(games: CollectionReference, user: DocumentReference, userID: String): Query
}
