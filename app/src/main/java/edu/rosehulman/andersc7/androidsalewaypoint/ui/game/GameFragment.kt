package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.content.Context
import android.graphics.Bitmap
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import edu.rosehulman.andersc7.androidsalewaypoint.Constants
import edu.rosehulman.andersc7.androidsalewaypoint.R
import edu.rosehulman.andersc7.androidsalewaypoint.ui.ImageConsumer
import edu.rosehulman.andersc7.androidsalewaypoint.ui.ImageTask
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.Listing
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.ListingAdapter
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.ListingLayoutManager
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game.view.*

class GameFragment : Fragment(), ImageConsumer {
	lateinit var root: View
	private var game: Game? = null
	private var user: String? = null
	private var wishlist: Boolean = false
	private lateinit var wishlistView: ImageView
	lateinit var adapter: ListingAdapter

	lateinit var gameRef: DocumentReference
	private var gameListener: ListenerRegistration? = null
	private var listingsListener: ListenerRegistration? = null

	lateinit var userRef: DocumentReference
	lateinit var userGameReference: DocumentReference

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			this.game = it.getParcelable(ARG_GAME)
			this.user = it.getString(ARG_USER)
		}

		this.gameRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_GAMES).document(this.game!!.id)
		this.userRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS).document(this.user!!)
		this.userGameReference = this.userRef.collection(Constants.COLLECTION_GAMES).document(this.game!!.id)
		this.userGameReference.get().addOnSuccessListener {
			if (it.exists()) this.wishlist = it.get(Constants.FIELD_WISHLIST) as Boolean
			else {
				this.userGameReference.set(mapOf(Constants.FIELD_WISHLIST to false))
				this.wishlist = false
			}
			this.updateWishlistView()
			this.userGameReference.addSnapshotListener { doc: DocumentSnapshot?, error: FirebaseFirestoreException? ->
				this.wishlist = doc!!.get(Constants.FIELD_WISHLIST) as Boolean
				this.updateWishlistView()
			}
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		this.root = inflater.inflate(R.layout.fragment_game, container, false)

		this.adapter = ListingAdapter(this.root.context)
		val recyclerView = this.root.findViewById<RecyclerView>(R.id.game_listings)
		recyclerView.layoutManager = ListingLayoutManager(this.root.context)
		recyclerView.setHasFixedSize(true)
		recyclerView.adapter = this.adapter

		this.root.findViewById<TextView>(R.id.game_title).text = this.game?.title
		this.root.findViewById<TextView>(R.id.game_developer).text = this.game?.developer
		this.root.findViewById<TextView>(R.id.game_description).text = this.game?.description
		if (this.game?.image != "") ImageTask(this).execute(this.game?.image)
		else this.root.findViewById<TextView>(R.id.game_image_cover).setText(R.string.no_image)
		this.game?.listings?.forEach { this.adapter.add(it) }
		this.wishlistView = this.root.findViewById(R.id.game_wishlist)
		this.wishlistView.setOnClickListener {
			this.userGameReference.set(mapOf("wishlist" to !wishlist), SetOptions.merge())
			this.gameRef.get().addOnSuccessListener {
				val wishlisters = it.get(Constants.FIELD_WISHLISTERS) as ArrayList<String>
				if (wishlist) wishlisters.add(this.user!!)
				else wishlisters.remove(this.user!!)
				this.gameRef.set(mapOf(Constants.FIELD_WISHLISTERS to wishlisters), SetOptions.merge())
			}
		}

		return this.root
	}

	private fun updateWishlistView() {
		this.wishlistView.setColorFilter(this.root.context.getColor(
			if (wishlist) R.color.colorWishlist
			else R.color.colorNotWishlist
		))
	}

	override fun onResume() {
		super.onResume()
		gameListener = gameRef.addSnapshotListener { doc: DocumentSnapshot?, error: FirebaseFirestoreException? ->
			if (error != null) {
				Log.d(Constants.TAG, error.toString())
			}else{
				game?.description = doc?.get(Constants.FIELD_DESCRIPTION) as String
				this.root.findViewById<TextView>(R.id.game_description).text = this.game?.description

				val image = doc.get(Constants.FIELD_IMAGE) as String
				if (image != game?.image) {
					game?.image = image
					this.root.findViewById<TextView>(R.id.game_image_cover) .setText(R.string.loading)
					this.root.findViewById<TextView>(R.id.game_image_cover).visibility = View.VISIBLE
					if (this.game?.image != "") ImageTask(this).execute(this.game?.image)
					else this.root.findViewById<TextView>(R.id.game_image_cover) .setText(R.string.no_image)
				}
			}
		}

		listingsListener = gameRef.collection(Constants.COLLECTION_LISTINGS).addSnapshotListener {listings: QuerySnapshot?, error: FirebaseFirestoreException? ->
			if (error != null) {
				Log.d(Constants.TAG, error.toString())
			}else {
				Log.d(Constants.TAG, "Listings changed")
				this.game?.listings?.clear()
				if (listings != null) {
					for (listing in listings) {
						game?.listings?.add(0, Listing.fromSnapshot(listing))
					}
				}
				this.adapter.clear()
				this.game?.listings?.forEach { this.adapter.add(it) }
				this.adapter.notifyDataSetChanged()
			}
		}
	}

	override fun onPause() {
		super.onPause()
		gameListener = null
		listingsListener = null
	}

	override fun getContextReference(): Context {
		return this.root.context
	}

	override fun onImageLoaded(bitmap: Bitmap?) {
		this.root.findViewById<TextView>(R.id.game_image_cover).visibility = View.GONE
		this.root.game_image.setImageBitmap(bitmap)
	}

	companion object {
		private const val ARG_GAME = "game"
		private const val ARG_USER = "user"

		@JvmStatic
		fun newInstance(game: Game, userID: String) = GameFragment().apply {
			arguments = Bundle().apply {
				putParcelable(ARG_GAME, game)
				putString(ARG_USER, userID)
			}
		}
	}
}