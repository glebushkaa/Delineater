package com.gleb.delineater.data.entities

import android.graphics.Paint
import android.graphics.Path
import com.gleb.delineater.ui.types.PaintType

class PaintEntity(
    var path: Path,
    var paint: Paint,
    var paintType: PaintType
)