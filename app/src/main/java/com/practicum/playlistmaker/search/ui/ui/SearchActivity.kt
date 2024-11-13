package com.practicum.playlistmaker.search.ui.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.state.TrackSearchState
import com.practicum.playlistmaker.search.presentation.viewmodel.TrackSearchViewModel
import com.practicum.playlistmaker.search.ui.uiComponents.HistoryTrackAdapter
import com.practicum.playlistmaker.search.ui.uiComponents.OnTrackClickListener
import com.practicum.playlistmaker.search.ui.uiComponents.TrackAdapter


class SearchActivity : AppCompatActivity(), OnTrackClickListener {

    private var inputMethodManager: InputMethodManager? = null
    private var searchRequest: String = ""
    private lateinit var binding: ActivitySearchBinding
    private val viewModel by lazy {
        ViewModelProvider(this, TrackSearchViewModel.factory())[TrackSearchViewModel::class.java]
    }

    private val adapterTrackSearch = TrackAdapter(listener = this)
    private val adapterTrackHistory = HistoryTrackAdapter(listener = this)

    private val handler = Handler(Looper.getMainLooper()) //Handler для передачи Runnable объектов в главный поток
    private var searchRunnable: Runnable? = null
    private var isClickAllowed = true


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getListHistorySearchMusic()

        binding.trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel.state.observe(this) { state ->
           render(state)
        }

        viewModel.listHistory.observe(this) {
            viewModel.listHistory.value?.let { adapterTrackHistory.updateSearchList(it) }
        }

        binding.clearIcon.setOnClickListener {
            binding.editText.setText("")
            inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.editText.windowToken, 0)
            showErrorMessgage(0)
            showHistorySearchTract(binding.editText.hasFocus())
            it.visibility = View.GONE
        }

        binding.arrowBack.setOnClickListener { finish() }

        //Наблюдает за изменениями в editeText
        val searchTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    adapterTrackSearch.searchListAdapter.clear()
                    showErrorMessgage(0)
                    viewModel.getListHistorySearchMusic()
                    showHistorySearchTract(binding.editText.hasFocus())
                    searchDebounce(false)
                } else {
                    searchRequest = s.toString()
                    showErrorMessgage(0)
                    showHistorySearchTract(false)
                    searchDebounce(true)
                }
                binding.clearIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                //Empty
            }

        }

        binding.editText.addTextChangedListener(searchTextWatcher)

        //Проверяет взаимодействуем ли мы (в Фокусе) с editeText, в зависимости от состояния показывает/скрывает историю
        binding.editText.setOnFocusChangeListener { _ , hasFocus ->
            showHistorySearchTract(hasFocus)
        }
        //Повторяет поисковый запрос
        binding.buttonUpdateSearchMusic.setOnClickListener{
            showErrorMessgage(0)
            searchDebounce(true)
        }

        //Очищает историю прослушанных треков
        binding.buttonClearHistory.setOnClickListener {
            viewModel.removeListHistorySearchMusic()
            showHistorySearchTract(false)
        }
    }

    private fun render(state: TrackSearchState) {
        when (state) {
            is TrackSearchState.Loading -> {
                binding.trackList.visibility = View.GONE
                showProgressLoading(true)
            }
            is TrackSearchState.Error -> {
                binding.trackList.visibility = View.GONE
                showProgressLoading(false)
                showErrorMessgage(2)
            }
            is TrackSearchState.Content -> {
                binding.trackList.visibility = View.VISIBLE
                showProgressLoading(false)
                showUpdatedListTrack(state.data)
            }
        }
    }

    private fun showProgressLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showUpdatedListTrack(list : List<Track>) {
        if (!list.isNullOrEmpty()) adapterTrackSearch.updateSearchList(list) else showErrorMessgage(1)
        adapterTrackSearch.notifyDataSetChanged()
    }

    //Отрабатывает нажатие на песню в списке
    override fun onItemClick(position: Int) {
       if (clickDebounce()){
           if (binding.trackList.adapter == adapterTrackSearch) {
               val track = adapterTrackSearch.searchListAdapter[position]
               parcelableTrack(track)
               viewModel.setToListHistorySearchMusic(track)
           } else {
               parcelableTrack(adapterTrackHistory.historyListAdapter[position])
           }
       }
    }

    //Очередь нажатия на треки в списке
    private fun clickDebounce() : Boolean{
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    //Очередь поиска трека
    private fun searchDebounce(isSearchAllowed : Boolean){
        adapterTrackSearch.searchListAdapter.clear()
        if (isSearchAllowed) {
            searchRunnable?.let { handler.removeCallbacks(it) }
            searchRunnable = Runnable { viewModel.searchMusic(searchRequest) }
            handler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
        } else {
            searchRunnable?.let { handler.removeCallbacks(it) }
        }
    }

    private fun showErrorMessgage(status: Int){
        when (status) {
            0 ->{
                binding.buttonUpdateSearchMusic.visibility = View.GONE
                binding.errorPoster.visibility = View.GONE
                binding.errorMessage.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.trackList.visibility =View.VISIBLE
            }
            1 -> {
                binding.trackList.visibility =View.GONE
                binding.buttonUpdateSearchMusic.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.errorPoster.visibility = View.VISIBLE
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text =resources.getText(R.string.noFoundСontent)
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
                    true -> binding.errorPoster.setImageResource(R.drawable.ic_not_found_dark)
                    else -> binding.errorPoster.setImageResource(R.drawable.ic_not_found_ligth)
                }
            }
            2 -> {
                binding.trackList.visibility =View.GONE
                binding.progressBar.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.errorPoster.visibility = View.VISIBLE
                binding.errorMessage.visibility = View.VISIBLE
                binding.buttonUpdateSearchMusic.visibility = View.VISIBLE
                binding.errorMessage.text =resources.getText(R.string.noInternetСontent)
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
                    true -> binding.errorPoster.setImageResource(R.drawable.ic_no_internet_dark)
                    else -> binding.errorPoster.setImageResource(R.drawable.ic_no_internet_ligth)
                }
            }
            else -> {
                binding.buttonUpdateSearchMusic.visibility = View.GONE
                binding.errorPoster.visibility = View.GONE
                binding.errorMessage.visibility = View.GONE
                binding.trackList.visibility =View.VISIBLE
            }
        }
    }

    private fun showHistorySearchTract(hasFocus: Boolean) {
        if (hasFocus && binding.editText.text.isEmpty() && !adapterTrackHistory.historyListAdapter.isNullOrEmpty()) {
            binding.hintTextSearch.visibility = View.VISIBLE
            binding.buttonClearHistory.visibility = View.VISIBLE
            binding.trackList.adapter = adapterTrackHistory
            adapterTrackHistory.notifyDataSetChanged()
        } else {
            binding.hintTextSearch.visibility = View.GONE
            binding.buttonClearHistory.visibility = View.GONE

            binding.trackList.adapter = adapterTrackSearch
            adapterTrackSearch.notifyDataSetChanged()
        }
    }

    private fun parcelableTrack(track: Track) {
        val playerIntent = Intent(this, PlayerActivity::class.java).apply {
            putExtra(Creator.TRACK, track)
        }
        startActivity(playerIntent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(Creator.SEARCH_REQUEST, searchRequest)
}

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchRequest = savedInstanceState.getString(Creator.SEARCH_REQUEST, Creator.AMOUNT_DEF)
        binding.editText.setText(searchRequest)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY =  2000L
    }
}
