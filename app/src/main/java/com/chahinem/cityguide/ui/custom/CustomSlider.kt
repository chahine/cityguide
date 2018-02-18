package com.chahinem.cityguide.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.widget.FrameLayout
import timber.log.Timber

class CustomSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val pinkPaint = Paint().apply {
    isAntiAlias = true
    color = Color.parseColor("#CE488B")
  }

  private val whitePaint = Paint().apply {
    isAntiAlias = true
    color = Color.parseColor("#ffffff")
  }

  private var sliderWidth: Float = 0.0f
    get() {
      return (width - paddingStart - paddingEnd).toFloat() / 3
    }

  private var sliderStart = paddingStart

  init {
    setOnTouchListener { _, event ->
      Timber.d("--> touch: $event")
      when (event.action) {
        ACTION_MOVE -> {
          sliderStart = Math.min(
              (width - paddingEnd - sliderWidth).toInt(),
              Math.max(paddingStart.toFloat(), event.x - width.toFloat() / 6).toInt())
        }
        ACTION_UP -> {

        }
      }
      invalidate()
      return@setOnTouchListener true
    }
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    val radius = (height - paddingBottom - paddingTop).toFloat() / 2
    val sliderHeight = (height - paddingBottom).toFloat()
    canvas?.drawRoundRect(
        paddingStart.toFloat(),
        paddingTop.toFloat(),
        (width - paddingEnd).toFloat(),
        sliderHeight,
        radius,
        radius,
        pinkPaint
    )

    canvas?.drawRoundRect(
        sliderStart.toFloat(),
        paddingTop.toFloat(),
        sliderWidth + sliderStart,
        sliderHeight,
        radius,
        radius,
        whitePaint
    )
  }

  inline val Float.dp: Int get() = (this * resources.displayMetrics.density).toInt()
}