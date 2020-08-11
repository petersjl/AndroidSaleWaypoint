package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import edu.rosehulman.andersc7.androidsalewaypoint.Constants
import edu.rosehulman.andersc7.androidsalewaypoint.R

class GameAdapter(var context: Context, var listener: OnGameSelectedListener?) : RecyclerView.Adapter<GameViewHolder>() {
	private val games = ArrayList<Game>()

	private val gamesRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_GAMES);
	private var listenerRegistration: ListenerRegistration? = null

	fun setSnapshotListener() {
		this.listenerRegistration?.remove()
		this.games.clear()
		this.notifyDataSetChanged()

		this.listenerRegistration = this.gamesRef.addSnapshotListener { querySnapshot, e ->
			if (e != null) Log.w(Constants.TAG, "Listen error: $e")
			else this.processSnapshotChanges(querySnapshot!!)
		}
	}

	private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
		for (change in querySnapshot.documentChanges) {
			val game = Game.fromSnapshot(change.document)
			when (change.type) {
				DocumentChange.Type.ADDED -> {
					this.games.add(0, game)
					this.notifyItemInserted(0)
				}
				DocumentChange.Type.REMOVED -> {
					val index = this.games.indexOfFirst { it.id == game.id }
					this.games.removeAt(index)
					this.notifyItemRemoved(index)
				}
				DocumentChange.Type.MODIFIED -> {
					val index = this.games.indexOfFirst { it.id == game.id }
					this.games[index] = game
					this.notifyItemChanged(index)
				}
			}
		}
	}

//	init {
//		for (i in 0 until 18) this.games.add(Game())
//	}

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