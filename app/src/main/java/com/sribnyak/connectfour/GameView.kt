package com.sribnyak.connectfour

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import kotlin.math.min

class GameView(ctx: Context) : View(ctx) {
    private companion object {
        const val BLOCK_WIDTH = 10 // in 'units'
        const val BLOCK_HEIGHT = 8
        const val RADIUS = 3
        const val WIDTH = Game.COLS * BLOCK_WIDTH + 2 // empty - cols - empty
        const val HEIGHT = (Game.ROWS + 1) * BLOCK_HEIGHT + 3 // empty row - fill - rows - fill - empty
        val BLUE = Color.rgb(34, 34, 136)
        val RED = Color.rgb(221, 0, 0)
        val YELLOW = Color.rgb(255, 221, 0)
        val WHITE = Color.rgb(255, 255, 255)
        private fun getPlayerColor(id: Int) = when (id) {
            1 -> RED
            -1 -> YELLOW
            else -> WHITE
        }
    }
    private val paint: Paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private var unit = min(width / WIDTH, height / HEIGHT).toFloat()
    private var x0 = (width - WIDTH * unit) / 2
    private var y0 = (height - HEIGHT * unit) / 2

    private fun getX(u: Int) = x0 + u * unit
    private fun getY(u: Int) = y0 + u * unit

    private fun getBlockX(j: Int) = getX(1 + j * BLOCK_WIDTH + BLOCK_WIDTH / 2)
    private fun getBlockY(i: Int) = getY(1 + (i + 1) * BLOCK_HEIGHT + BLOCK_HEIGHT / 2)

    private fun getField() = RectF(getX(1), getY(BLOCK_HEIGHT),
                                   getX(WIDTH - 1), getY(HEIGHT - 1))

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        Game.field[5][3] = 1
        Game.field[5][4] = -1

        if (Game.state == Game.State.TURN) {
            paint.color = WHITE
            canvas.drawPaint(paint)

            paint.color = BLUE
            canvas.drawRect(getField(), paint)

            paint.color = getPlayerColor(Game.currentTurn)
            canvas.drawCircle(getBlockX(Game.selectedColumn), getY(BLOCK_HEIGHT / 2), RADIUS * unit, paint)

            for (i in 0 until Game.ROWS) {
                for (j in 0 until Game.COLS) {
                    paint.color = getPlayerColor(Game.field[i][j])
                    canvas.drawCircle(getBlockX(j), getBlockY(i), RADIUS * unit, paint)
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        unit = min(width / WIDTH, height / HEIGHT).toFloat()
        x0 = (width - WIDTH * unit) / 2
        y0 = (height - HEIGHT * unit) / 2
    }
}
