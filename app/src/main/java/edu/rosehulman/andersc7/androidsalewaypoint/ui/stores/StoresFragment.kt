package edu.rosehulman.andersc7.androidsalewaypoint.ui.stores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import edu.rosehulman.andersc7.androidsalewaypoint.R

class StoresFragment : Fragment() {
	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_stores, container, false)
		val textView: TextView = root.findViewById(R.id.text_slideshow)
		return root
	}
}