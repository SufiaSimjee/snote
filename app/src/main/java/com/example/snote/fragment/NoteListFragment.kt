package com.example.snote.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.snote.NoteListViewModel
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snote.NoteListAdapter
import com.example.snote.databinding.NoteListFragmentBinding

private const val TAG = "NoteListFragment"

class NoteListFragment: Fragment() {

    private val noteListViewModel: NoteListViewModel by viewModels()
    private var _binding: NoteListFragmentBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access binding because it is null"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total notes: ${noteListViewModel.notes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NoteListFragmentBinding
            .inflate(inflater ,container , false)

        binding.noteRecycleView.layoutManager = LinearLayoutManager(context)
        val notes = noteListViewModel.notes
        val adapter = NoteListAdapter(notes)

        binding.noteRecycleView.adapter = adapter

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}