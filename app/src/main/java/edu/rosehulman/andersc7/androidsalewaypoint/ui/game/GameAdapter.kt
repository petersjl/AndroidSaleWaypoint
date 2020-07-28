package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.andersc7.androidsalewaypoint.Constants
import edu.rosehulman.andersc7.androidsalewaypoint.R

class GameAdapter(var context: Context, var listener: OnGameSelectedListener?) : RecyclerView.Adapter<GameViewHolder>() {
	private val games = ArrayList<Game>()

	init {
		for (i in 0 until 18) this.games.add(Game())
	}

	override fun onCreateViewHolder(parent: ViewGroup, index: Int): GameViewHolder {
		val view = LayoutInflater.from(this.context).inflate(R.layout.item_game, parent, false)
		return GameViewHolder(view, this)
	}

	override fun onBindViewHolder(holder: GameViewHolder, index: Int) {
		holder.bind(this.games[index])
	}

	override fun getItemCount(): Int = this.games.size

	fun selectGameAt(index: Int) {
		Log.d(Constants.TAG, index.toString())
		val game = this.games[index]
		this.listener?.onGameSelected(game)
	}

	interface OnGameSelectedListener {
		fun onGameSelected(game: Game)
	}
}