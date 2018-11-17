package com.github.watabee.rakutenranking.presentation.ranking

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.watabee.rakutenranking.R
import com.github.watabee.rakutenranking.common.data.RankingItem
import com.github.watabee.rakutenranking.common.presentation.RankingPresenter
import com.github.watabee.rakutenranking.common.presentation.RankingView
import com.github.watabee.rakutenranking.databinding.ActivityRankingBinding
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.Date

class RankingActivity : AppCompatActivity(), RankingView {

    private val presenter by inject<RankingPresenter> { parametersOf(this) }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<ActivityRankingBinding>(this, R.layout.activity_ranking)
    }

    private val adapter = RankingAdapter(this::onItemClicked)

    override var isLoading: Boolean
        get() = binding.swipeRefreshLayout.isRefreshing
        set(value) {
            binding.swipeRefreshLayout.isRefreshing = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener { presenter.findRanking() }

        binding.recyclerView.also {
            it.adapter = adapter
            it.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        }

        presenter.findRanking()
    }

    override fun onResume() {
        super.onResume()
        presenter.loadBrowsingHistoriesIfNeeded()
    }

    override fun showRanking(items: List<RankingItem>) {
        adapter.updateItems(items)
    }

    override fun showBrowsingHistories(items: List<RankingItem>) {
        adapter.updateHeader(items)
    }

    override fun showError() {
        Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show()
    }

    override fun openItemDetail(itemUrl: String) = openBrowser(itemUrl)

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    private fun onItemClicked(rankingItem: RankingItem) =
        presenter.onItemClicked(rankingItem, Date().time)

    private fun openBrowser(itemUrl: String) {
        CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .addDefaultShareMenuItem()
            .build()
            .launchUrl(this, Uri.parse(itemUrl))
    }
}
