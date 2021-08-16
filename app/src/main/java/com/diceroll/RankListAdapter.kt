package com.diceroll

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rank_list_item.view.*
import java.util.ArrayList

class RankListAdapter(private val rankList: ArrayList<RankListItem>):
    RecyclerView.Adapter<RankListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val playerName: TextView = itemView.player_name
        val rankNumber: TextView = itemView.rank_number
        val scoreNumber: TextView = itemView.score_number
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rank_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = rankList.size

    override fun onBindViewHolder(holder: RankListAdapter.ViewHolder, position: Int) {
        val currentPlayer: RankListItem = rankList[position]
        holder.playerName.text = currentPlayer.playerName
        holder.rankNumber.text = (position + 1).toString()
        holder.scoreNumber.text= currentPlayer.score.toString()

        if (currentPlayer.gameOver) {
            holder.playerName.setBackgroundColor(Color.parseColor("#caf59f"))
        }
    }
}