//
//  RankingHistoryItemCell.swift
//  RakutenRanking
//
//  Created by watabee on 2018/11/17.
//  Copyright © 2018 watabee. All rights reserved.
//

import UIKit
import common
import Nuke

let rankingHistoryItemCellSize: CGSize = {
    guard let cell = Bundle.main.loadNibNamed(RankingHistoryItemCell.nibName, owner: nil, options: nil)?.first as? RankingHistoryItemCell else {
        return .zero
    }
    return cell.bounds.size
}()

class RankingHistoryItemCell: UICollectionViewCell, ReusableView, NibLoadableView {

    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var itemNameLabel: UILabel!

    override var isHighlighted: Bool {
        didSet {
            if (isHighlighted) {
                backgroundColor = UIColor(white: 217.0 / 255, alpha: 1)
            } else {
                backgroundColor = .clear
            }
        }
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        Nuke.cancelRequest(for: imageView)
        imageView.image = nil
    }
    
    func bind(item: RankingItem) {
        itemNameLabel.text = item.itemName
        
        if let imageUrl = item.imageUrl, let url = URL(string: imageUrl) {
            Nuke.loadImage(with: url, into: imageView)
        }
    }
}
