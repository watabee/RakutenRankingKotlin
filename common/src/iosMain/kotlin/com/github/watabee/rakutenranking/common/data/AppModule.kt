package com.github.watabee.rakutenranking.common.data

import co.touchlab.knarch.DefaultSystemContext
import co.touchlab.multiplatform.architecture.db.sqlite.IosNativeOpenHelperFactory
import com.github.watabee.rakutenranking.common.core.AppCoroutineDispatchers
import com.github.watabee.rakutenranking.common.presentation.RankingPresenter
import com.github.watabee.rakutenranking.common.presentation.RankingView
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

    actual val browsingHistoryRepository =
        BrowsingHistoryRepository(IosNativeOpenHelperFactory(DefaultSystemContext()))

    actual fun provideRankingPresenter(view: RankingView): RankingPresenter {
        return RankingPresenter(
            api = api,
            repository = browsingHistoryRepository,
            view = view,
            coroutineDispatchers = coroutineDispatchers
        )
    }
}