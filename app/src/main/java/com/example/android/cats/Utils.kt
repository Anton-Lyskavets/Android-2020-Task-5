package com.example.android.cats

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

private const val QUALITY = 100

/**
 * Displaying a picture obtained by URL
 * @param imageView ImageView for showing
 * @param imgUrl link to picture
 */
fun setPicture(imageView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri =
            imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.black_cat)
            )
            .into(imageView)
    }
}

/**
 * Saving image to phone
 * @param bitmap the saving picture
 * @param name name of the save picture
 */
fun saveImageToGallery(bitmap: Bitmap?, name: String?, context: Context) {
    try {
        val fos: OutputStream? =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                getStreamForVersionSmallerQ(context, name)
            } else {
                getStreamForVersionLargerQ(context, name)
            }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, QUALITY, it)
        }
        Toast.makeText(context, "image saved", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "no saved", Toast.LENGTH_SHORT).show()
    }
}

/**
 * checking for internet connection
 */
@Suppress("DEPRECATION")
fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager?.run {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
    } else {
        connectivityManager?.run {
            connectivityManager.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    result = true
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    result = true
                }
            }
        }
    }
    return result
}

/**
 * Function to get OutputStream for API version < 29
 * @param name name of the save picture
 */
@RequiresApi(Build.VERSION_CODES.Q)
private fun getStreamForVersionSmallerQ(context: Context, name: String?): OutputStream? {
    context.contentResolver?.also { resolver ->
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$name.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES
            )
        }
        val imageUri: Uri? =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        return imageUri?.let { resolver.openOutputStream(it) }
    }
    return null
}

/**
 * Function to get OutputStream for API version > 29
 * @param name name of the save picture
 */
@Suppress("DEPRECATION")
private fun getStreamForVersionLargerQ(context: Context, name: String?): OutputStream {
    val imagesDir =
        when (Environment.getExternalStorageState()) {
            Environment.MEDIA_MOUNTED -> Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ).toString()
            else -> context.applicationContext.filesDir.toString()
        }
    val image = File(imagesDir, "$name.jpg")

    return FileOutputStream(image)
}

/**
 * checking permission to use phone storage
 */
fun checkPermission(context: Context) = ContextCompat.checkSelfPermission(
    context,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
) ==
    PackageManager.PERMISSION_GRANTED
