package com.example.snote.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.snote.NoteListViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snote.NoteListAdapter
import com.example.snote.databinding.NoteListFragmentBinding
import kotlinx.coroutines.launch

class NoteListFragment: Fragment() {

    private val noteListViewModel: NoteListViewModel by viewModels()
    private var _binding: NoteListFragmentBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access binding because it is null"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NoteListFragmentBinding
            .inflate(inflater ,container , false)

        binding.noteRecycleView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
}