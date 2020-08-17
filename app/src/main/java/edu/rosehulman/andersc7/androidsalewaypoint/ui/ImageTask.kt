package edu.rosehulman.andersc7.androidsalewaypoint.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import edu.rosehulman.andersc7.androidsalewaypoint.R
import java.lang.Exception
import java.net.URL

class ImageTask(private var imageConsumer: ImageConsumer) : AsyncTask<String, Void, Bitmap>() {
	override fun doInBackground(vararg params: String?): Bitmap {
		return try {
			val inStream = URL(params[0]).openStream()
			BitmapFactory.decodeStream(inStream)
		} catch (e: Exception) {
			BitmapFactory.decodeResource(this.imageConsumer.getContextReference().resources, R.drawable.invalid_image)
		}
	}

	override fun onPostExecute(result: Bitmap?) {
		super.onPostExecute(result)
		this.imageConsumer.onImageLoaded(result)
	}
}