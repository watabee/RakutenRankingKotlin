package com.github.watabee.rakutenranking.common.presentation

import com.github.watabee.rakutenranking.common.core.AppCoroutineDispatchers
import com.github.watabee.rakutenranking.common.data.BrowsingHistoryRepository
import com.github.watabee.rakutenranking.common.data.RakutenApi
import com.github.watabee.rakutenranking.common.data.RankingItem
import com.github.watabee.rakutenranking.common.data.RankingItemMapper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class RankingPresenter(
    private val api: RakutenApi,
    private val repository: BrowsingHistoryRepository,
    view: RankingView,
    coroutineDispatchers: AppCoroutineDispatchers
) : AppPresenter<RankingView>(view, coroutineDispatchers) {

    private var isLoading: Boolean = false
        set(value) {
            view.isLoading = value
        }

    private var loadBrowsingHistory = true

    fun findRanking() {
        if (isLoading) {
            return
        }
        isLoading = true

        launch {
            try {
                view.showRanking(api.findRanking().items.map { RankingItemMapper(it) })
                loadBrowsingHistoriesIfNeeded()
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    view.showError()
                }
            }
        }.invokeOnCompletion {
            isLoading = false
        }
    }

    fun loadBrowsingHistoriesIfNeeded() {
        if (loadBrowsingHistory) {
            loadBrowsingHistory = false

            view.showBrowsingHistories(repository.selectAll().map { RankingItemMapper(it) })
        }
    }

    fun onItemClicked(item: RankingItem, watchedAt: Long) {
        repository.insert(
            itemCode = item.itemCode,
            itemName = item.itemName,
            price = item.price,
            itemUrl = item.itemUrl,
            imageUrl = item.imageUrl,
            shopName = item.shopName,
            watchedAt = watchedAt
        )

        view.openItemDetail(item.itemUrl)

        loadBrowsingHistory = true
    }
}