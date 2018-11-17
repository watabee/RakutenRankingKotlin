package com.github.watabee.rakutenranking.common.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.github.watabee.rakutenranking.db.BrowsingHistory
import java.util.Locale

data class BindableRankingItem(
    @Bindable override val itemName: String,
    @Bindable override val imageUrl: String?,
    @Bindable override val price: String,
    @Bindable override val shopName: String,
    override val itemCode: String,
    override val itemUrl: String
) : BaseObservable(), RankingItem

actual object RankingItemMapper {
    actual operator fun invoke(item: RankingResult.Item): RankingItem =
        BindableRankingItem(
            itemName = item.itemName,
            imageUrl = item.mediumImageUrls.firstOrNull() ?: item.smallImageUrls.firstOrNull(),
            price = "¥ ${String.format(Locale.US, "%,d", item.itemPrice.toInt())}",
            shopName = item.shopName,
            itemCode = item.itemCode,
            itemUrl = item.itemUrl
        )

    actual operator fun invoke(browsingHistory: BrowsingHistory): RankingItem =
        BindableRankingItem(
            itemName = browsingHistory.itemName,
            imageUrl = browsingHistory.imageUrl,
            price = browsingHistory.itemPrice,
            shopName = browsingHistory.shopName,
            itemCode = browsingHistory.itemCode,
            itemUrl = browsingHistory.itemUrl
        )
}
