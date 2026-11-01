package com.example.snote

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.snote.databinding.ListItemBinding
import com.example.snote.model.Note
import java.util.UUID

class NoteHolder(val binding: ListItemBinding)
    : RecyclerView.ViewHolder(binding.root){
        fun bind(note: Note , onNoteClicked: (noteId: UUID) -> Unit ) {
            binding.tvTitle.text = note.title
            binding.tvDate.text = note.date.toString()

            binding.root.setOnClickListener {
                onNoteClicked(note.id)
            }
        }
    }

class NoteListAdapter(private val notes: List<Note> ,
                      private val onNoteClicked: (noteId: UUID) -> Unit)
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
        holder.bind(note , onNoteClicked)
    }

    override fun getItemCount() = notes.size

}