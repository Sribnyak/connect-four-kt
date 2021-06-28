package com.sribnyak.connectfour

object Game {
    const val ROWS = 6
    const val COLS = 7
    const val N_TO_WIN = 4
    const val MID_COL = 3

    enum class State { WELCOME, TURN, END }

    var state = State.TURN
    var currentTurn = 1
    var selectedColumn = MID_COL
    var field = Array(ROWS) { Array(COLS) { 0 } }

    fun dropDisc() {
        val depth = getLastEmptyBlock(selectedColumn)
        if (depth >= 0) {
            field[depth][selectedColumn] = currentTurn
            // TODO val line = getLineWith(selectedColumn, depth)
            currentTurn = -currentTurn
            selectedColumn = MID_COL
        }
    }

    private fun getLastEmptyBlock(col: Int): Int {
        var cnt = 0
        while (cnt < ROWS && field[cnt][col] == 0)
            cnt++
        return cnt-1
    }
}
