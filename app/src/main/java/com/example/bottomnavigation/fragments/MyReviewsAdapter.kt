package com.example.bottomnavigation.fragments

import android.app.Activity
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.review
import com.example.bottomnavigation.R
import java.util.*

class MyReviewsAdapter(val context: Activity,private val addresses: ArrayList<String>, private val locations: ArrayList<String>, private val reviews: ArrayList<String>,
                       private val ids: ArrayList<String>)
    : ArrayAdapter<String>(context, R.layout.custom_my_review_list, locations) {

    private lateinit var mtts: TextToSpeech

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_my_review_list, null, true)

        val ttsButton: Button = rowView.findViewById(R.id.tts) as Button
        val locationTitle = rowView.findViewById(R.id.locationTit) as TextView
        val locationAddy = rowView.findViewById(R.id.addy) as TextView
        val reviewContent = rowView.findViewById(R.id.review) as TextView

        val deleteButton = rowView.findViewById(R.id.delete) as Button

        deleteButton.setOnClickListener {
            deleteReview(ids[position])
        }

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

        locationTitle.text = locations[position]
        locationAddy.text = addresses[position]
        reviewContent.text = reviews[position]

        return rowView
    }

    fun deleteReview(s: String) {
        Amplify.DataStore.query(review::class.java, Where.id(s),
            { allReviews ->
                if (allReviews.hasNext()) {
                    val reviewx = allReviews.next()
                    Amplify.DataStore.delete(reviewx,
                        { Log.i("MyAmplifyApp", "Deleted a post.") },
                        { Log.e("MyAmplifyApp", "Delete failed.", it) }
                    )
                }
            },
            { Log.e("MyAmplifyApp", "Query failed.") }
        )
        var elementIndex: Int = ids.indexOf(s)
        ids.remove(s)
        locations.removeAt(elementIndex)
        reviews.removeAt(elementIndex)
        notifyDataSetChanged()
    }

    private fun speakOut(reviewText:String) {
        mtts!!.speak(reviewText, TextToSpeech.QUEUE_FLUSH, null, "")
    }

//    fun onDestroy() {
//        // Shutdown TTS when
//        // activity is destroyed
//        if (mtts != null) {
//            mtts!!.stop()
//            mtts!!.shutdown()
//        }
//        super.onDestroy()
//    }




}
