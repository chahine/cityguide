package com.chahinem.cityguide.ui.custom

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

class SliderOutlineProvider(val width: Int,
                            val height: Int,
                            val radius: Float) : ViewOutlineProvider() {
  override fun getOutline(view: View?, outline: Outline?) {
    outline?.setRoundRect(0, 0, width, height, radius)
  }
}