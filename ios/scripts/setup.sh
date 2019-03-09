#!/bin/sh

RAKUTEN_APP_ID=$1

bundle install
xcodegen generate
bundle exec pod keys set "RakutenAppId" "$RAKUTEN_APP_ID"
bundle exec pod install --repo-update
