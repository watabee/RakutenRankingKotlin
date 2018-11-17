package com.github.watabee.rakutenranking.common.presentation

import com.github.watabee.rakutenranking.common.core.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class AppPresenter<V : AppView>(
    view: V,
    protected val coroutineDispatchers: AppCoroutineDispatchers
) : CoroutineScope {

    private var _view: V? = view
    protected val view: V get() = _view!!

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = coroutineDispatchers.main + job

    open fun destroy() {
        _view = null
        job.cancel()
    }
}