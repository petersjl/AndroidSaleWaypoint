package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import edu.rosehulman.andersc7.androidsalewaypoint.Constants
import edu.rosehulman.andersc7.androidsalewaypoint.R
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.Listing
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.ListingAdapter
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.ListingLayoutManager

class GameFragment : Fragment() {
	lateinit var root: View
	private var game: Game? = null
	lateinit var adapter: ListingAdapter

	lateinit var gameRef: DocumentReference
	private var gameListener: ListenerRegistration? = null
	private var listingsListener: ListenerRegistration? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			this.game = it.getParcelable(ARG_GAME)
		}

		this.gameRef = FirebaseFirestore.getInstance().collection("Games").document(this.game!!.id)
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

		return this.root
	}

	override fun onResume() {
		super.onResume()
		gameListener = gameRef.addSnapshotListener { doc: DocumentSnapshot?, error: FirebaseFirestoreException? ->
			if (error != null) {
				Log.d(Constants.TAG, error.toString())
			}else{
				game?.description = doc?.get("description") as String
				this.root.findViewById<TextView>(R.id.game_description).text = this.game?.description
			}
		}

		listingsListener = gameRef.collection("Listings").addSnapshotListener {listings: QuerySnapshot?, error: FirebaseFirestoreException? ->
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

	companion object {
		private const val ARG_GAME = "game"

		@JvmStatic
		fun newInstance(game: Game) = GameFragment().apply {
			arguments = Bundle().apply {
				putParcelable(ARG_GAME, game)
			}
		}
	}
}