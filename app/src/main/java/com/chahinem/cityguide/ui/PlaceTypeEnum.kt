package com.chahinem.cityguide.ui

enum class PlaceTypeEnum constructor(private val value: String) {
  BAR("bar"),
  BISTRO("bistro"),
  CAFE("cafe");

  override fun toString() = value
}