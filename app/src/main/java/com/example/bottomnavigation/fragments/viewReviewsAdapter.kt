package com.example.bottomnavigation.fragments

import android.app.Activity
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Filterable
import android.widget.TextView
import com.example.bottomnavigation.R
import java.util.*

class viewReviewsAdapter (val context: Activity, private val addresses: ArrayList<String>, private val locations: ArrayList<String>, private val reviews: ArrayList<String>,
                          private val usernames: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.custom_view_reviews_list, locations), Filterable {

    private lateinit var mtts: TextToSpeech

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_view_reviews_list, null, true)

        val ttsButton: Button = rowView.findViewById(R.id.ttsViewReviews) as Button
        val theUsername = rowView.findViewById(R.id.username) as TextView
        val locationTitle = rowView.findViewById(R.id.locationTit) as TextView
        val locationAddy = rowView.findViewById(R.id.addy) as TextView
        val reviewContent = rowView.findViewById(R.id.review) as TextView

        mtts = TextToSpeech(context, TextToSpeech.OnInitListener() {
            fun onInit(status: Int) {
                if (status == TextToSpeech.SUCCESS) {
                    val result = mtts!!.setLanguage(Locale.US)
                    Log.i("TTS", "Worked")
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language not supported!")
                    } else {
                        ttsButton!!.isEnabled = true
                    }
                }
            }

            ttsButton.setOnClickListener {
                speakOut(reviewContent.text as String)
            }
        })

        theUsername.text = usernames[position]
        locationTitle.text = locations[position]
        locationAddy.text = addresses[position]
        reviewContent.text = reviews[position]

        return rowView
    }


    private fun speakOut(reviewText: String) {
        mtts!!.speak(reviewText, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}