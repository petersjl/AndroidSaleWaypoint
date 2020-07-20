package edu.rosehulman.andersc7.androidsalewaypoint.ui.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import edu.rosehulman.andersc7.androidsalewaypoint.R

class SalesFragment : Fragment() {
	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_sales, container, false)
		val textView: TextView = root.findViewById(R.id.text_gallery)
		return root
	}
}