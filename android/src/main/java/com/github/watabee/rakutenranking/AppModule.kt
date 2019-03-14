package com.github.watabee.rakutenranking

import com.github.watabee.rakutenranking.common.data.AppModule
import com.github.watabee.rakutenranking.common.presentation.RankingView
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val appModule = module {

    single {
        AppModule(
            context = androidContext()
        )
    }

    factory { (view: RankingView) ->
        get<AppModule>().provideRankingPresenter(view)
    }
}
