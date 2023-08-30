package com.example.bottomnavigation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.amazonaws.mobile.client.AWSMobileClient
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.location
import com.amplifyframework.datastore.generated.model.review
import com.example.bottomnavigation.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LocationInfo.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReviewOptionsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val view_rev = ViewReviewsFragment()
    private val add_rev = AddReviewsFragment()
     var isSent:Boolean =false
    lateinit var add_review_button : Button
    lateinit var view_reviews_button : Button
    lateinit var text_field : TextView
    lateinit var review_list : ArrayList<String>
    lateinit var added_Review : EditText


    lateinit var address : String
    lateinit var title : String
    lateinit var latlong : String




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
        return inflater.inflate(R.layout.review_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_review_button = view.findViewById(R.id.button4)
        view_reviews_button = view.findViewById(R.id.button2)
        text_field = view.findViewById(R.id.editTextTextPersonName)
        added_Review = view.findViewById(R.id.addReview)
        text_field.text = title




        Amplify.DataStore.query(location::class.java, Where.matches(location.ADDRESS.eq(address).and(location.TITLE.eq(title))),
            { matches ->
                if (matches.hasNext()) {
                    Log.i("AmplifyApp","Location Already Exists")

                }
                else{
                    val newLocation = location.builder()
                        .address(address)
                        .title(title)
                        .latlong(latlong)
                        .build()
                    Amplify.DataStore.save(newLocation,
                        {
                            ThreadUtils.runOnUiThread(Runnable{
                                Toast.makeText(this.activity,"New Location Found!", Toast.LENGTH_SHORT).show()
                            })
                            Log.i("MyAmplifyApp", "Submitted Review")
                        },
                        {
                            ThreadUtils.runOnUiThread(Runnable{
                                Toast.makeText(this.activity,"Review Submission Unsuccessful", Toast.LENGTH_SHORT).show()
                            })
                            Log.e("MyAmplifyApp", "Review Submission Failed", it)

                        }
                    )
                }
            },
            { Log.e("MyAmplifyApp", "Query failed", it) }
        )






        view_reviews_button.setOnClickListener(View.OnClickListener {
            view_rev.setCurrent(address, title)
            view_rev.set_parent(this)
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container,view_rev).commit()
        })

        add_review_button.setOnClickListener(View.OnClickListener {
            val reviewx = review.builder()
                .username(AWSMobileClient.getInstance().username)
                .locationTitle(title)
                .locationAddress(address)
                .description(added_Review.text.toString())
                .build()
                Amplify.DataStore.save(reviewx,
                    {
                        ThreadUtils.runOnUiThread(Runnable{
                            Toast.makeText(this.activity,"Review Submission Successful", Toast.LENGTH_SHORT).show()
                        })
                        Log.i("MyAmplifyApp", "Saved a post")
                    },
                    {
                        ThreadUtils.runOnUiThread(Runnable{
                            Toast.makeText(this.activity,"Feedback Submission Unsuccessful", Toast.LENGTH_SHORT).show()
                        })
                        Log.e("MyAmplifyApp", "Save failed", it)
                    }
                )

            added_Review.setText("")

        })


    }



    public fun getPlaceInfo(add: String,titl:String,lat :String){

        address=add
        title = titl
        latlong=lat

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LocationInfo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReviewOptionsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}