package edu.rosehulman.andersc7.androidsalewaypoint.ui.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.andersc7.androidsalewaypoint.R

class SalesFragment : Fragment() {
	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_games_tiles, container, false)
		return root
	}
}