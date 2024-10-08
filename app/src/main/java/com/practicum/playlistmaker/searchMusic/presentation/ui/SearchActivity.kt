package com.practicum.playlistmaker.searchMusic.presentation.ui

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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.searchMusic.creator.Creator
import com.practicum.playlistmaker.searchMusic.domain.api.MusicNetworkInteractor
import com.practicum.playlistmaker.searchMusic.domain.models.Track
import com.practicum.playlistmaker.searchMusic.presentation.uiComponents.HistoryTrackAdapter
import com.practicum.playlistmaker.searchMusic.presentation.uiComponents.OnTrackClickListener
import com.practicum.playlistmaker.searchMusic.presentation.uiComponents.TrackAdapter


class SearchActivity : AppCompatActivity(), OnTrackClickListener {

    private var inputMethodManager: InputMethodManager? = null
    private var searchRequest: String = ""
    private lateinit var binding: ActivitySearchBinding
    private lateinit var lineSearchLine: EditText
    private lateinit var clearButtonSearch: ImageView
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var errorPoster: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var buttonUpdateSearchMusic: Button

    //Набор переменных для работы с RecyclerView
    private val adapterTrackSearch = TrackAdapter(this) //адаптре для списка результата поиска треков
    private val adapterTrackHistory = HistoryTrackAdapter(this) //адаптер для списка истории поиска треков

    //Набор переменных для работы с многопоточностью
    private val handler = Handler(Looper.getMainLooper()) //Handler для передачи Runnable объектов в главный поток
    private var searchRunnable: Runnable? = null
    private var isClickAllowed = true


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clearButtonSearch = binding.clearIcon
        lineSearchLine = binding.editText
        recyclerTrack = binding.trackList
        errorPoster = binding.errorPoster
        errorMessage = binding.errorMessage
        buttonUpdateSearchMusic = binding.buttonUpdateSearchMusic


        recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        //Очищает поисковую строку
        clearButtonSearch.setOnClickListener {
            lineSearchLine.setText("")
            inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(lineSearchLine.windowToken, 0)
            showErrorMessgage(0)
            showHistorySearchTract(lineSearchLine.hasFocus())
            it.visibility = View.GONE
        }

        //Возвращает на прошлый экран
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
                    searchDebounce(false)
                    showHistorySearchTract(lineSearchLine.hasFocus())
                } else {
                    clearButtonSearch.visibility = clearButtonVisibility(s)
                    searchRequest = s.toString()
                    showHistorySearchTract(false)
                    searchDebounce(true)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //Empty
            }

        }

        lineSearchLine.addTextChangedListener(searchTextWatcher)

        //Проверяет взаимодействуем ли мы (в Фокусе) с editeText, в зависимости от состояния показывает/скрывает историю
        lineSearchLine.setOnFocusChangeListener { _ , hasFocus ->
            showHistorySearchTract(hasFocus)
        }
        //Повторяет поисковый запрос
        buttonUpdateSearchMusic.setOnClickListener{
           //Empty
        }

        //Очищает историю прослушанных треков
        binding.buttonClearHistory.setOnClickListener {
            Creator.removeHistory.execute()
            showHistorySearchTract(false)
        }
    }

    //Решает вопрос, показывать или не показывать кнопку "очистить"
    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    //Отрабатывает нажатие на песню в списке
    override fun onItemClick(position: Int) {
       if (clickDebounce()){
           if (recyclerTrack.adapter == adapterTrackSearch) {
               val track = adapterTrackSearch.searchListAdapter[position]
               parcelableTrack(track)
               Creator.setHistory.execute(track)
           } else {
               parcelableTrack(adapterTrackHistory.historyListAdapter[position])
           }
       }
    }

    //Поиск трека
    private fun searchTrack(): Runnable {
        return Runnable {
            showProgressBar(true)
            Creator.provideMusicInteractor().searchTrack(lineSearchLine.text.toString(), object : MusicNetworkInteractor.MusicConsumer {
                override fun consumer(foundMusic: List<Track>) {
                    handler.post {
                        showProgressBar(false)
                        updateListUI(foundMusic)
                    }
                }

            })
        }
    }

    //Обновление списка найденных треков
    private fun updateListUI(foundMusic: List<Track>){
        adapterTrackSearch.updateSearchList(foundMusic)
        recyclerTrack.adapter = adapterTrackSearch
    }

    private fun showProgressBar(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

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
        if (isSearchAllowed) {
            //Удаляем старый runnable
            searchRunnable?.let { handler.removeCallbacks(it) }

            searchRunnable = searchTrack()
            // Запускаем новый Runnable с задержкой
            handler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
        } else {
            searchRunnable?.let { handler.removeCallbacks(it) }
        }
    }

    //Решает вопрос какую ошибку показать пользователю
    private fun showErrorMessgage(status: Int){
        when (status) {
            0 ->{
                buttonUpdateSearchMusic.visibility = View.GONE
                errorPoster.visibility = View.GONE
                errorMessage.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                recyclerTrack.visibility =View.VISIBLE
            }
            1 -> {
                recyclerTrack.visibility =View.GONE
                buttonUpdateSearchMusic.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
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
                binding.progressBar.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
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

    //Решает вопрос показывать или не показывать историю прослушанных треков
    private fun showHistorySearchTract(hasFocus: Boolean) {
        if (hasFocus && lineSearchLine.text.isEmpty() && Creator.getHistory.execute().isNotEmpty()) {
            binding.hintTextSearch.visibility = View.VISIBLE
            binding.buttonClearHistory.visibility = View.VISIBLE
            adapterTrackHistory.updateSearchList(Creator.getHistory.execute())
            recyclerTrack.adapter = adapterTrackHistory
            adapterTrackHistory.notifyDataSetChanged()
        } else {
            binding.hintTextSearch.visibility = View.GONE
            binding.buttonClearHistory.visibility = View.GONE

            recyclerTrack.adapter = adapterTrackSearch
            adapterTrackSearch.notifyDataSetChanged()
        }
    }

    private fun parcelableTrack(track: Track) {
        val playerIntent = Intent(this, PlayerActivity::class.java).apply {
            putExtra("track", track)
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
        lineSearchLine.setText(searchRequest)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY =  2000L
    }
}
