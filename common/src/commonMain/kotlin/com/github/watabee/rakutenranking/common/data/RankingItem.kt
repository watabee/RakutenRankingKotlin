package com.github.watabee.rakutenranking.common.data

import com.github.watabee.rakutenranking.db.BrowsingHistory

interface RankingItem {
    val itemCode: String
    val itemName: String
    val imageUrl: String?
    val price: String
    val shopName: String
    val itemUrl: String
}

expect object RankingItemMapper {
    operator fun invoke(item: RankingResult.Item): RankingItem
    operator fun invoke(browsingHistory: BrowsingHistory): RankingItem
}
