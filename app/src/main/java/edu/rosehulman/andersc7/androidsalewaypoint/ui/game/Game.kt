package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.os.Parcelable
import android.util.Log
import com.google.android.gms.common.images.ImageManager
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
	var image: String = ""
) : Parcelable {
	@IgnoredOnParcel
	@get:Exclude
	var id = ""
	var wishlist = false
	val listings: ArrayList<Listing> = ArrayList()

	companion object {
		const val KEY_UID = "uid"

		fun fromSnapshot(doc: DocumentSnapshot, user: String): Game {
			val image = doc[Constants.FIELD_IMAGE]
			val game = Game(
				doc[Constants.FIELD_TITLE] as String,
				doc[Constants.FIELD_DEVELOPER] as String,
				doc[Constants.FIELD_DESCRIPTION] as String,
				if (image == null) { "" } else { image as String }
			)
			game.wishlist = (doc[Constants.FIELD_WISHLISTERS] as ArrayList<String>).contains(user)
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
