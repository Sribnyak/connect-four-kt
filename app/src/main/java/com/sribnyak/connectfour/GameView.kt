package com.sribnyak.connectfour

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class GameView(ctx: Context) : View(ctx) {
    private companion object {
        const val BLOCK_WIDTH = 10 // in 'units'
        const val BLOCK_HEIGHT = 8
        const val RADIUS = 3
        const val WIDTH = Game.COLS * BLOCK_WIDTH + 2 // empty - cols - empty
        const val HEIGHT = (Game.ROWS + 1) * BLOCK_HEIGHT + 3 // empty row - fill - rows - fill - empty
        const val WELCOME_TEXT_SIZE = 5
        const val END_TEXT_SIZE = BLOCK_HEIGHT - 2
        const val BLUE = 0xff222288.toInt()
        const val WHITE = 0xffffffff.toInt()
        const val RED = 0xffdd0000.toInt()
        const val YELLOW = 0xffffdd00.toInt()
        fun getPlayerColor(id: Int) = when (id) {
            1 -> RED
            -1 -> YELLOW
            else -> WHITE
        }
    }
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
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

        paint.color = WHITE
        canvas.drawPaint(paint)

        if (Game.state != Game.State.WELCOME) {
            paint.color = BLUE
            canvas.drawRect(getField(), paint)

            if (Game.state == Game.State.TURN) {
                paint.color = getPlayerColor(Game.currentTurn)
                canvas.drawCircle(getBlockX(Game.selectedColumn), getY(BLOCK_HEIGHT / 2),
                    RADIUS * unit, paint)
            } else {
                paint.color = BLUE
                paint.textSize = END_TEXT_SIZE * unit
                val text = when (Game.winner) {
                    1 -> "Red wins! Restart?"
                    -1 -> "Yellow wins! Restart?"
                    else -> "Draw. Restart?"
                }
                canvas.drawText(text, getX(WIDTH / 2), getY(BLOCK_HEIGHT - 2), paint)

                if (Game.winner != 0) {
                    for ((i, j) in Game.longestLine) {
                        paint.color = getPlayerColor(-Game.field[i][j])
                        canvas.drawCircle(getBlockX(j), getBlockY(i), (RADIUS + .5f) * unit, paint)
                    }
                }
            }

            for (i in 0 until Game.ROWS) {
                for (j in 0 until Game.COLS) {
                    paint.color = getPlayerColor(Game.field[i][j])
                    canvas.drawCircle(getBlockX(j), getBlockY(i), RADIUS * unit, paint)
                }
            }
        } else {
            val text = arrayOf("Welcome to Connect Four!",
                "",
                "In this text I could tell you",
                "the rules of the game and",
                "the controls, but I won't :)",
                "",
                "Now guess how to start")
            paint.color = BLUE
            paint.textSize = WELCOME_TEXT_SIZE * unit
            for (i in 0..6)
                canvas.drawText(text[i], getX(WIDTH/2),
                    getY(HEIGHT/2 + (i - 3) * WELCOME_TEXT_SIZE + 2), paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && event.action == MotionEvent.ACTION_DOWN) {
            val x = (event.x - x0) / unit
            val y = (event.y - y0) / unit
            if (Game.state == Game.State.TURN) {
                if (x > 1 && x < WIDTH - 1 && y > Game.ROWS * BLOCK_HEIGHT && y < HEIGHT - 1) {
                    Game.dropDisc()
                    invalidate()
                } else if (y > 0 && y < Game.ROWS * BLOCK_HEIGHT - 2)
                    for (i in 0 until Game.COLS)
                        if (x > i * BLOCK_WIDTH + 2 && x < (i + 1) * BLOCK_WIDTH) {
                            Game.selectedColumn = i
                            invalidate()
                            break
                        }
            } else if (x > 1 && x < WIDTH - 1 && y > 1 && y < HEIGHT - 1) {
                Game.start()
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        unit = min(width / WIDTH, height / HEIGHT).toFloat()
        x0 = (width - WIDTH * unit) / 2
        y0 = (height - HEIGHT * unit) / 2
    }
}
