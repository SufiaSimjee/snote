package com.example.snote.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.example.snote.viewModel.NoteListViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snote.NoteListAdapter
import com.example.snote.R
import com.example.snote.databinding.NoteListFragmentBinding
import com.example.snote.model.Note
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class NoteListFragment: Fragment() {

    private val noteListViewModel: NoteListViewModel by viewModels()
    private var _binding: NoteListFragmentBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NoteListFragmentBinding
            .inflate(inflater, container, false)

        binding.noteRecycleView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.fragment_note_list, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.new_note -> {
                    showNewNote()
                    true
                }

                else -> false
            }
        }}, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                noteListViewModel.notes.collect { notes ->
                    binding.noteRecycleView.adapter =
                        NoteListAdapter(notes) { noteId ->
                            findNavController().navigate(
                                NoteListFragmentDirections.showNoteDetail(noteId)
                            )
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNewNote() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newNote = Note(
                id = UUID.randomUUID(),
                title = "",
                content = " ",
                date = Date(),
            )
            noteListViewModel.addNote(newNote)
            findNavController().navigate(
                NoteListFragmentDirections.showNoteDetail(newNote.id)
            )
        }
    }
}

