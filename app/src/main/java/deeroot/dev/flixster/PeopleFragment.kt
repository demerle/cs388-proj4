package deeroot.dev.flixster

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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

class PeopleFragment : Fragment(), PeopleRecyclerViewAdapter.OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_person_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        updateAdapter(progressBar, recyclerView)
        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        client["https://api.themoviedb.org/3/person/popular", params, object :
            JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JSON
            ) {
                progressBar.hide()

                val dataJSON = json.jsonObject.get("results") as JSONArray
                val peopleRawJSON = dataJSON.toString()

                val gson = Gson()
                val arrayPersonType = object : TypeToken<List<Person>>() {}.type
                val models: List<Person> = gson.fromJson(peopleRawJSON, arrayPersonType)

                recyclerView.adapter = PeopleRecyclerViewAdapter(models, this@PeopleFragment)

                Log.d("PeopleFragment", "response successful")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                progressBar.hide()
                t?.message?.let {
                    Log.e("PeopleFragment", errorResponse)
                }
            }
        }]
    }

    override fun onItemClick(item: Person) {
        val intent = android.content.Intent(context, PersonDetailActivity::class.java)
        intent.putExtra(PersonDetailActivity.EXTRA_PERSON, Gson().toJson(item))
        startActivity(intent)
    }
}
