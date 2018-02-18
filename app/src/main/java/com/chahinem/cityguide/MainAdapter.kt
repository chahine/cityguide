package com.chahinem.cityguide

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.chahinem.cityguide.MainAdapter.MainViewHolder
import com.google.android.gms.location.places.Place

class MainAdapter : RecyclerView.Adapter<MainViewHolder>() {

  private val items = mutableListOf<MainItem>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
    return MainViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_place, parent, false))
  }

  override fun onBindViewHolder(holder: MainViewHolder?, position: Int) {
    holder?.let {
      if (it.adapterPosition != RecyclerView.NO_POSITION) {
        it.bind(items[holder.adapterPosition])
      }
    }
  }

  fun swapData(data: Collection<MainItem>?) {
    items.clear()
    data?.let { items.addAll(it) }
    notifyDataSetChanged()
  }

  override fun getItemCount() = items.size

  inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val name = itemView.findViewById<TextView>(R.id.name)
    private val distance = itemView.findViewById<TextView>(R.id.distance)
    private val image = itemView.findViewById<ImageView>(R.id.image)
    private val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)

    fun bind(item: MainItem) {
      item.place.let {
        name.text = it.name
        ratingBar.rating = it.rating
      }
    }
  }

  class MainItem(val place: Place)
}