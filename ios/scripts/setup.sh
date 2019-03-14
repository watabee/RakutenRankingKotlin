#!/bin/sh

bundle install
xcodegen generate
bundle exec pod install --repo-update
