//
//  Int+Extension.swift
//  RakutenRanking
//
//  Created by watabee on 2018/11/12.
//  Copyright © 2018 watabee. All rights reserved.
//

import Foundation

let currencyFormatter: NumberFormatter = {
    let fomatter = NumberFormatter()
    fomatter.numberStyle = NumberFormatter.Style.decimal
    return fomatter
}()

extension Int {
    func toCurrencyString() -> String {
        return currencyFormatter.string(from: NSNumber(value: self)) ?? "\(self)"
    }
}
