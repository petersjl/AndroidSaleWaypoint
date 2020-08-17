package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import edu.rosehulman.andersc7.androidsalewaypoint.Constants
import edu.rosehulman.andersc7.androidsalewaypoint.R
import edu.rosehulman.andersc7.androidsalewaypoint.SearchQuery
import edu.rosehulman.andersc7.androidsalewaypoint.ui.browse.GameFilter

class GameAdapter(var context: Context, val userID: String, val filter: GameFilter, var listener: OnGameSelectedListener?) : RecyclerView.Adapter<GameViewHolder>(), SearchQuery.Searchable {
	private val games = ArrayList<Game>()

	private var listenerRegistration: ListenerRegistration? = null
	private val gamesRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_GAMES)
	private val userRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USERS).document(userID)
	private val filterRef = filter.getSorted(this.gamesRef, this.userRef, this.userID)
	private var searchRef: Query = filterRef.orderBy(Constants.FIELD_SEARCHTERM, Query.Direction.DESCENDING)

	fun setSnapshotListener() {
		this.listenerRegistration?.remove()
		this.games.clear()
		this.notifyDataSetChanged()

		this.listenerRegistration = this.searchRef.addSnapshotListener { querySnapshot, e ->
			if (e != null) Log.w(Constants.TAG, "Listen error: $e")
			else this.processSnapshotChanges(querySnapshot!!)
		}
	}

	private fun processSnapshotChanges(querySnapshot: QuerySnapshot) {
		for (change in querySnapshot.documentChanges) {
			val game = Game.fromSnapshot(change.document, this.userID)
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

	override fun onViewRecycled(holder: GameViewHolder) {
		holder.listingsListener?.remove()
	}

	fun selectGameAt(index: Int) {
		Log.d(Constants.TAG, index.toString())
		val game = this.games[index]
		this.listener?.onGameSelected(game)
	}

	fun search(text: String) {
		val ref: Query = if (text == "") this.filterRef.orderBy(Constants.FIELD_SEARCHTERM, Query.Direction.DESCENDING)
//		else this.filterRef.orderBy(Constants.FIELD_SEARCHTERM, Query.Direction.DESCENDING).startAt(text).endAt("${text}\uf8ff")
		else this.filterRef.orderBy(Constants.FIELD_SEARCHTERM, Query.Direction.DESCENDING).startAt("${text}\uf8ff").endAt(text)
		this.searchRef = ref
		this.setSnapshotListener()
	}

	override fun onSearch(text: String) {
		this.search(text)
	}

	interface OnGameSelectedListener {
		fun onGameSelected(game: Game)
	}
}