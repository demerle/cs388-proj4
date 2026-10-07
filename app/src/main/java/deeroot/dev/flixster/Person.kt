package deeroot.dev.flixster

import com.google.gson.annotations.SerializedName

/**
 * Model for a single trending person from the TMDB
 * trending/person/week endpoint.
 *
 * SerializedName tags MUST match the JSON response for the
 * object to correctly parse with the gson library.
 */
class Person {

    @JvmField
    @SerializedName("name")
    var name: String? = null

    // Path of the person's headshot (appended to the TMDB image base url)
    @JvmField
    @SerializedName("profile_path")
    var profilePath: String? = null

    // Movies/tv shows this person is known for
    @SerializedName("known_for")
    var knownFor: List<KnownFor>? = null

    companion object {
        // TMDB image CDN base url
        const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"

        // Widths for the profile / poster paths
        const val PROFILE_SIZE = "w185"
        const val POSTER_SIZE = "w300"
    }

    // Convenience property to access the full headshot url
    val profileImageUrl: String?
        get() = profilePath?.let { TMDB_IMAGE_BASE_URL + PROFILE_SIZE + it }

    // Comma-joined list of titles this person is known for
    val knownForStr: String
        get() = knownFor?.mapNotNull { it.title }?.joinToString(", ") ?: ""

    /**
     * Model for a movie/show the person is known for.
     */
    class KnownFor {
        @JvmField
        @SerializedName("title")
        var title: String? = null

        @JvmField
        @SerializedName("overview")
        var overview: String? = null

        // Path of the show's poster (appended to the TMDB image base url)
        @JvmField
        @SerializedName("poster_path")
        var posterPath: String? = null

        // Convenience property to access the full poster url
        val posterImageUrl: String?
            get() = posterPath?.let { TMDB_IMAGE_BASE_URL + POSTER_SIZE + it }
    }
}
