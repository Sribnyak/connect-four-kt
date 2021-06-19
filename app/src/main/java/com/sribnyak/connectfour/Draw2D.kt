package com.sribnyak.connectfour

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import kotlin.math.min

class Draw2D(ctx: Context) : View(ctx) {
    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        canvas.drawPaint(paint)

        paint.isAntiAlias = true
        paint.color = Color.RED
        canvas.drawCircle(width / 2f, height / 2f, min(width, height) / 4f, paint)
    }
}
