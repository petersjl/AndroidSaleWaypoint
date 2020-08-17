package edu.rosehulman.andersc7.androidsalewaypoint

import android.widget.SearchView
import java.util.*

class SearchQuery(private val searchable: Searchable?) : SearchView.OnQueryTextListener {
	override fun onQueryTextSubmit(query: String?): Boolean {
		return true
	}

	override fun onQueryTextChange(newText: String?): Boolean {
		this.searchable?.onSearch(newText!!.toLowerCase(Locale.getDefault()))
		return true
	}

	interface Searchable {
		fun onSearch(text: String)
	}
}
