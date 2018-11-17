package com.github.watabee.rakutenranking.common.data

import com.github.watabee.rakutenranking.common.core.AppCoroutineDispatchers
import com.github.watabee.rakutenranking.common.presentation.RankingPresenter
import com.github.watabee.rakutenranking.common.presentation.RankingView
import io.ktor.client.HttpClient

expect class AppModule {
    val coroutineDispatchers: AppCoroutineDispatchers

    val httpClient: HttpClient

    val api: RakutenApi

    val browsingHistoryRepository: BrowsingHistoryRepository

    fun provideRankingPresenter(view: RankingView): RankingPresenter
}