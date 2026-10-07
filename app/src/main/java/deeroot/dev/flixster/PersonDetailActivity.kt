package deeroot.dev.flixster

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.gson.Gson

class PersonDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_person_detail)

        val person = Gson().fromJson(
            intent.getStringExtra(EXTRA_PERSON),
            Person::class.java
        )

        val profileImage = findViewById<ImageView>(R.id.detail_image_profile)
        val posterImage = findViewById<ImageView>(R.id.detail_image_poster)
        val nameText = findViewById<TextView>(R.id.detail_name)
        val knownForText = findViewById<TextView>(R.id.detail_known_for)
        val descriptionText = findViewById<TextView>(R.id.detail_description)

        // Profile headshot
        Glide.with(this)
            .load(person.profileImageUrl)
            .transform(CenterCrop())
            .into(profileImage)

        // Poster of the first movie the person is known for
        val firstKnown = person.knownFor?.firstOrNull { !it.posterImageUrl.isNullOrBlank() }
        Glide.with(this)
            .load(firstKnown?.posterImageUrl)
            .transform(CenterCrop())
            .into(posterImage)

        nameText.text = person.name
        knownForText.text = getString(R.string.known_for_prefix, person.knownForStr)

        // Description: overview of the first known-for movie that has one
        descriptionText.text = person.knownFor
            ?.firstOrNull { !it.overview.isNullOrBlank() }
            ?.overview
            ?: ""
    }

    companion object {
        const val EXTRA_PERSON = "extra_person"
    }
}
