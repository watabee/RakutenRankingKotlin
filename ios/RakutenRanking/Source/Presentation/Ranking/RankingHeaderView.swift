//
//  RankingHeaderView.swift
//  RakutenRanking
//
//  Created by watabee on 2018/11/17.
//  Copyright © 2018 watabee. All rights reserved.
//

import UIKit
import common
import DifferenceKit

class RankingHeaderView : UIView {

    private let collectionView: UICollectionView
    private var onItemClicked: ((RankingItem) -> Void)? = nil
    
    private var _rankingHistoryItems: [PresentationRankingItem] = []
    private var rankingHistoryItems: [PresentationRankingItem] {
        get {
            return _rankingHistoryItems
        }
        set {
            let changeset = StagedChangeset(source: _rankingHistoryItems, target: newValue)
            collectionView.reload(using: changeset) { data in
                _rankingHistoryItems = data
            }
        }
    }
    
    convenience init(frame: CGRect, onItemClicked: @escaping (RankingItem) -> Void) {
        self.init(frame: frame)
        self.onItemClicked = onItemClicked
    }

    override init(frame: CGRect) {
        let collectionViewLayout = UICollectionViewFlowLayout()
        collectionViewLayout.scrollDirection = .horizontal
        collectionViewLayout.itemSize = rankingHistoryItemCellSize
        collectionViewLayout.minimumLineSpacing = 0
        collectionViewLayout.minimumInteritemSpacing = 0
        
        collectionView = UICollectionView(frame: frame, collectionViewLayout: collectionViewLayout)
        collectionView.backgroundColor = .white
        collectionView.register(RankingHistoryItemCell.self)
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.autoresizingMask = [.flexibleWidth, .flexibleHeight]

        super.init(frame: frame)
        
        addSubview(collectionView)
        
        let height = 1.0 / UIScreen.main.scale
        let view = UIView(frame: CGRect(x: 0, y: rankingHistoryItemCellSize.height - height, width: frame.width, height: height))
        view.backgroundColor = UIColor(white: 217.0 / 255, alpha: 1)
        view.autoresizingMask = [.flexibleTopMargin, .flexibleWidth]
        addSubview(view)
        
        collectionView.delegate = self
        collectionView.dataSource = self
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError()
    }
    
    func update(items: [PresentationRankingItem]) {
        self.rankingHistoryItems = items
    }
}

extension RankingHeaderView : UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return rankingHistoryItems.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell: RankingHistoryItemCell = collectionView.dequeueReusableCell(for: indexPath)
        cell.bind(item: rankingHistoryItems[indexPath.item])
        return cell
    }
}

extension RankingHeaderView : UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        onItemClicked?(rankingHistoryItems[indexPath.item])
    }
}
