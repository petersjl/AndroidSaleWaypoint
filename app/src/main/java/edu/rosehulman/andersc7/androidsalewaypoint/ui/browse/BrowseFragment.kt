package edu.rosehulman.andersc7.androidsalewaypoint.ui.browse

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.andersc7.androidsalewaypoint.R
import edu.rosehulman.andersc7.androidsalewaypoint.ui.game.GameAdapter

class BrowseFragment(private val userID: String, private val filter: GameFilter) : Fragment() {
	private var listener: GameAdapter.OnGameSelectedListener? = null

	lateinit var root: View
	lateinit var adapter: GameAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		this.root = inflater.inflate(R.layout.fragment_browse, container, false)

		this.adapter = GameAdapter(this.root.context, this.userID, this.filter, this.listener)
		val recyclerView = this.root.findViewById<RecyclerView>(R.id.recycler_view)
		recyclerView.layoutManager = GridLayoutManager(this.root.context, 2)
		recyclerView.setHasFixedSize(true)
		recyclerView.adapter = this.adapter
		this.adapter.setSnapshotListener()

		return this.root
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context is GameAdapter.OnGameSelectedListener) this.listener = context
		else throw RuntimeException("$context must implement OnGameSelectedListener")
	}

	override fun onDetach() {
		super.onDetach()
		this.listener = null
	}
}