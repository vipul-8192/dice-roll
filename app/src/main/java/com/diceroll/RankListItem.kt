package com.diceroll

data class RankListItem(
    var playerName: String = "",
    var prevRoll: Int = 0,
    var rank: Int = 0,
    var score: Int = 0,
    var turn: Int = 0,
    var skipNext: Boolean = false,
    var gameOver: Boolean = false
)