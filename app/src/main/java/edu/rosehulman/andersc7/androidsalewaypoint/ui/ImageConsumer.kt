package edu.rosehulman.andersc7.androidsalewaypoint.ui

import android.graphics.Bitmap

interface ImageConsumer {
	fun onImageLoaded(bitmap: Bitmap?)
}