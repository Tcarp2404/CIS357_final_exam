package edu.gvsu.cis.cis357_carpenter_final_exam.data.network

import edu.gvsu.cis.cis357_carpenter_final_exam.data.model.DisneyApiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object DisneyApiService {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    suspend fun getCharacters(url: String = "https://api.disneyapi.dev/character"): DisneyApiResponse {
        return client.get(url).body()
    }

    suspend fun searchByName(name: String): DisneyApiResponse {
        return client.get("https://api.disneyapi.dev/character") {
            parameter("name", name)
        }.body()
    }

    suspend fun searchByFilm(film: String): DisneyApiResponse {
        return client.get("https://api.disneyapi.dev/character") {
            parameter("films", film)
        }.body()
    }
}