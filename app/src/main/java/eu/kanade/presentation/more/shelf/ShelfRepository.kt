package eu.kanade.presentation.more.shelf

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ShelfRepository(
    private val client: OkHttpClient = OkHttpClient(),
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun getAniListShelf(userName: String): ShelfUiState {
        val query = """
            query (${'$'}userName: String) {
              MediaListCollection(userName: ${'$'}userName, type: MANGA) {
                lists {
                  entries {
                    score
                    media {
                      id
                      title { english romaji }
                      format
                      genres
                      countryOfOrigin
                      coverImage { extraLarge }
                      bannerImage
                      description(asHtml: false)
                      siteUrl
                      averageScore
                    }
                  }
                }
              }
            }
        """.trimIndent()

        val variables = JSONObject().put("userName", userName).toString()
        val body = JSONObject().put("query", query).put("variables", variables).toString()
            .toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("https://graphql.anilist.co")
            .post(body)
            .build()

        return try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: return ShelfUiState()
            parseAniList(responseBody)
        } catch (e: Exception) {
            ShelfUiState()
        }
    }

    private fun parseAniList(responseBody: String): ShelfUiState {
        val root = JSONObject(responseBody)
        val lists = root
            .optJSONObject("data")
            ?.optJSONObject("MediaListCollection")
            ?.optJSONArray("lists")
            ?: return ShelfUiState()

        val all = mutableListOf<ShelfItem>()
        for (i in 0 until lists.length()) {
            val entries = lists.getJSONObject(i).optJSONArray("entries") ?: continue
            for (j in 0 until entries.length()) {
                val entry = entries.getJSONObject(j)
                val media = entry.optJSONObject("media") ?: continue
                val title = media.optJSONObject("title")
                val name = title?.optString("english")?.takeIf { it.isNotBlank() }
                    ?: title?.optString("romaji") ?: "Unknown"
                val coverImage = media.optJSONObject("coverImage")
                    ?.optString("extraLarge") ?: ""
                val bannerImage = media.optString("bannerImage") ?: ""
                val genres = media.optJSONArray("genres")?.let { arr ->
                    (0 until arr.length()).map { arr.getString(it) }
                } ?: emptyList()
                val country = media.optString("countryOfOrigin") ?: ""
                val description = media.optString("description") ?: ""
                val score = entry.optDouble("score", 0.0)
                val siteUrl = media.optString("siteUrl") ?: ""
                val format = media.optString("format") ?: ""

                all.add(
                    ShelfItem(
                        title = name,
                        subtitle = if (country == "KR") "Manhwa" else format,
                        imageUrl = coverImage,
                        description = description,
                        tags = genres.take(5),
                        sourceUrl = siteUrl,
                    ),
                )
            }
        }

        val manhwa = all.filter { it.subtitle == "Manhwa" }
        val manga = all.filter { it.subtitle != "Manhwa" }

        return ShelfUiState(
            featured = all.firstOrNull(),
            manga = manga,
            manhwa = manhwa,
            loading = false,
        )
    }

    suspend fun getLastFmTop(apiKey: String, userName: String): List<ShelfItem> {
        if (apiKey.isBlank()) return emptyList()
        val url = "https://ws.audioscrobbler.com/2.0/?method=user.gettopalbums&user=$userName&api_key=$apiKey&format=json&period=12month&limit=12"
        val request = Request.Builder().url(url).build()

        return try {
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: return emptyList()
            val root = JSONObject(body)
            val albums = root.optJSONObject("topalbums")?.optJSONArray("album") ?: return emptyList()
            val items = mutableListOf<ShelfItem>()
            for (i in 0 until albums.length()) {
                val album = albums.getJSONObject(i)
                val images = album.optJSONArray("image")
                val image = images?.let {
                    (it.length() - 1 downTo 0).map { idx -> it.getJSONObject(idx).optString("#text") }.firstOrNull { s -> s.isNotBlank() }
                } ?: ""
                items.add(
                    ShelfItem(
                        title = album.optString("name"),
                        subtitle = album.optJSONObject("artist")?.optString("name") ?: "",
                        imageUrl = image,
                        tags = listOf("album", "last.fm"),
                        sourceUrl = album.optString("url") ?: "",
                    ),
                )
            }
            items
        } catch (e: Exception) {
            emptyList()
        }
    }
}
