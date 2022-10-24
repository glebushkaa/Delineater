package com.gleb.delineater

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gleb.delineater.DrawFragment.Companion.paint
import com.gleb.delineater.DrawFragment.Companion.paintStroke
import com.gleb.delineater.DrawFragment.Companion.path
import com.gleb.delineater.data.models.PaintModel

class PaintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var params: LayoutParams? = null

    companion object {
        var paintList = arrayListOf<PaintModel>()
        var colorList = arrayListOf<Int>()
    }

    init {
        paint.apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeWidth = 10f
        }
        LayoutParams.MATCH_PARENT.let {
            params = LayoutParams(it, it)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                val newPaint = Paint()
                newPaint.apply {
                    isAntiAlias = true
                    color = Color.BLACK
                    style = Paint.Style.STROKE
                    strokeJoin = Paint.Join.ROUND
                    strokeWidth = paintStroke
                }
                paintList.add(PaintModel(path, newPaint))
            }
            else -> {
                return false
            }
        }
        postInvalidate()
        return false
    }

    override fun onDraw(canvas: Canvas?) {
        paintList.indices.forEach {
            canvas?.drawPath(paintList[it].path, paintList[it].paint)
            invalidate()
        }
    }

}