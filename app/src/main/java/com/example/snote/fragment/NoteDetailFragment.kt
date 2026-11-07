package com.example.snote.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.snote.R
import com.example.snote.databinding.NoteDetailFragmentBinding
import com.example.snote.getScaledBitmap
import com.example.snote.model.Note
import com.example.snote.viewModel.NoteDetailViewModel
import com.example.snote.viewModel.NoteDetailViewModelFactory
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

private const val DATE_FORMAT = "EEE, MMM, dd"
class NoteDetailFragment: Fragment() {

    private val args: NoteDetailFragmentArgs by navArgs()

    private val selectSendTo = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        uri?.let { parseContactSelection(it) }
    }

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePhoto: Boolean ->
        if (didTakePhoto && photoName != null) {
            noteDetailViewModel.updateNote { oldNote ->
                oldNote.copy(photoFileName = photoName)
            }
        }
    }

    private var photoName: String? = null

    private val noteDetailViewModel: NoteDetailViewModel by viewModels {
        NoteDetailViewModelFactory(args.noteId)
    }

    private var _binding: NoteDetailFragmentBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NoteDetailFragmentBinding
            .inflate(
                inflater,
                container,
                false
            )
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

            btnNoteDelete.setOnClickListener {
                noteDetailViewModel.note.value?.let { note ->
                    noteDetailViewModel.deleteNote(note)
                    findNavController().navigateUp()
                }
            }

            noteSend.setOnClickListener {
                selectSendTo.launch(null)
            }

            val selectSendToIntent = selectSendTo.contract.createIntent(
                requireContext(),
                null
            )
            noteSend.isEnabled = canResolveIntent(selectSendToIntent)

            noteCamera.setOnClickListener {
                photoName = "IMG_${Date()}.JPG"
                val photoFile = File(
                    requireContext().applicationContext.filesDir,
                    photoName
                )
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.snote.fileprovider",
                    photoFile
                )
                takePhoto.launch(photoUri)
            }

            val captureImageIntent = takePhoto.contract.createIntent(
                requireContext(),
                Uri.parse("")
            )
            noteCamera.isEnabled = canResolveIntent(captureImageIntent)
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

            noteReport.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getNoteReport(note))
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        getString(R.string.note_report_subject)
                    )
                }
                val chooserIntent = Intent.createChooser(
                    reportIntent,
                    getString(R.string.send_report)
                )
                startActivity(chooserIntent)
            }

            noteSend.text = note.sendTo.ifEmpty {
                getString(R.string.note_send)
            }

            updatePhoto(note.photoFileName)
        }
    }

    private fun getNoteReport(note: Note): String {

        val dateString = DateFormat.format(DATE_FORMAT, note.date)
            .toString()

        val noteText = if (note.sendTo.isBlank()) {
            getString(R.string.note_report_no_sendTo)
        } else {
            getString(R.string.note_report_sendTo, note.sendTo)
        }

        return getString(
            R.string.note_report,
            note.title, dateString, note.content, noteText
        )
    }

    private fun parseContactSelection(contactUri: Uri) {
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        val queryCursor = requireActivity().contentResolver
            .query(contactUri, queryFields, null, null, null)

        queryCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val sendTo = cursor.getString(0)
                noteDetailViewModel.updateNote { oldNote ->
                    oldNote.copy(sendTo = sendTo)
                }
            }
        }
    }

    private fun canResolveIntent(intent: Intent): Boolean {
//        intent.addCategory(Intent.CATEGORY_HOME)
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        return resolvedActivity != null
    }
    private fun updatePhoto(photoFileName: String?) {
        if (binding.notePhoto.tag != photoFileName) {
            val photoFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }
            if (photoFile?.exists() == true) {
                binding.notePhoto.doOnLayout { measuredView ->
                    val scaledBitmap = getScaledBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                    binding.notePhoto.setImageBitmap(scaledBitmap)
                    binding.notePhoto.tag = photoFileName
                }
            } else {
                binding.notePhoto.setImageBitmap(null)
                binding.notePhoto.tag = null
            }
        }
    }
}


