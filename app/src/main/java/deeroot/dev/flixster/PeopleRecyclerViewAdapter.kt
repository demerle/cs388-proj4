package deeroot.dev.flixster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class PeopleRecyclerViewAdapter(
    private val people: List<Person>,
    private val mListener: OnListFragmentInteractionListener?,
) : RecyclerView.Adapter<PeopleRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_person, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = people[position]
        holder.mItem = person
        holder.mPersonName.text = person.name

        Glide.with(holder.mView)
            .load(person.profileImageUrl)
            .transform(CenterCrop())
            .into(holder.mPersonImage)

        holder.mView.setOnClickListener {
            holder.mItem?.let { item ->
                mListener?.onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = people.size

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: Person? = null
        val mPersonImage: ImageView = mView.findViewById(R.id.person_image)
        val mPersonName: TextView = mView.findViewById(R.id.person_name)

        override fun toString(): String {
            return super.toString() + " '" + mPersonName.text + "'"
        }
    }

    interface OnListFragmentInteractionListener {
        fun onItemClick(item: Person)
    }
}
