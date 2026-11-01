package com.example.snote.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.snote.databinding.NoteDetailFragmentBinding
import com.example.snote.model.Note
import java.util.Date
import java.util.UUID

class NoteDetailFragment: Fragment() {

    private lateinit var note: Note
    private val args: NoteDetailFragmentArgs by navArgs()

    private var _binding : NoteDetailFragmentBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access binding because it is null"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        note = Note(
                    id = UUID.randomUUID(),
                    title = "" ,
                    content = "" ,
                    date = Date()
                    )

        Log.d("NoteID", "The note ID is: ${args.noteId}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NoteDetailFragmentBinding
                    .inflate(inflater ,
                        container ,
                        false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etTitle.doOnTextChanged { text, _, _, _ ->
                note = note.copy(title = text.toString())
            }

            descriptionInput.doOnTextChanged { text, _, _, _ ->
                note = note.copy(content = text.toString())
            }

            tvNoteDate.text = note.date.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


