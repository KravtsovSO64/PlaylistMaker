package com.practicum.playlistmaker.ui.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.search.state.State
import com.practicum.playlistmaker.presentation.search.viewmodel.TrackSearchViewModel
import com.practicum.playlistmaker.ui.player.PlayerFragment
import com.practicum.playlistmaker.ui.search.uicomponents.OnTrackClickListener
import com.practicum.playlistmaker.ui.search.uicomponents.TrackAdapter
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), OnTrackClickListener {
    private val viewModel by viewModel<TrackSearchViewModel>()

    private var searchRequest: String = ""
    private var latestSearchText: String = ""
    private var searchJob: Job? = null
    private var isClickAllowed = true
    private var lastSearchResults: List<Track> = emptyList()
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var binding: FragmentSearchBinding
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackAdapter = TrackAdapter(listener = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        setupRecyclerView()
        setupEditText()
        setupButtons()

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewTrack.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }
    }

    private fun setupEditText() {
        val editText =  binding.editText

       editText.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrEmpty()) {
                        searchRequest = s.toString()
                        binding.clearIcon.show()
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isNotEmpty()) {
                        searchDebounce(searchRequest)
                    } else {
                        binding.clearIcon.gone()
                        searchJob = null
                        render(State.Default)
                    }
                }
            })

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && text.isEmpty()) { showHistoryTracks() }
            }
        }
    }

    private fun setupButtons() {
        binding.buttonUpdateSearchMusic.setOnClickListener { repeatLastRequest() }
        binding.buttonClearHistory.setOnClickListener {
            viewModel.removeListHistorySearchMusic()
            searchJob?.cancel()
        }
        binding.clearIcon.setOnClickListener {
            clearSearchRequest()
            it.gone()
        }
    }

    override fun onResume() {
        super.onResume()
        isClickAllowed = true
        render(State.Default)
    }

    private fun render(state: State) {
        binding.progressBar.visibility = if (state is State.Loading) View.VISIBLE else View.GONE

        when (state) {
            is State.Error -> showErrorState()
            is State.Empty -> showEmptyState()
            is State.Default -> showDefaultState()
            is State.History -> showHistoryState(state.tracks)
            is State.Content ->  showContentState(state.tracks)
            is State.Loading -> showProgressState()
        }
    }

    private fun showErrorState() {
        binding.apply {
            errorPoster.show()
            errorMessage.show()
            buttonUpdateSearchMusic.show()
            errorMessage.text = getString(R.string.noInternetСontent)
            errorPoster.setImageResource(R.drawable.ic_no_internet)
        }
    }

    private fun showEmptyState() {
        binding.apply {
            errorPoster.show()
            errorMessage.show()
            errorMessage.text = getString(R.string.noFoundСontent)
            errorPoster.setImageResource(R.drawable.ic_not_found)
        }
    }

    private fun showDefaultState() {
        binding.apply {
            errorPoster.gone()
            errorMessage.gone()
            buttonUpdateSearchMusic.gone()
            hintTextSearch.gone()
            buttonClearHistory.gone()
            recyclerViewTrack.gone()
        }
    }

    private fun showHistoryState(tracks: List<Track>) {
        trackAdapter.setList(tracks)
        trackAdapter.notifyDataSetChanged()
        binding.apply {
            errorPoster.gone()
            errorMessage.gone()
            buttonUpdateSearchMusic.gone()
            if (tracks.isEmpty()) {
                hintTextSearch.gone()
                buttonClearHistory.gone()
                recyclerViewTrack.gone()
            } else {
                trackAdapter.getList().size
                hintTextSearch.show()
                buttonClearHistory.show()
                recyclerViewTrack.show()
            }
        }
    }

    private fun showContentState(tracks: List<Track>) {
        trackAdapter.setList(tracks)
        trackAdapter.notifyDataSetChanged()
        lastSearchResults = trackAdapter.getList()
        latestSearchText = ""
        binding.apply {
            errorPoster.gone()
            errorMessage.gone()
            buttonUpdateSearchMusic.gone()
            hintTextSearch.gone()
            buttonClearHistory.gone()
            recyclerViewTrack.show()
        }
    }

    private fun showProgressState() {
        binding.apply {
            errorPoster.gone()
            errorMessage.gone()
            buttonUpdateSearchMusic.gone()
            hintTextSearch.gone()
            buttonClearHistory.gone()
            recyclerViewTrack.gone()
        }
    }

    private fun clearSearchRequest() {
        searchJob?.cancel()
        binding.editText.text.clear()
        inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.editText.windowToken, 0)

        render(State.Default)
    }

    private fun searchDebounce(query: String) {
        if (latestSearchText == query) return
        latestSearchText = query
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            hideKeyboard()
            viewModel.searchMusic(query)
        }
    }

    private fun repeatLastRequest() {
        render(State.Default)
        searchDebounce(searchRequest)
    }

    private fun hideKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(binding.editText.windowToken, 0)
    }

    override fun onItemClick(track: Track) {
        if (clickDebounce()) openPlayer(track)
    }

    private fun openPlayer(track: Track) {
        viewModel.setToListHistorySearchMusic(track)
        findNavController().navigate(R.id.action_searchFragment_to_playerFragment, PlayerFragment.createArgs(track))
    }

    private fun clickDebounce(): Boolean {
        val currentState = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return currentState
    }

    private fun showHistoryTracks() {
        viewModel.getListHistorySearchMusic()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
