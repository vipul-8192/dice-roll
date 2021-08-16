package com.diceroll

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.rank_list.*
import java.util.*

class RankList(private val rankList: ArrayList<RankListItem>): DialogFragment(), View.OnClickListener {
    private lateinit var dismissButton: Button

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rank_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rank_list.adapter = RankListAdapter(rankList)
        rank_list.layoutManager = LinearLayoutManager(this.context)
        rank_list.setHasFixedSize(true)
        dismissButton = dialog_dismiss
        dismissButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        dismiss()
    }
}