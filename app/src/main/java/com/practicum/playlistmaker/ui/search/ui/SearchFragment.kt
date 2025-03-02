package com.practicum.playlistmaker.ui.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.search.state.TrackState
import com.practicum.playlistmaker.presentation.search.viewmodel.TrackSearchViewModel
import com.practicum.playlistmaker.ui.player.PlayerActivity
import com.practicum.playlistmaker.ui.search.uicomponents.HistoryTrackAdapter
import com.practicum.playlistmaker.ui.search.uicomponents.OnTrackClickListener
import com.practicum.playlistmaker.ui.search.uicomponents.TrackAdapter
import com.practicum.playlistmaker.utils.Constants
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), OnTrackClickListener {

    private var inputMethodManager: InputMethodManager? = null
    private var searchRequest: String = ""
    private var latestSearchText: String = ""
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<TrackSearchViewModel>()

    private val adapterTrackSearch = TrackAdapter(listener = this)
    private val adapterTrackHistory = HistoryTrackAdapter(listener = this)

    private var isClickAllowed = true
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        viewModel.getListHistorySearchMusic()

        binding.trackList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
            if (state is TrackState.History) adapterTrackHistory.updateSearchList(state.tracks)
        }

        binding.clearIcon.setOnClickListener {
            clearSearchRequest()
            it.gone()
        }

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    adapterTrackSearch.searchListAdapter.clear()
                    showErrorMessage(0)
                    viewModel.getListHistorySearchMusic()
                    showHistorySearchTrack(binding.editText.hasFocus())
                } else {
                    showErrorMessage(0)
                    showHistorySearchTrack(false)
                    adapterTrackSearch.searchListAdapter.clear()
                    searchRequest = s.toString()
                }
                binding.clearIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                searchDebounce(searchRequest)
            }
        })

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            showHistorySearchTrack(hasFocus)
        }

        binding.buttonUpdateSearchMusic.setOnClickListener {
            showErrorMessage(0)
            latestSearchText = ""
            searchDebounce(searchRequest)
        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.removeListHistorySearchMusic()
            showHistorySearchTrack(false)
            adapterTrackHistory.historyListAdapter.clear()
            searchJob?.cancel()
        }
    }

    private fun clearSearchRequest() {
        binding.editText.setText("")
        inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.editText.windowToken, 0)
        showErrorMessage(0)
        showHistorySearchTrack(binding.editText.hasFocus())
    }

    private fun render(state: TrackState) {
        when (state) {
            is TrackState.Loading -> {
                binding.trackList.gone()
                showProgressLoading(true)
            }
            is TrackState.Error -> {
                binding.trackList.gone()
                showProgressLoading(false)
                showErrorMessage(2)
            }
            is TrackState.Content -> {
                binding.trackList.show()
                showProgressLoading(false)
                showUpdatedListTrack(state.tracks)
            }
            is TrackState.History -> {
                adapterTrackHistory.updateSearchList(state.tracks)
            }
            is TrackState.Empty -> {
                binding.trackList.gone()
                showProgressLoading(false)
                showErrorMessage(1)
            }
        }
    }

    private fun showProgressLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showUpdatedListTrack(list: List<Track>) {
        if (list.isNotEmpty()) {
            adapterTrackSearch.updateSearchList(list)
        } else {
            showErrorMessage(1)
        }
        adapterTrackSearch.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        val track = if (binding.trackList.adapter == adapterTrackSearch) {
            adapterTrackSearch.searchListAdapter[position]
        } else {
            adapterTrackHistory.historyListAdapter[position]
        }
       if (clickDebounce()) {
           transferTrackToPlayer(track)
       }
    }

    private fun showErrorMessage(status: Int) {
        when (status) {
            0 -> {
                binding.buttonUpdateSearchMusic.gone()
                binding.errorPoster.gone()
                binding.errorMessage.gone()
                binding.progressBar.gone()
                binding.trackList.show()
            }
            1 -> {
                binding.trackList.gone()
                binding.buttonUpdateSearchMusic.gone()
                binding.progressBar.gone()
                binding.errorPoster.show()
                binding.errorMessage.show()
                binding.errorMessage.text = resources.getText(R.string.noFoundСontent)
                binding.errorPoster.setImageResource(R.drawable.ic_not_found)
            }
            2 -> {
                binding.trackList.gone()
                binding.progressBar.gone()
                binding.errorPoster.show()
                binding.errorMessage.show()
                binding.buttonUpdateSearchMusic.show()
                binding.errorMessage.text = resources.getText(R.string.noInternetСontent)
                binding.errorPoster.setImageResource(R.drawable.ic_no_internet_ligth)
            }
            else -> {
                binding.buttonUpdateSearchMusic.gone()
                binding.errorPoster.gone()
                binding.errorMessage.gone()
                binding.trackList.show()
            }
        }
    }

    private fun showHistorySearchTrack(hasFocus: Boolean) {
        if (hasFocus && binding.editText.text.isEmpty() && adapterTrackHistory.historyListAdapter.isNotEmpty()) {
            binding.hintTextSearch.show()
            binding.buttonClearHistory.show()
            binding.trackList.adapter = adapterTrackHistory
            adapterTrackHistory.notifyDataSetChanged()
        } else {
            binding.hintTextSearch.gone()
            binding.buttonClearHistory.gone()
            binding.trackList.adapter = adapterTrackSearch
            adapterTrackSearch.notifyDataSetChanged()
        }
    }

    private fun transferTrackToPlayer(track: Track) {
        val playerIntent = Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(Constants.TRACK, track)
        }
        startActivity(playerIntent)
        addTrackToHistory(track)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun searchDebounce(changedText: String){
        searchJob?.cancel()
        if (latestSearchText == changedText) return

        latestSearchText = changedText

        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            viewModel.searchMusic(changedText)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.SEARCH_REQUEST, searchRequest)
    }

    private fun addTrackToHistory(track: Track){
        viewModel.setToListHistorySearchMusic(track)
        adapterTrackHistory.notifyDataSetChanged()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}