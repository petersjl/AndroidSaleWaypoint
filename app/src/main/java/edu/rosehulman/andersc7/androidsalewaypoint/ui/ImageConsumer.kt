package edu.rosehulman.andersc7.androidsalewaypoint.ui

import android.content.Context
import android.graphics.Bitmap

interface ImageConsumer {
	fun getContextReference(): Context
	fun onImageLoaded(bitmap: Bitmap?)
}