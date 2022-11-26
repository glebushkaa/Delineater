package com.gleb.delineater.ui.customViews


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gleb.delineater.data.entities.PaintEntity
import com.gleb.delineater.ui.types.PaintType

private const val DEFAULT_BRUSH_SIZE = 10f

class PaintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var paint = Paint()
    private var path = Path()
    private var paintList = arrayListOf<PaintEntity>()
    private var deletedItemsList = arrayListOf<PaintEntity>()

    var brushWidth = DEFAULT_BRUSH_SIZE
    var brushColor = Color.BLACK
    var eraserColor = Color.WHITE
    var paintType: PaintType = PaintType.Brush

    init {
        setPaint(brushColor)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onDownAction(x, y)
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
    }

    fun resetPaint() {
        brushColor = Color.BLACK
        brushWidth = DEFAULT_BRUSH_SIZE
        paintList.clear()
    }

    fun resetSurface() {
        paintList.clear()
        deletedItemsList.clear()
        path.reset()
        postInvalidate()
    }

    fun removeLastStep() {
        if (paintList.isNotEmpty()) {
            deletedItemsList.add(paintList.removeLast())
            postInvalidate()
        }
    }

    fun restoreDeletedStep() {
        if (deletedItemsList.isNotEmpty()) {
            paintList.add(deletedItemsList.removeLast())
            postInvalidate()
        }
    }

    fun updateEraseColor(color: Int) {
        paintList.forEach {
            if (it.paintType == PaintType.Eraser) {
                it.paint.color = color
            }
        }
        invalidate()
    }

    private fun onDownAction(x: Float, y: Float) {
        path = Path()
        paint = Paint()
        path.moveTo(x, y)
        paintList.add(PaintEntity(path, paint, paintType))
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
        when (paintType) {
            is PaintType.Brush -> setPaint(brushColor)
            is PaintType.Eraser -> setPaint(eraserColor)
        }
        paintList.removeLast()
        paintList.add(PaintEntity(path, paint, paintType))
    }
}