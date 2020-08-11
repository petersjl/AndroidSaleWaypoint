package edu.rosehulman.andersc7.androidsalewaypoint.ui.listing

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.andersc7.androidsalewaypoint.R
import kotlinx.android.synthetic.main.item_listing.view.*
import kotlin.math.floor

class ListingViewHolder : RecyclerView.ViewHolder {
	var adapter: ListingAdapter
	var view: View

	constructor(itemView: View, adapter: ListingAdapter): super(itemView) {
		this.adapter = adapter
		this.view = itemView
	}

	fun bind(listing: Listing) {
		val img = when (listing.store) {
			StoreType.STEAM -> R.drawable.ic_steam
			StoreType.PLAYSTATION -> R.drawable.ic_playstation
			StoreType.XBOX -> R.drawable.ic_xbox
			StoreType.NINTENDO -> R.drawable.ic_nintendo
			StoreType.ITCH -> R.drawable.ic_itch
		}
		this.view.listing_store.setImageResource(img)
		this.view.listing_price_default.text = "$" + listing.price.toString()
		this.view.listing_sale.text = (listing.sale * 100).toInt().toString() + "% Off"
		val priceSale = floor((listing.price * (1 - listing.sale)) * 100) / 100
		this.view.listing_price_sale.text = "$" + priceSale.toString()
	}
}