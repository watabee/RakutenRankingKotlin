# RakutenRanking

This is a sample project for kotlin native, shows information of the top selling items on Rakuten Ichiba using [Rakuten Ichiba Ranking API](https://webservice.rakuten.co.jp/api/ichibaitemranking/) (<a href="https://webservice.rakuten.co.jp/" target="_blank">Supported by Rakuten Developers</a>).

## Setup

Open `gradle.properties`, set `RAKUTEN_APP_ID`.

It's the application key for [Rakuten Web Service](https://webservice.rakuten.co.jp/guide/).

### iOS

Install the latest version of the following libraries.

- [bundler](https://bundler.io/)
- [XcodeGen](https://github.com/yonaskolb/XcodeGen)

Run following command.

```
$ ./gradlew :common:setupIos
```
