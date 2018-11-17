//
//  RankingItemCell.swift
//  RakutenRanking
//
//  Created by watabee on 2018/11/11.
//  Copyright © 2018 watabee. All rights reserved.
//

import UIKit
import common
import Nuke

class RankingItemCell : UITableViewCell, ReusableView, NibLoadableView {
    
    @IBOutlet weak var itemImageView: UIImageView!
    @IBOutlet weak var itemNameLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var shopNameLabel: UILabel!

    func bind(item: RankingItem) {
        itemNameLabel.text = item.itemName
        priceLabel.text = item.price
        shopNameLabel.text = item.shopName
        
        if let imageUrl = item.imageUrl, let url = URL(string: imageUrl) {
            Nuke.loadImage(with: url, into: itemImageView)
        }
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()

        Nuke.cancelRequest(for: itemImageView)
        itemImageView.image = nil
    }
}
