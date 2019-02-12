package com.github.watabee.rakutenranking.common.data

import com.github.watabee.rakutenranking.db.BrowsingHistory
import com.github.watabee.rakutenranking.db.Database

private const val LIMIT_BROWSING_HISTORY = 10

class BrowsingHistoryRepository(database: Database) {
    private val queries = database.browsingHistoryQueries

    fun selectAll(): List<BrowsingHistory> = queries.selectAll().executeAsList()

    fun insert(
        itemCode: String,
        itemName: String,
        price: String,
        itemUrl: String,
        imageUrl: String?,
        shopName: String,
        watchedAt: Long
    ) {
        queries.transaction {
            queries.insertOrUpdate(
                itemCode = itemCode,
                itemName = itemName,
                itemPrice = price,
                itemUrl = itemUrl,
                imageUrl = imageUrl,
                shopName = shopName,
                watchedAt = watchedAt
            )

            val count = queries.selectCount().executeAsOne()
            if (count > LIMIT_BROWSING_HISTORY) {
                queries.deleteOldestBrowsingHistory()
            }
        }
    }
}
