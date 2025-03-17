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
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.root.SharedViewModel
import com.practicum.playlistmaker.presentation.search.state.TrackState
import com.practicum.playlistmaker.presentation.search.viewmodel.TrackSearchViewModel
import com.practicum.playlistmaker.ui.player.PlayerFragment
import com.practicum.playlistmaker.ui.search.uicomponents.HistoryTrackAdapter
import com.practicum.playlistmaker.ui.search.uicomponents.OnTrackClickListener
import com.practicum.playlistmaker.ui.search.uicomponents.TrackAdapter
import com.practicum.playlistmaker.utils.Constants
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), OnTrackClickListener {

    private var inputMethodManager: InputMethodManager? = null
    private var searchRequest: String = ""
    private var latestSearchText: String = ""
    private lateinit var recyclerView: RecyclerView

    //Binding
    private lateinit var binding: FragmentSearchBinding

    //ViewModel
    private val viewModel by viewModel<TrackSearchViewModel>()
    private val sharedViewModel: SharedViewModel by activityViewModel()

    //Adapter for RecyclerView
    private val adapterTrackSearch = TrackAdapter(listener = this)
    private val adapterTrackHistory = HistoryTrackAdapter(listener = this)

    //Object for click debounce
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

        viewModel.getListHistorySearchMusic()

        recyclerView = binding.trackList
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
            if (state is TrackState.History) adapterTrackHistory.updateSearchList(state.tracks)
        }

        sharedViewModel.items.observe(viewLifecycleOwner) {
            adapterTrackSearch.updateSearchList(it)
        }

        binding.clearIcon.setOnClickListener {
            sharedViewModel.removeItems()
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
                    sharedViewModel.removeItems()
                    searchJob = null
                } else {
                    showErrorMessage(0)
                    showHistorySearchTrack(false)
                    adapterTrackSearch.searchListAdapter.clear()
                    searchRequest = s.toString()
                }
                binding.clearIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    searchDebounce(searchRequest)
                }
            }
        })

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            showHistorySearchTrack(hasFocus)
        }

        binding.buttonUpdateSearchMusic.setOnClickListener {
            repeatLastRequest()
        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.removeListHistorySearchMusic()
            showHistorySearchTrack(false)
            adapterTrackHistory.historyListAdapter.clear()
            searchJob?.cancel()
        }

    }

    override fun onResume() {
        isClickAllowed = true
        super.onResume()
    }

    private fun clearSearchRequest() {
        searchJob?.cancel()
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

    override fun onItemClick(track: Track) {
       if (clickDebounce()) {
           transferTrackToPlayer(isFavoriteTrack(track))
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
                binding.errorPoster.setImageResource(R.drawable.ic_no_internet)
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
        sharedViewModel.setItems(adapterTrackSearch.searchListAdapter)
        findNavController().navigate(R.id.action_searchFragment_to_playerFragment, PlayerFragment.createArgs(track))
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
            hideKeyboard()
            viewModel.searchMusic(changedText)
        }

    }

    private fun repeatLastRequest() {
        showErrorMessage(0)
        latestSearchText = ""
        searchDebounce(searchRequest)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.SEARCH_REQUEST, searchRequest)
    }

    private fun addTrackToHistory(track: Track){
        viewModel.setToListHistorySearchMusic(track)
        adapterTrackHistory.notifyDataSetChanged()
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editText.windowToken, 0)
    }

    private fun isFavoriteTrack(track: Track): Track {
        viewModel.currentIndexesFavouriteTracks() //Сделали запрос на обновление списка в LiveData
        val indexes = viewModel.isFavorite.value //Забираем значение из LiveData

            return if (indexes != null && indexes.isEmpty()) {
                if (track.trackId in indexes) {
                    track.copy(isFavorite = true)
                } else {
                    track
                }
            } else {
                track
            }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}