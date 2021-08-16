package com.diceroll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var start_button: Button
    private lateinit var roll_dice_button: Button
    private lateinit var rank_list_button: Button
    private lateinit var quit_button: Button

    private lateinit var initial_screen: ConstraintLayout
    private lateinit var playing_arena: ConstraintLayout
    private lateinit var dice_arena: ConstraintLayout

    private lateinit var player_turn_notification: TextView
    private lateinit var player_turn: TextView
    private lateinit var dice_roll_value: TextView

    private lateinit var player_count: EditText
    private lateinit var winning_target: EditText

    private var players: Int = 0
    private var target: Int = 0
    private var currentTurn: Int = -1
    private lateinit var rankList: ArrayList<RankListItem>
    private lateinit var playingList: ArrayList<RankListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
    }

    private fun initializeViews() {
        start_button = findViewById(R.id.start_button)
        roll_dice_button = findViewById(R.id.roll_dice_button)
        rank_list_button = findViewById(R.id.rank_list_button)
        quit_button = findViewById(R.id.quit_button)
        initial_screen = findViewById(R.id.initial_screen)
        playing_arena = findViewById(R.id.playing_arena)
        dice_arena = findViewById(R.id.dice_arena)
        player_turn_notification = findViewById(R.id.player_turn_notification)
        player_turn = findViewById(R.id.player_turn)
        dice_roll_value = findViewById(R.id.dice_roll_value)
        player_count = findViewById(R.id.player_count)
        winning_target = findViewById(R.id.winning_target)

        start_button.setOnClickListener(this)
        rank_list_button.setOnClickListener(this)
        quit_button.setOnClickListener(this)

        restartGame()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.start_button -> initializeToArena()
            R.id.roll_dice_button -> rollDice()
            R.id.rank_list_button -> openRankList()
            R.id.quit_button -> restartGame()
        }
    }

    private fun initializeToArena() {
        val playersString = player_count.text.toString()
        val targetString = winning_target.text.toString()
        if (playersString.isNullOrEmpty() || targetString.isNullOrEmpty() || playersString.toInt() < 1 || targetString.toInt() < 1) {
            Toast.makeText(applicationContext, "Invalid parameter values", Toast.LENGTH_LONG).show()
            return
        }

        players = playersString.toInt()
        target = targetString.toInt()

        populatePlayingList()

        initial_screen.visibility = View.GONE
        playing_arena.visibility = View.VISIBLE
        dice_arena.visibility = View.VISIBLE

        beforeNextRoll(0)
    }

    private fun populatePlayingList() {
        for (i in 1..players) {
            val player = RankListItem()
            player.playerName = "PLAYER-${i}"
            player.prevRoll = 0
            player.rank = 0
            player.score = 0
            player.skipNext = false
            player.gameOver = false
            playingList.add(player)
        }

        playingList.shuffle()

        for (i in 0 until players) {
            playingList[i].turn = i
        }
    }

    private fun restartGame() {
        player_count.setText("")
        winning_target.setText("")

        initial_screen.visibility = View.VISIBLE
        playing_arena.visibility = View.GONE
        dice_arena.visibility = View.GONE
        player_turn_notification.text = "IT'S YOUR TURN"
        quit_button.text = "QUIT"

        players = 0
        target = 0
        currentTurn = 0
        rankList = ArrayList<RankListItem>()
        playingList = ArrayList<RankListItem>()

        roll_dice_button.setOnClickListener(this)
    }

    private fun rollDice() {
        val diceValue: Int = Random.nextInt(6) + 1
        dice_roll_value.text = diceValue.toString()

        playingList[currentTurn].score += diceValue

        if (playingList[currentTurn].score >= target) {
            playingList[currentTurn].gameOver = true
            appendToRankList(playingList[currentTurn])
            playingList.removeAt(currentTurn)
            return beforeNextRoll(currentTurn)
        }

        if (diceValue == 1 && playingList[currentTurn].prevRoll == 1) {
            playingList[currentTurn].skipNext = true
        }

        playingList[currentTurn].prevRoll = diceValue

        if (diceValue == 6) {
            return beforeNextRoll(currentTurn)
        }

        beforeNextRoll(currentTurn+1)
    }

    private fun beforeNextRoll(playingListIndex: Int) {
        if(playingList.size == 0) {
            return endGame()
        }
        if (playingListIndex >= playingList.size) {
            return beforeNextRoll(0)
        }
        if (playingList[playingListIndex].skipNext) {
            playingList[playingListIndex].skipNext = false
            return beforeNextRoll(playingListIndex+1)
        }
        player_turn.text = playingList[playingListIndex].playerName
        currentTurn = playingListIndex
    }

    private fun endGame() {
        quit_button.text = "NEW GAME"
        player_turn_notification.text = "GAME"
        player_turn.text = "OVER"
        roll_dice_button.setOnClickListener(null)
    }

    private fun openRankList() {
        val comparator = kotlin.Comparator { o1: RankListItem, o2: RankListItem ->
            if (o1.score == o2.score) {
                return@Comparator o1.turn - o2.turn
            }
            return@Comparator o2.score - o1.score
        }

        val rankSortedList = ArrayList<RankListItem>()
        rankSortedList.addAll(rankList)
        val tempPlayingList = playingList.sortedWith(comparator)
        rankSortedList.addAll(tempPlayingList)
        RankList(rankSortedList).show(supportFragmentManager, "Rank List")
    }

    private fun appendToRankList(player: RankListItem) {
        val newRank = rankList.size + 1
        player.rank = newRank
        rankList.add(player)
    }
}