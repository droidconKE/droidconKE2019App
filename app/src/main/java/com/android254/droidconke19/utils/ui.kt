package com.android254.droidconke19.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

// This file holds UI - related extension functions

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun ImageView.loadImage(url: String?, @DrawableRes placeholder: Int
) {
    Glide.with(context).load(url).apply { apply(RequestOptions().placeholder(placeholder).diskCacheStrategy(DiskCacheStrategy.ALL)) }.into(this)
}

