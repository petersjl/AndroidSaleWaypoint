package ninja.cooperstuff.expandableview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import edu.rosehulman.andersc7.androidsalewaypoint.R
import kotlin.math.max


class ExpandableView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
	var title: String?
	var startOpen: Boolean = true
	var open: Boolean = this.startOpen

	var expandableView: LinearLayout
	var headerView: LinearLayout
	var bodyView: LinearLayout

	var titleView: TextView
	var stateView: ImageView

	init {
		context.obtainStyledAttributes(attrs, R.styleable.ExpandableView, 0, 0).apply {
			try {
				title = getString(R.styleable.ExpandableView_title)
				startOpen = getBoolean(R.styleable.ExpandableView_startOpen, startOpen)
				open = startOpen
			} finally {
				recycle()
			}
		}

		expandableView = LayoutInflater.from(this.context).inflate(R.layout.list_header, this, false) as LinearLayout
		headerView = expandableView.findViewById(R.id.linearLayout_header)
		bodyView = expandableView.findViewById(R.id.linearLayout_body)

		titleView = headerView.findViewById(R.id.textView_title)
		stateView = headerView.findViewById(R.id.imageView_state)

		headerView.setOnClickListener {
			open = !open
			updateViews()
		}
		titleView.text = title
		updateViews()

		this.addView(expandableView)
	}

	override fun onViewAdded(child: View?) {
		if (childCount == 1) {
			super.onViewAdded(child)
			return
		}
		this.removeView(child)
		this.bodyView.addView(child)
	}

	override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
		expandableView.layout(
			expandableView.left, expandableView.top,
			expandableView.left + expandableView.measuredWidth,
			expandableView.top + expandableView.measuredHeight
		)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		// Find out how big the view needs to be
		measureChild(expandableView, widthMeasureSpec, heightMeasureSpec)

		// Find right-most and bottom-most point
		var maxWidth = expandableView.left + expandableView.measuredWidth
		var maxHeight = expandableView.top + expandableView.measuredHeight

		// Account for padding
		maxWidth += paddingLeft + paddingRight
		maxHeight += paddingTop + paddingBottom

		// Check against minimum height and width
		maxWidth = max(maxWidth, suggestedMinimumWidth)
		maxHeight = max(maxHeight, suggestedMinimumHeight)

		setMeasuredDimension(
			View.resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
			View.resolveSizeAndState(maxHeight, heightMeasureSpec, 0)
		)
	}

	private fun updateViews() {
		bodyView.visibility = if (open) View.VISIBLE else View.GONE
		stateView.setImageResource(
			if (open) R.drawable.ic_baseline_expand_less_24
			else R.drawable.ic_baseline_expand_more_24
		)
	}
}