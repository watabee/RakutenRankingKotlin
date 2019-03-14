package com.github.watabee.rakutenranking.common.data

import android.content.Context
import com.github.watabee.rakutenranking.common.core.AppCoroutineDispatchers
import com.github.watabee.rakutenranking.common.presentation.RankingPresenter
import com.github.watabee.rakutenranking.common.presentation.RankingView
import com.github.watabee.rakutenranking.db.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.coroutines.Dispatchers
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

actual class AppModule(context: Context) {

    actual val coroutineDispatchers = AppCoroutineDispatchers(main = Dispatchers.Main)

    actual val httpClient = HttpClient(OkHttp) {
        engine {

            addInterceptor(
                HttpLoggingInterceptor(
                    HttpLoggingInterceptor.Logger { message ->
                        Timber.tag("OkHttp").i(message)
                    }
                ).apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer().apply {
                setMapper(RankingResult::class, RankingResult.serializer())
            }
        }
    }

    actual val api = RakutenApi(httpClient)

    actual val driver: SqlDriver = AndroidSqliteDriver(Database.Schema, context, databaseName)

    actual val database: Database = Database(driver)

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