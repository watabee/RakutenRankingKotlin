//
//  NibLoadableView.swift
//  RakutenRanking
//
//  Created by watabee on 2018/11/12.
//  Copyright © 2018 watabee. All rights reserved.
//

import UIKit

protocol NibLoadableView: class {
    static var nibName: String { get }
}

extension NibLoadableView where Self: UIView {
    static var nibName: String {
        return String(describing: Self.self)
    }
}
