package com.chahinem.cityguide.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import timber.log.Timber

class CustomSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnTouchListener {

  var positionChangeListener: PositionChangeListener? = null

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

  private var position: Int = 0
  private var sliderStart = paddingStart
  private var slideAnimator: ValueAnimator? = null

  init {
    setOnTouchListener(this)
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

  override fun onTouch(v: View?, event: MotionEvent?): Boolean {
    when (event?.action) {
      ACTION_MOVE -> {
        sliderStart = Math.min(
            (width - paddingEnd - sliderWidth).toInt(),
            Math.max(paddingStart.toFloat(), event.x - width.toFloat() / 6).toInt())
        val previousPosition = position
        computePosition(event)
        if (previousPosition != position) {
          positionChangeListener?.onPositionChanged(position)
        }
      }
      ACTION_UP -> {
        val target = paddingStart + (position * sliderWidth).toInt()
        slideAnimator?.cancel()
        slideAnimator = ValueAnimator.ofInt(sliderStart, target).apply {
          duration = DEFAULT_ANIM_DURATION
          interpolator = DEFAULT_INTERPOLATOR
          addUpdateListener { animation ->
            sliderStart = animation.animatedValue as Int
            invalidate()
          }
          start()
        }
      }
    }
    invalidate()
    return true
  }

  private fun computePosition(event: MotionEvent) {
    position = Math.min(2.0, Math.max(0.0, Math.floor(event.x.toDouble() / sliderWidth))).toInt()
  }

  interface PositionChangeListener {
    fun onPositionChanged(position: Int)
  }

  companion object {
    private const val DEFAULT_ANIM_DURATION = 200L
    private val DEFAULT_INTERPOLATOR = AccelerateDecelerateInterpolator()
  }
}