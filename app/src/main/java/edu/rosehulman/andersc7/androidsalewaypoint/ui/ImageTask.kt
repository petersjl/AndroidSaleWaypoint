package edu.rosehulman.andersc7.androidsalewaypoint.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.net.URL

class ImageTask(private var imageConsumer: ImageConsumer) : AsyncTask<String, Void, Bitmap>() {
	override fun doInBackground(vararg params: String?): Bitmap {
		val inStream = URL(params[0]).openStream()
		return BitmapFactory.decodeStream(inStream)
	}

	override fun onPostExecute(result: Bitmap?) {
		super.onPostExecute(result)
		this.imageConsumer.onImageLoaded(result)
	}
}