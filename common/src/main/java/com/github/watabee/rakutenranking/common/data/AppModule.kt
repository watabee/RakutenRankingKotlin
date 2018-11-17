package com.github.watabee.rakutenranking.common.data

import android.content.Context
import co.touchlab.multiplatform.architecture.db.sqlite.AndroidNativeOpenHelperFactory
import com.github.watabee.rakutenranking.common.core.AppCoroutineDispatchers
import com.github.watabee.rakutenranking.common.presentation.RankingPresenter
import com.github.watabee.rakutenranking.common.presentation.RankingView
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.coroutines.Dispatchers
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import timber.log.info

actual class AppModule(rakutenAppId: String, context: Context) {

    actual val coroutineDispatchers = AppCoroutineDispatchers(main = Dispatchers.Main)

    actual val httpClient = HttpClient(OkHttp) {
        engine {

            addInterceptor(
                HttpLoggingInterceptor(
                    HttpLoggingInterceptor.Logger { message ->
                        Timber.tagged("OkHttp").info { message }
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

    actual val api = RakutenApi(httpClient, rakutenAppId)

    actual val browsingHistoryRepository =
        BrowsingHistoryRepository(AndroidNativeOpenHelperFactory(context))

    actual fun provideRankingPresenter(view: RankingView): RankingPresenter {
        return RankingPresenter(
            api = api,
            repository = browsingHistoryRepository,
            view = view,
            coroutineDispatchers = coroutineDispatchers
        )
    }
}