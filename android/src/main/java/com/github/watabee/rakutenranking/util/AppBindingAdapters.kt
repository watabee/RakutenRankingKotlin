package com.github.watabee.rakutenranking.util

import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {

    GlideApp.with(view).clear(view)

    if (url.isNullOrBlank().not()) {
        GlideApp.with(view)
            .load(url)
            .fitCenter()
            .into(view)
    }
}

@BindingAdapter("colorSchemeColor")
fun setColorSchemeColor(view: SwipeRefreshLayout, @ColorInt color: Int) {
    view.setColorSchemeColors(color)
}
