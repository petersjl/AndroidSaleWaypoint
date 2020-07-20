package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.andersc7.androidsalewaypoint.R

class GameAdapter(var context: Context, val games: ArrayList<Game>) : RecyclerView.Adapter<GameViewHolder>() {
	private val inflater = LayoutInflater.from(this.context)

	override fun getItemCount(): Int = this.games.size

	override fun onCreateViewHolder(parent: ViewGroup, index: Int): GameViewHolder {
		val view = this.inflater.inflate(R.layout.item_game, parent, false)
		return GameViewHolder(view, this)
	}

	override fun onBindViewHolder(holder: GameViewHolder, index: Int) {
		holder.bind(this.games[index])
	}

}