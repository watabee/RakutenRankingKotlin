//
//  RankingViewController.swift
//  RakutenRanking
//
//  Created by watabee on 2018/10/27.
//  Copyright © 2018 watabee. All rights reserved.
//

import UIKit
import SafariServices
import common
import Keys.RakutenRankingKeys
import SnapKit
import DifferenceKit

class RankingViewController: UIViewController, RankingView {

    private var presenter: RankingPresenter!

    var isLoading: Bool {
        get {
            return refreshControl.isRefreshing
        }
        set {
            if (!newValue) {
                refreshControl.endRefreshing()
            }
            UIApplication.shared.isNetworkActivityIndicatorVisible = newValue
        }
    }

    private let refreshControl = UIRefreshControl()
    private let tableView = UITableView()
    private var headerView: RankingHeaderView!
    
    private var _rankingItems: [PresentationRankingItem] = []
    private var rankingItems: [PresentationRankingItem] {
        get {
            return _rankingItems
        }
        set {
            let changeset = StagedChangeset(source: _rankingItems, target: newValue)
            tableView.reload(using: changeset, with: .fade) { data in
                _rankingItems = data
            }
        }
    }
    
    init(api: RakutenApi, repository: BrowsingHistoryRepository, coroutineDispatchers: AppCoroutineDispatchers) {
        super.init(nibName: nil, bundle: nil)
        self.presenter = RankingPresenter(api: api, repository: repository, view: self, coroutineDispatchers: coroutineDispatchers)
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError()
    }

    deinit {
        presenter.destroy()
    }

    override func loadView() {
        super.loadView()

        view.addSubview(tableView)
        tableView.snp.makeConstraints { (make) -> Void in
            make.top.equalTo(view.safeAreaLayoutGuide.snp.top)
            make.left.equalTo(view.safeAreaLayoutGuide.snp.left)
            make.bottom.equalTo(view.safeAreaLayoutGuide.snp.bottom)
            make.right.equalTo(view.safeAreaLayoutGuide.snp.right)
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        refreshControl.addTarget(self, action: #selector(refresh(sender:)), for: .valueChanged)
        
        tableView.refreshControl = refreshControl

        tableView.dataSource = self
        tableView.delegate = self
        
        tableView.register(RankingItemCell.self)
        tableView.rowHeight = UITableView.automaticDimension
        tableView.estimatedRowHeight = UITableView.automaticDimension
        tableView.cellLayoutMarginsFollowReadableWidth = false
        tableView.separatorInset = .zero
        
        headerView = RankingHeaderView(frame: CGRect(x: 0, y: 0, width: tableView.bounds.width, height: rankingHistoryItemCellSize.height)) { [weak self] in self?.onItemClicked(rankingItem: $0)
        }
        
        presenter.findRanking()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        deselectRow()
        presenter.loadBrowsingHistoriesIfNeeded()
    }
    
    @objc func refresh(sender: UIRefreshControl) {
        presenter.findRanking()
    }
    
    func showRanking(items: [RankingItem]) {
        self.rankingItems = items.compactMap { $0 as? PresentationRankingItem }
    }
    
    func showBrowsingHistories(items: [RankingItem]) {
        if tableView.tableHeaderView == nil && !items.isEmpty {
            tableView.tableHeaderView = headerView
        }
        headerView.update(items: items.compactMap { $0 as? PresentationRankingItem })
    }

    func showError() {

    }

    func openItemDetail(itemUrl: String) {
        guard let url = URL(string: itemUrl) else {
            deselectRow()
            return
        }
        present(SFSafariViewController(url: url), animated: true, completion: nil)
    }
    
    private func deselectRow() {
        if let indexPathForSelectedRow = tableView.indexPathForSelectedRow {
            tableView.deselectRow(at: indexPathForSelectedRow, animated: true)
        }
    }
    
    private func onItemClicked(rankingItem: RankingItem) {
        presenter.onItemClicked(item: rankingItem, watchedAt: Int64(Date().timeIntervalSince1970))
    }
}

extension PresentationRankingItem : Differentiable {
    public var differenceIdentifier: String {
        get {
            return itemCode
        }
    }
    
    public func isContentEqual(to source: PresentationRankingItem) -> Bool {
        return itemName == source.itemName &&
            price == source.price &&
            imageUrl == source.imageUrl &&
            shopName == source.shopName &&
            itemUrl == source.itemUrl
    }
}

extension RankingViewController : UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return rankingItems.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: RankingItemCell = tableView.dequeueReusableCell(for: indexPath)
        cell.bind(item: rankingItems[indexPath.row])
        return cell
    }
}

extension RankingViewController : UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        onItemClicked(rankingItem: rankingItems[indexPath.row])
    }
}

