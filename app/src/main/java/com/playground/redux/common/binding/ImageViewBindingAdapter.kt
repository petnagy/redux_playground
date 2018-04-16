package com.playground.redux.common.binding

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView

@BindingAdapter("image")
fun setupImageView(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("image")
fun setupImageView(imageView: ImageView, drawable: Drawable) {
    imageView.setImageDrawable(drawable)
}