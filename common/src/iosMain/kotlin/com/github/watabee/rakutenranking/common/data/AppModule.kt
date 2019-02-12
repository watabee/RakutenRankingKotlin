package com.github.watabee.rakutenranking.common.data

import com.github.watabee.rakutenranking.common.core.AppCoroutineDispatchers
import com.github.watabee.rakutenranking.common.presentation.RankingPresenter
import com.github.watabee.rakutenranking.common.presentation.RankingView
import com.github.watabee.rakutenranking.db.Database
import com.squareup.sqldelight.drivers.ios.NativeSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.ios.Ios
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

actual class AppModule(rakutenAppId: String) {

    actual val coroutineDispatchers = AppCoroutineDispatchers(main = MainQueueDispatcher())

    actual val httpClient = HttpClient(Ios) {
        install(JsonFeature) {
            serializer = KotlinxSerializer().apply {
                setMapper(RankingResult::class, RankingResult.serializer())
            }
        }
    }

    actual val api = RakutenApi(httpClient, rakutenAppId)

    actual val driver: SqlDriver = NativeSqliteDriver(Database.Schema, databaseName)

    actual val database = Database(driver)

    actual val browsingHistoryRepository = BrowsingHistoryRepository(database)

    actual fun provideRankingPresenter(view: RankingView): RankingPresenter {
        return RankingPresenter(
            api = api,
            repository = browsingHistoryRepository,
            view = view,
            coroutineDispatchers = coroutineDispatchers
        )
    }
}