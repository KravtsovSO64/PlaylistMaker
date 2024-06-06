package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val comeBackMain = findViewById<ImageView>(R.id.arrow_back)
        val lineSearchLine = findViewById<EditText>(R.id.editText)
        val clearButtonSearch = findViewById<ImageView>(R.id.clearIcon)

        clearButtonSearch.setOnClickListener{
            lineSearchLine.setText("")
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

    private var searchRequest : String = ""

    companion object{
        const val SEARCH_REQUEST = "SEARCH_REQUEST"
        const val AMOUNT_DEF = ""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST,searchRequest)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val lineSearchLine = findViewById<EditText>(R.id.editText)
        searchRequest = savedInstanceState.getString(SEARCH_REQUEST,AMOUNT_DEF)
        lineSearchLine.setText(searchRequest)
    }


}