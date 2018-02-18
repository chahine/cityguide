package com.chahinem.cityguide.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.chahinem.cityguide.R
import com.chahinem.cityguide.ui.MainAdapter.MainViewHolder
import com.chahinem.cityguide.ui.PlaceTypeEnum.BAR
import com.chahinem.cityguide.ui.PlaceTypeEnum.BISTRO
import com.chahinem.cityguide.ui.PlaceTypeEnum.CAFE
import com.google.android.gms.location.places.Place

class MainAdapter : RecyclerView.Adapter<MainViewHolder>() {

  var type: PlaceTypeEnum = BAR

  private val items = mutableListOf<Item>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
    return MainViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_place, parent, false))
  }

  override fun onBindViewHolder(holder: MainViewHolder?, position: Int) {
    holder?.let {
      if (it.adapterPosition != RecyclerView.NO_POSITION) {
        it.bind(items.filter { it.type == type }[holder.adapterPosition])
      }
    }
  }

  fun swapData(data: Collection<Item>?) {
    items.clear()
    data?.let { items.addAll(it) }
    notifyDataSetChanged()
  }

  override fun getItemCount() = items.filter { it.type == type }.size

  inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val name = itemView.findViewById<TextView>(R.id.name)
    private val distance = itemView.findViewById<TextView>(R.id.distance)
    private val image = itemView.findViewById<ImageView>(R.id.image)
    private val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)

    fun bind(item: Item) {
      item.place.let {
        name.text = it.name
        ratingBar.rating = it.rating
      }
      distance.text = item.distanceText
      when (item.type) {
        BAR -> image.setImageResource(R.drawable.ic_local_bar)
        BISTRO -> image.setImageResource(R.drawable.ic_restaurant)
        CAFE -> image.setImageResource(R.drawable.ic_local_cafe)
      }
    }
  }

  class Item(val type: PlaceTypeEnum, val place: Place, val distanceText: String?)
}
