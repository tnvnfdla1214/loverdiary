package com.example.loverdiary.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loverdiary.R
import com.example.loverdiary.data.Notes
import kotlinx.android.synthetic.main.items_notes.view.*
import java.util.ArrayList

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private var notes = ArrayList<Notes>()

    fun setData(data: List<Notes>?) {
        if (data == null) return
        this.notes.clear()
        this.notes.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_notes, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val movies = notes[position]
        holder.bind(movies)
    }

    override fun getItemCount(): Int = notes.size

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Notes) {
            with(itemView) {
                tvTitle.text = data.notesTitle
                tvMessage.text = data.notesDate.toString()
            }
        }
    }
}