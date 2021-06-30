package com.sribnyak.connectfour

object Game {
    const val ROWS = 6
    const val COLS = 7
    private const val N_TO_WIN = 4
    private const val MID_COL = 3

    enum class State { WELCOME, TURN, END }

    var state = State.WELCOME
    var currentTurn = 1
    var selectedColumn = MID_COL
    var longestLine = ArrayList<Pair<Int, Int>>()
    val field = Array(ROWS) { Array(COLS) { 0 } }

    val winner
        get() = if (longestLine.size < N_TO_WIN) 0 else {
            val (x, y) = longestLine[0]
            field[x][y]
        }

    fun dropDisc() {
        val depth = getLastEmptyBlock(selectedColumn)
        if (depth >= 0) {
            field[depth][selectedColumn] = currentTurn
            updateLongestLine(selectedColumn, depth)
            if (longestLine.size < N_TO_WIN && field[0].contains(0)) {
                currentTurn = -currentTurn
                selectedColumn = MID_COL
            } else
                state = State.END
        }
    }

    fun start() {
        if (state == State.END) {
            currentTurn = 1
            selectedColumn = MID_COL
            longestLine = ArrayList()
            field.forEach { it.fill(0) }
        }
        state = State.TURN
    }

    private fun getLastEmptyBlock(col: Int): Int {
        var cnt = 0
        while (cnt < ROWS && field[cnt][col] == 0)
            cnt++
        return cnt-1
    }

    private fun updateLongestLine(x0: Int, y0: Int) {
        for ((dx, dy) in arrayOf(Pair(1, -1), Pair(1, 0), Pair(1, 1), Pair(0, 1)))
            longestLine = maxOf(longestLine, growLine(x0, y0, dx, dy), compareBy { it.size })
    }

    private fun growLine(x0: Int, y0: Int, dx: Int, dy: Int): ArrayList<Pair<Int, Int>> {
        val line = ArrayList<Pair<Int, Int>>()
        line.add(Pair(y0, x0))
        var x = x0 - dx
        var y = y0 - dy
        while (x in 0 until COLS && y in 0 until ROWS && field[y][x] == field[y0][x0]) {
            line.add(Pair(y, x))
            x -= dx
            y -= dy
        }
        x = x0 + dx
        y = y0 + dy
        while (x in 0 until COLS && y in 0 until ROWS && field[y][x] == field[y0][x0]) {
            line.add(Pair(y, x))
            x += dx
            y += dy
        }
        return line
    }
}
