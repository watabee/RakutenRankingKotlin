package com.github.watabee.rakutenranking.common.data

import com.github.watabee.rakutenranking.common.core.AppCoroutineDispatchers
import com.github.watabee.rakutenranking.common.presentation.RankingPresenter
import com.github.watabee.rakutenranking.common.presentation.RankingView
import com.github.watabee.rakutenranking.db.Database
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.HttpClient

internal val databaseName = "RakutenRankingDb.db"

expect class AppModule {
    val coroutineDispatchers: AppCoroutineDispatchers

    val httpClient: HttpClient

    val api: RakutenApi

    val driver: SqlDriver

    val database: Database

    val browsingHistoryRepository: BrowsingHistoryRepository

    fun provideRankingPresenter(view: RankingView): RankingPresenter
}