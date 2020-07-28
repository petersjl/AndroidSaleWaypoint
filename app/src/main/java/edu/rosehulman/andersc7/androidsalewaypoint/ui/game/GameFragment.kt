package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.andersc7.androidsalewaypoint.R

class GameFragment : Fragment() {
	lateinit var root: View
	private var game: Game? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			this.game = it.getParcelable(ARG_GAME)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		this.root = inflater.inflate(R.layout.fragment_game, container, false)
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