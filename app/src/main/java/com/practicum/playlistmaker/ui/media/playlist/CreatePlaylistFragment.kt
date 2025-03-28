package com.practicum.playlistmaker.ui.media.playlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.databinding.FragmentCreatingPlaylistBinding
import com.practicum.playlistmaker.presentation.media.viewmodel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment: Fragment() {

    private var _binding: FragmentCreatingPlaylistBinding? = null
    private val viewModel by viewModel<PlaylistViewModel>()

    private val binding get() = _binding!!
    private var namePlaylist: String = ""
    private var descriptionPlaylist: String = ""
    private var coverImagePath: String = ""
    private var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private var confirmDialog: MaterialAlertDialogBuilder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatingPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editName()
        editDescription()
        createPickMedia()
        createDialogConfirmation()
        clickHandler()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        namePlaylist = ""
        descriptionPlaylist = ""
        coverImagePath = ""
    }

    private fun editName() {
        binding.editName.apply {
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    namePlaylist = p0.toString()
                    switchActiveButtonCreate(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
    }

    private fun editDescription() {
        binding.editDescription.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    descriptionPlaylist = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
    }

    private fun createPickMedia() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {  uri ->
            coverImagePath = uri.toString()
            if (uri != null) {
                binding.addPhoto.setImageURI(uri)
                viewModel.saveImageToPrivateStorage(uri)
            }
        }
    }

    private fun switchActiveButtonCreate(text: String) =  text.isNotEmpty().also {
        binding.buttonCreate.isEnabled = it
    }

    private fun createDialogConfirmation() {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setNegativeButton("Отмена") { dialog, which ->

            }.setPositiveButton("Завершить") { dialog, which ->
                findNavController().popBackStack()
            }
    }

    private fun clickHandler() {

        binding.apply {
            addPhoto.setOnClickListener {
                pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            buttonCreate.setOnClickListener {
                viewModel.createPlaylist(namePlaylist, descriptionPlaylist, coverImagePath)
                Snackbar.make(binding.root, "\"Плейлист $namePlaylist создан\"", Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }

            arrowBackPlayer.setNavigationOnClickListener {
                navigateBack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        })
    }

    private fun navigateBack() {
        if (namePlaylist.isNotEmpty() || binding.addPhoto.drawable  != null)  confirmDialog?.show() else  findNavController().popBackStack()
    }

}