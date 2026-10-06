package deeroot.dev.flixster

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray
import kotlin.collections.set

private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MoviesFragment : Fragment(), MoviesRecyclerViewAdapter.OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context

        recyclerView.layoutManager = LinearLayoutManager(context)

        updateAdapter(progressBar, recyclerView)
        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        val client = AsyncHttpClient()
        val params = RequestParams()
        val headers = RequestHeaders()
        headers["X-Api-Key"] = API_KEY

        client["https://api.themoviedb.org/3/movie/now_playing", headers, params, object :
            JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JSON
            ) {
                progressBar.hide()

                val dataJSON = json.jsonObject.get("results") as JSONArray
                val parksRawJSON = dataJSON.toString()

                val gson = Gson()
                val arrayParkType = object : TypeToken<List<Movie>>() {}.type
                val models: List<Movie> = gson.fromJson(parksRawJSON, arrayParkType)

                recyclerView.adapter = MoviesRecyclerViewAdapter(models, this@MoviesFragment)

                Log.d("MoviesFragment", "response successful")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                progressBar.hide()
                t?.message?.let {
                    Log.e("MoviesFragment", errorResponse)
                }
            }
        }]
    }

    override fun onItemClick(item: Movie) {
        Toast.makeText(context, "Test: ${item.name}", Toast.LENGTH_SHORT).show()
    }
}
