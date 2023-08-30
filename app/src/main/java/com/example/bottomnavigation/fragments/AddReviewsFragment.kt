package com.example.bottomnavigation.fragments

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.bottomnavigation.R
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddReviewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddReviewsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var back_button: Button
    lateinit var parent_frag: ReviewOptionsFragment
    lateinit var mtts: TextToSpeech


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_button = view.findViewById(R.id.button5)

        back_button.setOnClickListener(View.OnClickListener {
        //speakOut()
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container,parent_frag).commit()
        })

        mtts =  TextToSpeech(activity,TextToSpeech.OnInitListener(){
            fun onInit(status: Int) {
                if (status == TextToSpeech.SUCCESS) {
                    val result = mtts!!.setLanguage(Locale.US)

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS","The Language not supported!")
                    } else {
                        back_button!!.isEnabled = true
                    }
                }
            }


        })


    }


    private fun speakOut() {
        val text = "hello how are you"
        mtts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (mtts != null) {
            mtts!!.stop()
            mtts!!.shutdown()
        }
        super.onDestroy()
    }


    public fun set_parent(parent_fragment: ReviewOptionsFragment)
    {
        parent_frag = parent_fragment
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddReviewsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddReviewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}