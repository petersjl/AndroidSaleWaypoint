package edu.rosehulman.andersc7.androidsalewaypoint.ui.listing

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.andersc7.androidsalewaypoint.R

class ListingAdapter(var context: Context) : RecyclerView.Adapter<ListingViewHolder>() {
	private val listings = ArrayList<Listing>()

	init {
		for (storeType in StoreType.values()) {
			this.listings.add(Listing(storeType, 9.99f, 0.25f))
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, index: Int): ListingViewHolder {
		val view = LayoutInflater.from(this.context).inflate(R.layout.item_listing, parent, false)
		return ListingViewHolder(view, this)
	}

	override fun onBindViewHolder(holder: ListingViewHolder, index: Int) {
		holder.bind(this.listings[index])
	}

	override fun getItemCount(): Int = this.listings.size
}