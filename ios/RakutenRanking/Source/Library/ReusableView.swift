//
//  ReusableView.swift
//  RakutenRanking
//
//  Created by watabee on 2018/11/12.
//  Copyright © 2018 watabee. All rights reserved.
//

import UIKit

protocol ReusableView: class {
    static var reuseIdentifier: String { get }
}

extension ReusableView where Self: UIView {
    static var reuseIdentifier: String {
        return String(describing: Self.self)
    }
}
