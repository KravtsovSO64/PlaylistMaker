package com.practicum.playlistmaker.presentation.media.ui.playlist

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
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatingPlaylistBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.presentation.media.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment: Fragment() {

    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        private const val CLICK_DEBOUNCE_DELAY = 300L

        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(ARGS_PLAYLIST to playlist)
    }

    private var _binding: FragmentCreatingPlaylistBinding? = null
    private lateinit var playlist: Playlist
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistViewModel>()
    private var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private var namePlaylist: String = ""
    private var descriptionPlaylist: String = ""
    private var coverImagePath: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlist = arguments?.getSerializable(PlaylistFragment.ARGS_PLAYLIST) as? Playlist
            ?: throw IllegalArgumentException("Track cannot be null")
        namePlaylist = playlist.name
        descriptionPlaylist = playlist.description
        coverImagePath = playlist.coverImagePath

    }

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

        render()
        editName()
        editDescription()
        createPickMedia()
        clickHandler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation(false)
    }

    override fun onPause() {
        super.onPause()
        showBottomNavigation(true)
    }

    private fun render() {

        binding.editName.setText(namePlaylist)
        binding.editDescription.setText(descriptionPlaylist)
        binding.buttonCreate.text = "Сохранить"
        binding.arrowBackPlayer.setTitle("Редактировать")
        //Так как плейлист не может существовать без названия
        binding.buttonCreate.isEnabled = true

        Glide.with(requireContext())
            .load(coverImagePath)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.addPhoto)

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

            val editText = this
            val cursorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.text_cursor)
            editText.textCursorDrawable = cursorDrawable

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

    private fun switchActiveButtonCreate(text: String) =  text.isNotBlank().also {
        binding.buttonCreate.isEnabled = it
    }

    private fun createPickMedia() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            coverImagePath = uri.toString()
            if (uri != null) {
                binding.addPhoto.setImageURI(uri)
                viewModel.saveImageToPrivateStorage(uri)
            }
        }
    }

    private fun clickHandler() {

        binding.apply {
            addPhoto.setOnClickListener {
                pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            buttonCreate.setOnClickListener {
                val newPlaylist = playlist.copy(
                    name = namePlaylist,
                    description  = descriptionPlaylist,
                    coverImagePath = coverImagePath
                )
                viewModel.update(newPlaylist)
                findNavController().popBackStack()
            }

            arrowBackPlayer.setNavigationOnClickListener {
               findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
    }

    private fun showBottomNavigation(flag: Boolean) {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val divider = requireActivity().findViewById<View>(R.id.divider)

        if (flag) {
            bottomNavigationView.show()
            divider.show()
        } else {
            bottomNavigationView.gone()
            divider.gone()
        }
    }
}

