package com.example.bottomnavigation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.feedback
import com.amplifyframework.datastore.generated.model.review
import com.example.bottomnavigation.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Feedback.newInstance] factory method to
 * create an instance of this fragment.
 */
class Feedback : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var submit_feedback : Button
    lateinit var feedback_text : EditText

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
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submit_feedback = view.findViewById(R.id.feedback)
        feedback_text = view.findViewById(R.id.addFeedback)

        submit_feedback.setOnClickListener(View.OnClickListener  {
            val feedback = feedback.builder()
                .feedbackText(feedback_text.text.toString())
                .build()

            Amplify.DataStore.save(feedback,
                {
                    ThreadUtils.runOnUiThread(Runnable{
                        Toast.makeText(this.activity,"Feedback Submission Successful", Toast.LENGTH_SHORT).show()
                    })
                    Log.i("MyAmplifyApp", "Submitted Feedback")
                },
                {
                    ThreadUtils.runOnUiThread(Runnable{
                        Toast.makeText(this.activity,"Feedback Submission Unsuccessful", Toast.LENGTH_SHORT).show()
                    })
                    Log.e("MyAmplifyApp", "Feedback Submission Failed", it)

                }
            )
            feedback_text.setText("")
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment feedback.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Feedback().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}