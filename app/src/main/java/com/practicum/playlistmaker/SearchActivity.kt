package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var lineSearchLine : EditText
    private lateinit var comeBackMain : ImageView
    private lateinit var clearButtonSearch : ImageView
    private var inputMethodManager : InputMethodManager? = null
    private var searchRequest : String = ""

            @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

         comeBackMain = findViewById(R.id.arrow_back)
         lineSearchLine = findViewById(R.id.editText)
         clearButtonSearch = findViewById(R.id.clearIcon)

        clearButtonSearch.setOnClickListener{
            lineSearchLine.setText("")
            inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(lineSearchLine.windowToken, 0)
        }
        comeBackMain.setOnClickListener {
            finish()
        }

        val searchTextWatcher = object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButtonSearch.visibility = clearButtonVisibility(s)
                searchRequest = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //Empty
            }

        }
        lineSearchLine.addTextChangedListener(searchTextWatcher)
    }
     fun clearButtonVisibility(s : CharSequence?) : Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST,searchRequest)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchRequest = savedInstanceState.getString(SEARCH_REQUEST,AMOUNT_DEF)
        lineSearchLine.setText(searchRequest)
    }
    companion object{
        private const val SEARCH_REQUEST = "SEARCH_REQUEST"
        private const val AMOUNT_DEF = ""
    }
}