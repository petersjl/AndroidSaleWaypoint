package edu.rosehulman.andersc7.androidsalewaypoint.ui.game

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
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

	val colorNotListed = this.view.context.getColor(R.color.colorNotListed)
	val colorListed = this.view.context.getColor(R.color.colorTextPrimary)
	val colorSale = this.view.context.getColor(R.color.colorSale)

	fun bind(game: Game) {
		this.itemView.setOnClickListener {
			this.adapter.selectGameAt(this.adapterPosition)
		}
		this.itemView.setOnLongClickListener {
			Toast.makeText(this.itemView.context, game.title, Toast.LENGTH_SHORT).show()
			return@setOnLongClickListener true
		}
		this.textCover = this.itemView.findViewById(R.id.tile_game_title)
		setIconsBlack()
		val visibility = if (game.wishlist) { View.VISIBLE } else { View.GONE }
		this.itemView.findViewById<ImageView>(R.id.item_game_wishlist).visibility = visibility
		this.textCover.setText(R.string.loading)
		this.textCover.visibility = View.VISIBLE
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
				if (error != null) Log.d(Constants.TAG, "Well something went wrong in the GameViewHolder")
				else{
					setIconsBlack()
					for(l in listings!!){
						val listing = l.toObject(Listing::class.java)
						val store = when(listing.store) {
							StoreType.STEAM -> view.tile_steam
							StoreType.PLAYSTATION -> view.tile_playstation
							StoreType.XBOX -> view.tile_xbox
							StoreType.NINTENDO -> view.tile_nintendo
							StoreType.ITCH -> view.tile_itch
						}
						setColor(store, if (listing.sale == 0f) colorListed else colorSale)
					}
				}
			}
	}

	private fun setColor(imageView: ImageView, color: Int) {
		ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color))
	}

	fun setIconsBlack(){
		setColor(view.tile_steam, colorNotListed)
		setColor(view.tile_playstation, colorNotListed)
		setColor(view.tile_xbox, colorNotListed)
		setColor(view.tile_nintendo, colorNotListed)
		setColor(view.tile_itch, colorNotListed)
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