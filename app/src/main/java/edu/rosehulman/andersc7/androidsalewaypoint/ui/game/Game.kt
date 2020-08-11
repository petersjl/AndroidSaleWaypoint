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
	val description: String = "",
	val listings: ArrayList<Listing> = ArrayList()
) : Parcelable {
	@IgnoredOnParcel
	@get:Exclude
	var id = ""

	companion object {
		const val KEY_UID = "uid"

		fun fromSnapshot(doc: DocumentSnapshot): Game {
			val game = Game(doc["title"] as String, doc["developer"] as String, doc["description"] as String)
			doc.reference.collection("Listings").get().addOnSuccessListener {
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
