package com.github.watabee.rakutenranking.util

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
internal class GlideModule : AppGlideModule() {

    override fun isManifestParsingEnabled(): Boolean = false
}