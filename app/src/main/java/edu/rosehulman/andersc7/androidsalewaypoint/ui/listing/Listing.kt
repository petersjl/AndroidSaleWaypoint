package edu.rosehulman.andersc7.androidsalewaypoint.ui.listing

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Listing(
	val store: StoreType = StoreType.STEAM,
	val price: Float = 0f,
	val sale: Float = 0f
) : Parcelable {
	@IgnoredOnParcel
	@get:Exclude
	var id = ""

	companion object {
		const val KEY_UID = "uid"

		fun fromSnapshot(doc: DocumentSnapshot): Listing {
			val listing = doc.toObject(Listing::class.java)!!
			listing.id = doc.id
			return listing
		}

		fun fromReference(doc: DocumentReference): Listing {
			var listing: Listing = Listing()
			doc.get().addOnSuccessListener {
				listing = Listing.fromSnapshot(it)
			}
			return listing
		}
	}
}
