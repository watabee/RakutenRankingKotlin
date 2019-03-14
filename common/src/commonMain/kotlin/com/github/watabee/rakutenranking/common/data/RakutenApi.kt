package com.github.watabee.rakutenranking.common.data

import com.github.watabee.rakutenranking.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.request.get

private const val apiEndpoint = "https://app.rakuten.co.jp"

class RakutenApi(private val client: HttpClient) {

    suspend fun findRanking(): RankingResult {
        return client.get("$apiEndpoint/services/api/IchibaItem/Ranking/20170628") {
            url {
                parameters.append("format", "json")
                parameters.append("formatVersion", "2")
                parameters.append("applicationId", BuildKonfig.RAKUTEN_APP_ID)
            }
        }
    }
}