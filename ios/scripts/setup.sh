#!/bin/sh

RAKUTEN_APP_ID=$1

bundle install
bundle exec pod keys set "RakutenAppId" "$RAKUTEN_APP_ID"
xcodegen generate
bundle exec pod install --repo-update
