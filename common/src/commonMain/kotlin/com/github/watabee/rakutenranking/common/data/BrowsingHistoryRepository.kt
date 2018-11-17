package com.github.watabee.rakutenranking.common.data

import co.touchlab.multiplatform.architecture.db.sqlite.NativeOpenHelperFactory
import com.github.watabee.rakutenranking.db.BrowsingHistory
import com.github.watabee.rakutenranking.db.QueryWrapper
import com.squareup.sqldelight.multiplatform.create

private const val LIMIT_BROWSING_HISTORY = 10

class BrowsingHistoryRepository(
    openHelperFactory: NativeOpenHelperFactory
) {
    private val queryWrapper: QueryWrapper = QueryWrapper(
        database = QueryWrapper.create(name = "RakutenRankingDb", openHelperFactory = openHelperFactory)
    )

    fun selectAll(): List<BrowsingHistory> =
        queryWrapper.browsingHistoryQueries.selectAll().executeAsList()

    fun insert(
        itemCode: String,
        itemName: String,
        price: String,
        itemUrl: String,
        imageUrl: String?,
        shopName: String,
        watchedAt: Long
    ) {
        queryWrapper.browsingHistoryQueries.transaction {

            queryWrapper.browsingHistoryQueries
                .insertOrUpdate(
                    itemCode = itemCode,
                    itemName = itemName,
                    itemPrice = price,
                    itemUrl = itemUrl,
                    imageUrl = imageUrl,
                    shopName = shopName,
                    watchedAt = watchedAt
                )

            val count = queryWrapper.browsingHistoryQueries.selectCount().executeAsOne()
            if (count > LIMIT_BROWSING_HISTORY) {
                queryWrapper.browsingHistoryQueries.deleteOldestBrowsingHistory()
            }
        }
    }
}
