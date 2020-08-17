package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import edu.rosehulman.andersc7.androidsalewaypoint.Constants
import edu.rosehulman.andersc7.androidsalewaypoint.R
import edu.rosehulman.andersc7.androidsalewaypoint.ui.ImageConsumer
import edu.rosehulman.andersc7.androidsalewaypoint.ui.ImageTask
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.Listing
import edu.rosehulman.andersc7.androidsalewaypoint.ui.listing.StoreType
import kotlinx.android.synthetic.main.item_game.view.*

class GameViewHolder(itemView: View, var adapter: GameAdapter) : RecyclerView.ViewHolder(itemView), ImageConsumer {
	var view: View = itemView
	var listingsListener: ListenerRegistration? = null
	lateinit var textCover: TextView
	var imageTask: AsyncTask<String, Void, Bitmap>? = null
	var imageTaskRunning = false

	fun bind(game: Game) {
		this.itemView.setOnClickListener {
			this.adapter.selectGameAt(this.adapterPosition)
		}
		this.textCover = this.itemView.findViewById(R.id.tile_game_title)
		setIconsBlack()
		val visibility = if (game.wishlist) { View.VISIBLE } else { View.GONE }
		this.itemView.findViewById<ImageView>(R.id.item_game_wishlist).visibility = visibility
		this.textCover.setText(R.string.loading)
		this.textCover.visibility = View.VISIBLE
		if (imageTaskRunning) this.imageTask?.cancel(true)
		if (game.image != "") {
			this.imageTask = ImageTask(this).execute(game.image)
			this.imageTaskRunning = true
		}
		else this.textCover.text = game.title
		listingsListener = FirebaseFirestore
			.getInstance()
			.collection("Games")
			.document(game.id)
			.collection("Listings").addSnapshotListener { listings: QuerySnapshot?, error: FirebaseFirestoreException? ->
				if (error != null) Log.d(Constants.TAG, "Well somthing went wrong in the GameViewHolder")
				else{
					setIconsBlack()
					for(l in listings!!){
						val listing = l.toObject(Listing::class.java)
						when(listing.store){
							StoreType.STEAM -> {
								DrawableCompat.setTint(
									DrawableCompat.wrap(view.tile_steam.drawable),
									Color.parseColor(if (listing.sale == 0f) "#EEEEEE" else "#00A273")
								)
							}
							StoreType.PLAYSTATION -> {
								DrawableCompat.setTint(
									DrawableCompat.wrap(view.tile_playstation.drawable),
									Color.parseColor(if (listing.sale == 0f) "#EEEEEE" else "#00A273")
								)
							}
							StoreType.XBOX -> {
								DrawableCompat.setTint(
									DrawableCompat.wrap(view.tile_xbox.drawable),
									Color.parseColor(if (listing.sale == 0f) "#EEEEEE" else "#00A273")
								)
							}
							StoreType.NINTENDO -> {
								DrawableCompat.setTint(
									DrawableCompat.wrap(view.tile_nintendo.drawable),
									Color.parseColor(if (listing.sale == 0f) "#EEEEEE" else "#00A273")
								)
							}
							StoreType.ITCH -> {
								DrawableCompat.setTint(
									DrawableCompat.wrap(view.tile_itch.drawable),
									Color.parseColor(if (listing.sale == 0f) "#EEEEEE" else "#00A273")
								)
							}
						}
					}
				}
			}
	}

	fun setIconsBlack(){
		DrawableCompat.setTint(
			DrawableCompat.wrap(view.tile_steam.drawable),
			Color.parseColor("#000000")
		)
		DrawableCompat.setTint(
			DrawableCompat.wrap(view.tile_playstation.drawable),
			Color.parseColor("#000000")
		)
		DrawableCompat.setTint(
			DrawableCompat.wrap(view.tile_xbox.drawable),
			Color.parseColor("#000000")
		)
		DrawableCompat.setTint(
			DrawableCompat.wrap(view.tile_nintendo.drawable),
			Color.parseColor("#000000")
		)
		DrawableCompat.setTint(
			DrawableCompat.wrap(view.tile_itch.drawable),
			Color.parseColor("#000000")
		)
	}

	override fun getContextReference(): Context {
		return this.itemView.context
	}

	override fun onImageLoaded(bitmap: Bitmap?) {
		this.textCover.visibility = View.GONE
		this.itemView.findViewById<ImageView>(R.id.tile_image_background).setImageBitmap(bitmap)
		this.imageTaskRunning = false
	}
}