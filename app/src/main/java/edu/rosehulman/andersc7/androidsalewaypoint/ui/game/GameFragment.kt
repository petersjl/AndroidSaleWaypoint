package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.andersc7.androidsalewaypoint.R
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.ListingAdapter

class GameFragment : Fragment() {
	lateinit var root: View
	private var game: Game? = null
	lateinit var adapter: ListingAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			this.game = it.getParcelable(ARG_GAME)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		this.root = inflater.inflate(R.layout.fragment_game, container, false)

		this.adapter = ListingAdapter(this.root.context)
		val recyclerView = this.root.findViewById<RecyclerView>(R.id.game_listings)
		recyclerView.layoutManager = LinearLayoutManager(this.root.context)
		recyclerView.setHasFixedSize(true)
		recyclerView.adapter = this.adapter

		return this.root
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