package com.miabisuteri.admin.data.github

import com.miabisuteri.admin.BuildConfig
import com.miabisuteri.admin.domain.model.GitHubRelease
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubUpdateDataSource @Inject constructor(
    private val client: OkHttpClient
) {
    /**
     * Checks GitHub Releases API for a newer version.
     * Returns the release info if an update is available, null otherwise.
     *
     * Version comparison: tag names like "v1.0.0" are compared semantically.
     */
    suspend fun checkForUpdate(): GitHubRelease? = withContext(Dispatchers.IO) {
        val owner = BuildConfig.GITHUB_OWNER
        val repo = BuildConfig.GITHUB_REPO
        val url = "https://api.github.com/repos/$owner/$repo/releases/latest"

        val request = Request.Builder()
            .url(url)
            .header("Accept", "application/vnd.github+json")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .build()

        runCatching {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@runCatching null

            val body = response.body?.string() ?: return@runCatching null
            val json = JSONObject(body)

            val tagName = json.optString("tag_name", "")
            val changelog = json.optString("body", "")

            // Find APK asset
            val assets = json.optJSONArray("assets")
            var apkUrl = ""
            if (assets != null) {
                for (i in 0 until assets.length()) {
                    val asset = assets.getJSONObject(i)
                    val name = asset.optString("name", "")
                    if (name.endsWith(".apk")) {
                        apkUrl = asset.optString("browser_download_url", "")
                        break
                    }
                }
            }

            if (apkUrl.isEmpty()) return@runCatching null

            // Compare versions
            val latestVersion = tagName.trimStart('v')
            val currentVersion = BuildConfig.VERSION_NAME

            if (isNewerVersion(latestVersion, currentVersion)) {
                GitHubRelease(
                    tagName = tagName,
                    body = changelog,
                    apkUrl = apkUrl
                )
            } else {
                null
            }
        }.getOrNull()
    }

    /**
     * Returns true if [latest] is newer than [current].
     * Compares semantic version numbers (major.minor.patch).
     */
    private fun isNewerVersion(latest: String, current: String): Boolean {
        val latestParts = latest.split(".").map { it.toIntOrNull() ?: 0 }
        val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }

        val maxLen = maxOf(latestParts.size, currentParts.size)
        for (i in 0 until maxLen) {
            val l = latestParts.getOrElse(i) { 0 }
            val c = currentParts.getOrElse(i) { 0 }
            if (l > c) return true
            if (l < c) return false
        }
        return false
    }
}
