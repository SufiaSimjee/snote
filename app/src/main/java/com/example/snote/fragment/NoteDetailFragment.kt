package com.example.snote.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.snote.databinding.NoteDetailFragmentBinding
import com.example.snote.model.Note
import com.example.snote.viewModel.NoteDetailViewModel
import com.example.snote.viewModel.NoteDetailViewModelFactory
import kotlinx.coroutines.launch
import java.util.Date

class NoteDetailFragment: Fragment() {


    private val args: NoteDetailFragmentArgs by navArgs()

    private val noteDetailViewModel: NoteDetailViewModel by viewModels {
        NoteDetailViewModelFactory(args.noteId)
    }

    private var _binding : NoteDetailFragmentBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access binding because it is null"
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
                noteDetailViewModel.updateNote { oldNote ->
                    oldNote.copy(title = text.toString())
                }
            }

            descriptionInput.doOnTextChanged { text, _, _, _ ->
                noteDetailViewModel.updateNote { oldNote ->
                    oldNote.copy(content = text.toString())
                }
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                noteDetailViewModel.note.collect { note ->
                    note?.let { updateUi(it) }
                }
            }
        }

        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            val newDate =
                bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            noteDetailViewModel.updateNote { it.copy(date = newDate) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateUi(note: Note) {
        binding.apply {
            if (etTitle.text.toString() != note.title) {
                etTitle.setText(note.title)
            }

            if (descriptionInput.text.toString() != note.content) {
                descriptionInput.setText(note.content)
            }
            btnNoteDate.text = note.date.toString()

            btnNoteDate.setOnClickListener {
                findNavController().navigate(NoteDetailFragmentDirections.showDatePicker(note.date))
            }
        }
    }
}


