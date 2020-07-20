package edu.rosehulman.andersc7.androidsalewaypoint.ui.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.andersc7.androidsalewaypoint.R
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.Game
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.GameAdapter

class WishlistFragment : Fragment() {
	lateinit var root: View
	lateinit var adapter: GameAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		this.root = inflater.inflate(R.layout.fragment_wishlist, container, false)

		val games: ArrayList<Game> = ArrayList()
		for (i in 0 until 18) games.add(Game())
		this.adapter = GameAdapter(this.root.context, games)
		val recyclerView = this.root.findViewById<RecyclerView>(R.id.recycler_view)
		recyclerView.layoutManager = GridLayoutManager(this.root.context, 2)
		recyclerView.setHasFixedSize(true)
		recyclerView.adapter = this.adapter

		return root
	}
}