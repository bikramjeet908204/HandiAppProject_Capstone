package com.example.bottomnavigation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.amazonaws.mobile.client.AWSMobileClient
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.review
import com.example.bottomnavigation.R
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyReviewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class MyReviewsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var myReviewList: ListView
    private lateinit var locations: ArrayList<String>
    private lateinit var reviews: ArrayList<String>
    private lateinit var ids: ArrayList<String>
    private lateinit var addresses: ArrayList<String>
    private lateinit var dates: ArrayList<String>
    lateinit var myReviewAdapter: ArrayAdapter<*>


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

        return inflater.inflate(R.layout.fragment_my_reviews, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myReviewList = view.findViewById(R.id.myReviewList)

        addresses = ArrayList()
        locations = ArrayList()
        reviews = ArrayList()
        dates = ArrayList()
        ids = ArrayList()

        myReviewAdapter = MyReviewsAdapter(requireActivity(), addresses, locations, reviews, ids)
        myReviewList.adapter = myReviewAdapter

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute{

            Amplify.DataStore.query(review::class.java, Where.matches(
                review.USERNAME.eq(AWSMobileClient.getInstance().username.toString())),
            { allReviews ->
                if(!allReviews.hasNext()) Log.i("AmplifyApp","No Reviews")
                while(allReviews.hasNext()){
                    val reviewx = allReviews.next()
                    Log.i("usernames", reviewx.username)
                    Log.i("usernames", AWSMobileClient.getInstance().username.toString())
                    ThreadUtils.runOnUiThread(Runnable{
                        addresses.add(reviewx.locationAddress)
                        locations.add(reviewx.locationTitle)
                        reviews.add(reviewx.description)
                        //dates.add(reviewx.createdAt.toString())
                        ids.add(reviewx.id)
                        myReviewAdapter.notifyDataSetChanged()
                    })
                    Log.i("AmplifyApp","Title:${reviewx.locationTitle}")
                }
                Log.i("Tester","Do we get here?")
                //Thread { while (myReviewList.count === 0); }.start()
            },
            {
                Log.e("AmplifyApp","Query failed",it)}
            )
        }

        handler.post{
            myReviewAdapter.notifyDataSetChanged()
        }

    }


        /*
        locations.add("MarketMall")
        locations.add("Superstore")
        locations.add("Rexall")
        locations.add("edo")
        locations.add("chatime")
        locations.add("gongcha")
*/
       /* reviews.add("As I have recently received an injury to my leg, I'm stuck moving in crutches." +
                "The option to take an elevator has made life a lot easier.")
        reviews.add("The automatic doors make life easier as I am sitting in a wheel chair.")
        reviews.add("Love the automatic doors")
        reviews.add("Love the automatic doors")
        reviews.add("Love the automatic doors")
        reviews.add("Love the automatic doors")
*/
//        Amplify.DataStore.clear(
//            { Log.i("MyAmplifyApp", "clear complete") },
//            { Log.e("MyAmplifyApp", "clear failed", it) }
//        )

//        val reviewx = review.builder()
//            .username("testing")
//            .location("Real Canadian Superstore")
//            .description("This establishment had ramps, that allowed me to access anywhere.")
//            .build()
//
//        Amplify.DataStore.save(reviewx,
//            { Log.i("MyAmplifyApp", "Saved a post") },
//            { Log.e("MyAmplifyApp", "Save failed", it) }
//        )





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyReviewsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyReviewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}