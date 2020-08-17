package edu.rosehulman.andersc7.androidsalewaypoint.ui.listing

import android.graphics.Color
import android.opengl.Visibility
import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.andersc7.androidsalewaypoint.R
import kotlinx.android.synthetic.main.item_game.view.*
import kotlinx.android.synthetic.main.item_listing.view.*
import kotlin.math.floor

class ListingViewHolder(itemView: View, var adapter: ListingAdapter) :
	RecyclerView.ViewHolder(itemView) {
	var view: View = itemView

	fun bind(listing: Listing) {
		val img = when (listing.store) {
			StoreType.STEAM -> R.drawable.ic_steam
			StoreType.PLAYSTATION -> R.drawable.ic_playstation
			StoreType.XBOX -> R.drawable.ic_xbox
			StoreType.NINTENDO -> R.drawable.ic_nintendo
			StoreType.ITCH -> R.drawable.ic_itch
		}
		val sale = (listing.sale * 100).toInt()
		this.view.listing_store.setImageResource(img)
		this.view.listing_price_default.text = String.format("$%.2f", listing.price)
		this.view.listing_sale.text = String.format("%d%% Off",(listing.sale * 100).toInt())
		val priceSale = floor((listing.price * (1 - listing.sale)) * 100) / 100
		this.view.listing_price_sale.text = String.format("$%.2f", priceSale)
		if (sale == 0){
			this.view.listing_sale.isVisible = false
			this.view.listing_sale.background.setTint(Color.parseColor("#202125"))
			this.view.listing_price_sale.isVisible = false
		}else{
			this.view.listing_sale.isVisible = true
			this.view.listing_sale.background.setTint(Color.parseColor("#00A273"))
			this.view.listing_price_sale.isVisible = true
		}

	}
}