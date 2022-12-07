package com.gleb.delineater.ui.intents

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.gleb.delineater.R
import java.io.File

private const val PROVIDER_AUTHORITY = "com.gleb.fileprovider.delineater"
private const val SHARE_TYPE = "image/*"

fun Context.sharePicture(picturePath: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_STREAM,
            getContentUri(File(picturePath))
        )
        type = SHARE_TYPE
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    startActivity(
        Intent.createChooser(
            shareIntent, getString(R.string.share_via)
        )
    )
}

private fun Context.getContentUri(file: File) = FileProvider.getUriForFile(
    this, PROVIDER_AUTHORITY, file
)