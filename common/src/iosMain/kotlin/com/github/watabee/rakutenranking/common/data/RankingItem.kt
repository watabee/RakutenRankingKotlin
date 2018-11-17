package com.github.watabee.rakutenranking.common.data

import com.github.watabee.rakutenranking.common.util.toCurrencyString
import com.github.watabee.rakutenranking.db.BrowsingHistory

data class PresentationRankingItem(
    override val itemName: String,
    override val imageUrl: String?,
    override val price: String,
    override val shopName: String,
    override val itemCode: String,
    override val itemUrl: String
) : RankingItem

actual object RankingItemMapper {
    actual operator fun invoke(item: RankingResult.Item): RankingItem =
        PresentationRankingItem(
            itemName = item.itemName,
            imageUrl = item.mediumImageUrls.firstOrNull() ?: item.smallImageUrls.firstOrNull(),
            price = "¥ ${item.itemPrice.toInt().toCurrencyString()}",
            shopName = item.shopName,
            itemCode = item.itemCode,
            itemUrl = item.itemUrl
        )

    actual operator fun invoke(browsingHistory: BrowsingHistory): RankingItem =
        PresentationRankingItem(
            itemName = browsingHistory.itemName,
            imageUrl = browsingHistory.imageUrl,
            price = browsingHistory.itemPrice,
            shopName = browsingHistory.shopName,
            itemCode = browsingHistory.itemCode,
            itemUrl = browsingHistory.itemUrl
        )
}