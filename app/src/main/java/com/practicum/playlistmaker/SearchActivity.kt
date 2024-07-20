package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), OnTrackClickListener {
    private val baseUrl = "https://itunes.apple.com"
    private var inputMethodManager: InputMethodManager? = null
    private var searchRequest: String = ""
    private lateinit var binding: ActivitySearchBinding
    private lateinit var lineSearchLine: EditText
    private lateinit var comeBackMain: ImageView
    private lateinit var clearButtonSearch: ImageView
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var errorPoster: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var buttonUpdateSearchMusic: Button
    private lateinit var historySearch: HistorySearch
    private lateinit var sharedPrefs: SharedPreferences


    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val musicService = retrofit.create(MusicApi::class.java)

    private val musicList = ArrayList<Track>()
    private val adapterTrackSearch = TrackAdapter(this) //адаптре для списка результата поиска треков
    private val adapterTrackHistory = HistoryTrackAdapter() //адаптер для списка истории поиска треков

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences(HISTORY_SEARCH, MODE_APPEND)
        historySearch = HistorySearch(sharedPrefs)
        adapterTrackHistory.historyListAdapter = historySearch.getListHistory()

        clearButtonSearch = binding.clearIcon
        comeBackMain = binding.arrowBack
        lineSearchLine = binding.editText
        recyclerTrack = binding.trackList
        errorPoster = binding.errorPoster
        errorMessage = binding.errorMessage
        buttonUpdateSearchMusic = binding.buttonUpdateSearchMusic

        recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        clearButtonSearch.setOnClickListener {
            lineSearchLine.setText("")
            inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(lineSearchLine.windowToken, 0)
            showErrorMessgage(0)
            showHistorySearchTract(lineSearchLine.hasFocus())
        }
        comeBackMain.setOnClickListener {
            finish()
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) musicList.clear()
                if (s.isNullOrEmpty()) showErrorMessgage(0)
                clearButtonSearch.visibility = clearButtonVisibility(s)
                searchRequest = s.toString()
                showHistorySearchTract(lineSearchLine.hasFocus())
            }

            override fun afterTextChanged(s: Editable?) {
                //Empty
            }

        }
        lineSearchLine.addTextChangedListener(searchTextWatcher)
        lineSearchLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchMusic()
                true
            }
            false
        }
        lineSearchLine.setOnFocusChangeListener { _ , hasFocus ->
            showHistorySearchTract(hasFocus)
        }
        buttonUpdateSearchMusic.setOnClickListener{
            searchMusic()
        }
        binding.buttonClearHistory.setOnClickListener {
            historySearch.clearTracksToListHistory()
            showHistorySearchTract(lineSearchLine.hasFocus())
        }

    }

    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST, searchRequest)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchRequest = savedInstanceState.getString(SEARCH_REQUEST, AMOUNT_DEF)
        lineSearchLine.setText(searchRequest)
    }

    override fun onResume() {
        super.onResume()
        historySearch.linkedList = historySearch.getListFromMemory(sharedPrefs)
    }

    override fun onStop() {
        super.onStop()
        historySearch.setListToMemory(sharedPrefs)
    }

    override fun onItemClick(position: Int) {
        historySearch.addTrackToListHistory(adapterTrackSearch.searchListAdapter.get(position))
    }


    private fun searchMusic(){
        musicService.getMusic(lineSearchLine.text.toString())
            .enqueue(object : Callback<MusicResponce> {
                override fun onResponse(call: Call<MusicResponce>,
                                        response: Response<MusicResponce>) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                musicList.clear()
                                musicList.addAll(response.body()?.results!!)
                                recyclerTrack.adapter = adapterTrackSearch
                                adapterTrackSearch.searchListAdapter = musicList
                                adapterTrackSearch.notifyDataSetChanged()
                                adapterTrackHistory.notifyDataSetChanged()
                                showErrorMessgage(0)
                            } else {
                                showErrorMessgage(1)
                            }

                        }
                        401 ->  {
                            showErrorMessgage(2)
                        }
                    }

                }

                override fun onFailure(call: Call<MusicResponce>, t: Throwable) {
                    showErrorMessgage(2)
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
                        true -> errorPoster.setImageResource(R.drawable.ic_no_internet_dark)
                        else -> errorPoster.setImageResource(R.drawable.ic_no_internet_ligth)
                    }
                }

            })

    }

    private fun showErrorMessgage(status: Int){
        when (status) {
            0 ->{
                buttonUpdateSearchMusic.visibility = View.GONE
                errorPoster.visibility = View.GONE
                errorMessage.visibility = View.GONE
                recyclerTrack.visibility =View.VISIBLE
            }
            1 -> {
                recyclerTrack.visibility =View.GONE
                buttonUpdateSearchMusic.visibility = View.GONE
                errorPoster.visibility = View.VISIBLE
                errorMessage.visibility = View.VISIBLE
                errorMessage.text =resources.getText(R.string.noFoundСontent)
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
                    true -> errorPoster.setImageResource(R.drawable.ic_not_found_dark)
                    else -> errorPoster.setImageResource(R.drawable.ic_not_found_ligth)
                }
            }
            2 -> {
                recyclerTrack.visibility =View.GONE
                errorPoster.visibility = View.VISIBLE
                errorMessage.visibility = View.VISIBLE
                buttonUpdateSearchMusic.visibility = View.VISIBLE
                errorMessage.text =resources.getText(R.string.noInternetСontent)
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
                    true -> errorPoster.setImageResource(R.drawable.ic_no_internet_dark)
                    else -> errorPoster.setImageResource(R.drawable.ic_no_internet_ligth)
                }
            }
            else -> {
                buttonUpdateSearchMusic.visibility = View.GONE
                errorPoster.visibility = View.GONE
                errorMessage.visibility = View.GONE
                recyclerTrack.visibility =View.VISIBLE
            }
        }
    }

    private fun showHistorySearchTract(hasFocus: Boolean) {
        if (hasFocus && lineSearchLine.text.isEmpty() && historySearch.getListHistory().isNotEmpty()) {
            binding.hintTextSearch.visibility = View.VISIBLE
            binding.buttonClearHistory.visibility = View.VISIBLE
            recyclerTrack.adapter = adapterTrackHistory
        } else {
            binding.hintTextSearch.visibility = View.GONE
            binding.buttonClearHistory.visibility = View.GONE
            recyclerTrack.adapter = adapterTrackSearch
        }
        adapterTrackHistory.historyListAdapter = historySearch.getListHistory()
        adapterTrackHistory.notifyDataSetChanged()
        adapterTrackSearch.notifyDataSetChanged()
    }

    companion object {
        private const val SEARCH_REQUEST = "SEARCH_REQUEST"
        private const val AMOUNT_DEF = ""
        private const val HISTORY_SEARCH = "history_search"
    }
}
