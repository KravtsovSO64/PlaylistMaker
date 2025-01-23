package com.practicum.playlistmaker.search.ui.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Constants
import com.practicum.playlistmaker.creator.gone
import com.practicum.playlistmaker.creator.show
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.uiComponents.HistoryTrackAdapter
import com.practicum.playlistmaker.search.ui.uiComponents.OnTrackClickListener
import com.practicum.playlistmaker.search.ui.uiComponents.TrackAdapter
import com.practicum.playlistmaker.search.viewmodel.state.TrackSearchViewState
import com.practicum.playlistmaker.search.viewmodel.viewmodel.TrackSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), OnTrackClickListener {

    private var inputMethodManager: InputMethodManager? = null
    private var searchRequest: String = ""
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<TrackSearchViewModel>()

    private val adapterTrackSearch = TrackAdapter(listener = this)
    private val adapterTrackHistory = HistoryTrackAdapter(listener = this)

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            if (state is TrackSearchViewState.History) adapterTrackHistory.updateSearchList(state.tracks)
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
                    searchDebounce(false)
                } else {
                    searchRequest = s.toString()
                    showErrorMessage(0)
                    showHistorySearchTrack(false)
                    searchDebounce(true)
                }
                binding.clearIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            showHistorySearchTrack(hasFocus)
        }

        binding.buttonUpdateSearchMusic.setOnClickListener {
            showErrorMessage(0)
            searchDebounce(true)
        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.removeListHistorySearchMusic()
            showHistorySearchTrack(false)
            adapterTrackHistory.historyListAdapter.clear()
        }
    }

    private fun clearSearchRequest() {
        binding.editText.setText("")
        inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.editText.windowToken, 0)
        showErrorMessage(0)
        showHistorySearchTrack(binding.editText.hasFocus())
    }

    private fun render(state: TrackSearchViewState) {
        when (state) {
            is TrackSearchViewState.Loading -> {
                binding.trackList.gone()
                showProgressLoading(true)
            }
            is TrackSearchViewState.Error -> {
                binding.trackList.gone()
                showProgressLoading(false)
                showErrorMessage(2)
            }
            is TrackSearchViewState.Content -> {
                binding.trackList.show()
                showProgressLoading(false)
                showUpdatedListTrack(state.tracks)
            }
            is TrackSearchViewState.History -> {
                adapterTrackHistory.updateSearchList(state.tracks)
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
        if (clickDebounce()) {
            val track = if (binding.trackList.adapter == adapterTrackSearch) {
                adapterTrackSearch.searchListAdapter[position]
            } else {
                adapterTrackHistory.historyListAdapter[position]
            }
            transferTrackToPlayer(track)
            if (binding.trackList.adapter == adapterTrackSearch) {
                viewModel.setToListHistorySearchMusic(track)
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce(isSearchAllowed: Boolean) {
        adapterTrackSearch.searchListAdapter.clear()
        if (isSearchAllowed) {
            searchRunnable?.let { handler.removeCallbacks(it) }
            searchRunnable = Runnable { viewModel.searchMusic(searchRequest) }
            handler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
        } else {
            searchRunnable?.let { handler.removeCallbacks(it) }
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.SEARCH_REQUEST, searchRequest)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
