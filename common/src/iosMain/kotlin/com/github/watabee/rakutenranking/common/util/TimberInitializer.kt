package com.github.watabee.rakutenranking.common.core

import timber.log.NSLogTree
import timber.log.Timber

class TimberInitializer {

    fun initialize() {
        Timber.plant(NSLogTree(0))
    }
}