package com.github.watabee.rakutenranking.common.presentation

import com.github.watabee.rakutenranking.common.data.RankingItem

interface RankingView : AppView {

    var isLoading: Boolean

    fun showRanking(items: List<RankingItem>)

    fun showBrowsingHistories(items: List<RankingItem>)

    fun showError()

    fun openItemDetail(itemUrl: String)
}