package com.gleb.delineater

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.gleb.delineater.DrawFragment.Companion.paint
import com.gleb.delineater.DrawFragment.Companion.path

class PaintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var params: LayoutParams? = null

    companion object {
        var pathList = arrayListOf<Path>()
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
                pathList.add(path)
            }
            else -> {
                return false
            }
        }
        postInvalidate()
        return false
    }

    override fun onDraw(canvas: Canvas?) {
        for (index in pathList.indices){
            canvas?.drawPath(pathList[index], paint)
            invalidate()
        }
    }

}