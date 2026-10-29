package com.example.snote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.snote.databinding.ListItemBinding
import com.example.snote.model.Note

class NoteHolder(val binding: ListItemBinding)
    : RecyclerView.ViewHolder(binding.root){
}

class NoteListAdapter(private val notes: List<Note>)
    : RecyclerView.Adapter<NoteHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater , parent , false)
        return NoteHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
       val note = notes[position]
        holder.apply {
            binding.tvTitle.text = note.title
            binding.tvDate.text = note.date.toString()
        }
    }

    override fun getItemCount() = notes.size

}