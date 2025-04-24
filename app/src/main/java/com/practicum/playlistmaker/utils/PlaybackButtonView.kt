package com.practicum.playlistmaker.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.practicum.playlistmaker.R


class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
    ): View(context, attrs, defStyleAttr, defStyleRes) {

    private val imagePlay: Bitmap?
    private val imagePause: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var imagePadding: Float = 0f
    private var isPlaying: Boolean = false
    private var imageToDraw: Bitmap? = null
    var onClickPlayBack: (() -> Unit)? = null

    init {
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.PlaybackButtonView,
                defStyleAttr,
                defStyleRes
            ).apply {
                try {
                    imagePlay = getDrawable(R.styleable.PlaybackButtonView_imageSrcPlay)?.toBitmap()
                    imagePause = getDrawable(R.styleable.PlaybackButtonView_imageSrcPause)?.toBitmap()
                    imagePadding = getDimension(R.styleable.PlaybackButtonView_imagePadding, 0f)
                } finally {
                    recycle()
                }
            }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(imagePadding, imagePadding, measuredWidth.toFloat() - imagePadding, measuredHeight.toFloat() - imagePadding)
    }

    override fun onDraw(canvas: Canvas) {
         imageToDraw = if (isPlaying) imagePause else imagePlay
        imageToDraw?.let { canvas.drawBitmap(it, null, imageRect, null) }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
               togglePlayState()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun isPlaying(switcher: Boolean) {
        isPlaying = switcher
        invalidate()
    }

    private fun togglePlayState() {
        isPlaying = !isPlaying
        invalidate()
        onClickPlayBack?.invoke()
    }

}