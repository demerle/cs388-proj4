package deeroot.dev.flixster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MoviesRecyclerViewAdapter(
    private val movies: List<Movie>,
    private val mListener: OnListFragmentInteractionListener?,
) : RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.mItem = movie
        holder.mMovieName.text = movie.name
        holder.mMovieDescription.text = movie.description

        Glide.with(holder.mView)
            .load(movie.imageUrl)
            .centerCrop()
            .into(holder.mMovieImage)

        holder.mView.setOnClickListener {
            holder.mItem?.let { item ->
                mListener?.onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: Movie? = null
        val mMovieImage: ImageView = mView.findViewById(R.id.movie_image)
        val mMovieName: TextView = mView.findViewById(R.id.movie_name)
        val mMovieDescription: TextView = mView.findViewById(R.id.movie_description)

        override fun toString(): String {
            return super.toString() + " '" + mMovieName.text + "'"
        }
    }

    interface OnListFragmentInteractionListener {
        fun onItemClick(item: Movie)
    }
}
