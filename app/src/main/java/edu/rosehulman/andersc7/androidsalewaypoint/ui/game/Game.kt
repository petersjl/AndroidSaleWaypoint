package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import edu.rosehulman.andersc7.androidsalewaypoint.Constants
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.Listing
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
	val title: String = "",
	val developer: String = "",
	var description: String = "",
	val listings: ArrayList<Listing> = ArrayList()
) : Parcelable {
	@IgnoredOnParcel
	@get:Exclude
	var id = ""

	companion object {
		const val KEY_UID = "uid"

		fun fromSnapshot(doc: DocumentSnapshot): Game {
			val game = Game(doc[Constants.FIELD_TITLE] as String, doc[Constants.FIELD_DEVELOPER] as String, doc[Constants.FIELD_DESCRIPTION] as String)
			doc.reference.collection(Constants.COLLECTION_LISTINGS).get().addOnSuccessListener {
				for (listing in it) {
					game.listings.add(Listing.fromSnapshot(listing))
					Log.d(Constants.TAG, "test" + game.listings.size.toString())
				}
			}
			game.id = doc.id
			return game
		}
	}
}
