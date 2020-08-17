package edu.rosehulman.andersc7.androidsalewaypoint.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import edu.rosehulman.andersc7.androidsalewaypoint.Constants
import java.lang.IllegalStateException
import java.net.URL

class ImageTask(private var imageConsumer: ImageConsumer) : AsyncTask<String, Void, Bitmap>() {
	override fun doInBackground(vararg params: String?): Bitmap {
		var inStream = URL(params[0]).openStream()
		val bitmap: Bitmap
		try {
			bitmap = BitmapFactory.decodeStream(inStream)
		} catch (e: IllegalStateException) {
			inStream = URL(Constants.INVALID_IMAGE).openStream()
			return BitmapFactory.decodeStream(inStream)
		}
		return bitmap
	}

	override fun onPostExecute(result: Bitmap?) {
		super.onPostExecute(result)
		this.imageConsumer.onImageLoaded(result)
	}
}