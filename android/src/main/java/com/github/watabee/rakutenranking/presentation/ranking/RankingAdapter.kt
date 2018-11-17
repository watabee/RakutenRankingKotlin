package com.github.watabee.rakutenranking.presentation.ranking

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.watabee.rakutenranking.R
import com.github.watabee.rakutenranking.common.data.RankingItem
import com.github.watabee.rakutenranking.databinding.ListItemRankingBinding
import com.github.watabee.rakutenranking.databinding.ListItemRankingHeaderBinding
import com.github.watabee.rakutenranking.databinding.ListItemRankingHistoryBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.databinding.BindableItem

internal class RankingAdapter(
    private val onItemClicked: (RankingItem) -> Unit
) : GroupAdapter<ViewHolder>() {

    private val section = Section()
    private val rankingHeader = RankingHeader(onItemClicked)

    init {
        add(section)
        section.setHeader(rankingHeader)
        setOnItemClickListener { item, _ -> (item as? Ranking)?.item?.let(onItemClicked) }
    }

    fun updateItems(items: List<RankingItem>) = section.update(items.map(::Ranking))

    fun updateHeader(items: List<RankingItem>) = rankingHeader.update(items)

    private class Ranking(
        val item: RankingItem
    ) : BindableItem<ListItemRankingBinding>(item.hashCode().toLong()) {

        override fun getLayout(): Int = R.layout.list_item_ranking

        override fun bind(viewBinding: ListItemRankingBinding, position: Int) {
            viewBinding.item = item
        }
    }

    private class RankingHeader(
        private val onItemClicked: (RankingItem) -> Unit
    ) : BindableItem<ListItemRankingHeaderBinding>(0) {
        private val section = Section()

        override fun getLayout(): Int = R.layout.list_item_ranking_header

        override fun bind(viewBinding: ListItemRankingHeaderBinding, position: Int) {
            viewBinding.recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = GroupAdapter<ViewHolder>().apply {
                    add(section)
                    setOnItemClickListener { item, _ ->
                        (item as? RankingHistory)?.let { onItemClicked(item.item) }
                    }
                }
            }
        }

        fun update(items: List<RankingItem>) {
            section.update(items.map(::RankingHistory))
        }

        override fun isClickable(): Boolean = false
    }

    private class RankingHistory(val item: RankingItem) : BindableItem<ListItemRankingHistoryBinding>() {
        override fun getLayout(): Int = R.layout.list_item_ranking_history

        override fun bind(viewBinding: ListItemRankingHistoryBinding, position: Int) {
            viewBinding.item = item
        }
    }
}