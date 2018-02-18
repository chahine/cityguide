package com.chahinem.cityguide.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.chahinem.cityguide.R.id
import com.chahinem.cityguide.R.layout
import com.chahinem.cityguide.api.DistanceMatrixResponse
import com.chahinem.cityguide.ui.MainAdapter.MainViewHolder
import com.google.android.gms.location.places.Place
import timber.log.Timber

class MainAdapter : RecyclerView.Adapter<MainViewHolder>() {

  private val items = mutableListOf<MainItem>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
    return MainViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(layout.item_place, parent, false))
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

    private val name = itemView.findViewById<TextView>(id.name)
    private val distance = itemView.findViewById<TextView>(id.distance)
    private val image = itemView.findViewById<ImageView>(id.image)
    private val ratingBar = itemView.findViewById<RatingBar>(id.ratingBar)

    fun bind(item: MainItem) {
      item.place.let {
        Timber.d("--> placeId:${it.id}")
        name.text = it.name
        ratingBar.rating = it.rating
      }
      distance.text = item.matrixResponse.rows?.firstOrNull()
          ?.elements?.firstOrNull()
          ?.distance?.text
    }
  }

  class MainItem(val place: Place, val matrixResponse: DistanceMatrixResponse)
}