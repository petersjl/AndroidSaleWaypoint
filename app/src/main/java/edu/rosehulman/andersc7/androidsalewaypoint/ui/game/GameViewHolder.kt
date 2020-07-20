package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GameViewHolder : RecyclerView.ViewHolder {
	var adapter: GameAdapter
	var view: View

	constructor(itemView: View, adapter: GameAdapter): super(itemView) {
		this.adapter = adapter
		this.view = itemView
	}

	fun bind(game: Game) {

	}
}