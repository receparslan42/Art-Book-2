package com.receparslan.artbook.util

import android.R.drawable
import android.content.Context
import android.os.Looper
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.request.RequestOptions

fun CircularProgressDrawable.defaultProgressIndicator(): CircularProgressDrawable = apply {
    strokeWidth = 10f
    centerRadius = 50f
    if (Looper.myLooper() == Looper.getMainLooper())
        start()
}

private fun getErrorDrawable(context: Context) = AppCompatResources.getDrawable(context, drawable.stat_notify_error)?.mutate().apply {
    this?.setTint(ContextCompat.getColor(context, android.R.color.holo_red_dark))
}

fun defaultImageOptions(context: Context): RequestOptions =
    RequestOptions()
        .placeholder(CircularProgressDrawable(context).defaultProgressIndicator())
        .error(getErrorDrawable(context))