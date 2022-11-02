package com.gleb.delineater.ui.customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import com.gleb.delineater.ui.fragments.DrawFragment.Companion.brushColor
import com.gleb.delineater.ui.fragments.DrawFragment.Companion.brushWidth
import com.gleb.delineater.ui.fragments.DrawFragment.Companion.eraserColor
import com.gleb.delineater.ui.fragments.DrawFragment.Companion.isEraserSelected
import com.gleb.delineater.data.entities.PaintEntity
import java.io.File

class PaintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    companion object {
        var paintList = arrayListOf<PaintEntity>()
        var paint = Paint()
        var path = Path()
    }

    init {
        paint.apply {
            isAntiAlias = true
            color = brushColor
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeWidth = brushWidth
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path = Path()
                paint = Paint()
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                selectPaintType()
            }
            else -> {
                return false
            }
        }
        postInvalidate()
        return false
    }

    override fun onDraw(canvas: Canvas?) {
        paintList.forEach {
            canvas?.drawPath(it.path, it.paint)
        }
        invalidate()
    }

    fun resetPaint(){
        brushColor = Color.BLACK
        brushWidth = 10f
        paintList.clear()
    }

    fun resetSurface(){
        paintList.clear()
        path.reset()
    }

    private fun setPaint(paintColor: Int) {
        paint.apply {
            isAntiAlias = true
            color = paintColor
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeWidth = brushWidth
        }
    }

    private fun selectPaintType() {
        if (isEraserSelected) {
            setEraserPaint()
        } else {
            setBrushPaint()
        }
        paintList.add(
            PaintEntity(path, paint)
        )
    }

    private fun setBrushPaint() {
        setPaint(brushColor)
    }

    private fun setEraserPaint() {
        setPaint(eraserColor)
    }

}