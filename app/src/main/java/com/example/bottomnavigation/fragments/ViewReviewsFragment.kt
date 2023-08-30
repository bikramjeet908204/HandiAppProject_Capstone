package com.example.bottomnavigation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
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
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewReviewsFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var listView: ListView
    lateinit var locations: ArrayList<String>
    lateinit var addresses: ArrayList<String>
    lateinit var usernames: ArrayList<String>
    lateinit var therevs: ArrayList<String>
    lateinit var adapter: ArrayAdapter<*>
    lateinit var currentAddress: String
    lateinit var currentLocation: String
    lateinit var back_button: Button
    lateinit var parent_frag: ReviewOptionsFragment
    lateinit var searchBar: SearchView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater!!.inflate(R.layout.view_reviews, container, false);

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val parent_frag: ReviewOptionsFragment = parentFragment as ReviewOptionsFragment

        listView = view.findViewById(R.id.listviewreviews)
        back_button = view.findViewById(R.id.button3)
        searchBar = view.findViewById(R.id.search)
        locations = ArrayList()
        usernames = ArrayList()
        addresses = ArrayList()
        therevs = ArrayList()

        adapter = viewReviewsAdapter(requireActivity(), addresses, locations, therevs, usernames)
        listView.adapter = adapter

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute{

            Amplify.DataStore.query(
                review::class.java, Where.matches(review.LOCATION_ADDRESS.eq(currentAddress).and(review.LOCATION_TITLE.eq(currentLocation))),
                { matches ->
                    while(matches.hasNext()) {
                        Log.i("AmplifyApp","Location Already Exists")

                        val reviewx = matches.next()
                        ThreadUtils.runOnUiThread(Runnable{
                            addresses.add(reviewx.locationAddress)
                            locations.add(reviewx.locationTitle)
                            therevs.add(reviewx.description)
                            usernames.add(reviewx.username)
                            adapter.notifyDataSetChanged()
                        })
                        //Log.i("AmplifyApp","Title:${reviewx.locationTitle}")
                    }
                    Log.i("Tester","Do we get here?")
                    //Thread { while (myReviewList.count === 0); }.start()
                },
                {
                    Log.e("AmplifyApp","Query failed",it)}
            )

            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    locations = ArrayList()
                    usernames = ArrayList()
                    addresses = ArrayList()
                    therevs = ArrayList()

                    adapter = viewReviewsAdapter(requireActivity(), addresses, locations, therevs, usernames)
                    listView.adapter = adapter

                    Amplify.DataStore.query(
                        review::class.java, Where.matches(review.LOCATION_ADDRESS.eq(currentAddress).and(review.LOCATION_TITLE.eq(currentLocation)).and(review.DESCRIPTION.contains(query))),
                        { matches ->
                            while(matches.hasNext()) {
                                Log.i("AmplifyApp","Location Already Exists")

                                val reviewx = matches.next()
                                ThreadUtils.runOnUiThread(Runnable{
                                    addresses.add(reviewx.locationAddress)
                                    locations.add(reviewx.locationTitle)
                                    therevs.add(reviewx.description)
                                    usernames.add(reviewx.username)
                                    adapter.notifyDataSetChanged()
                                })
                                Log.i("AmplifyApp","Title:${reviewx.locationTitle}")
                            }
                            Log.i("Tester","Do we get here?")
                            //Thread { while (myReviewList.count === 0); }.start()
                        },
                        {
                            Log.e("AmplifyApp","Query failed",it)}
                    )
                    //Added by tyler
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
//                if (TextUtils.isEmpty(newText)) {
//                    listView.visibility = View.GONE
//                } else {
//                    adapter.filter.filter(newText)
//                    listView.visibility = View.VISIBLE
//                }
                    return false
                }
            })
        }

        handler.post{
            adapter.notifyDataSetChanged()
        }


        back_button = view.findViewById(R.id.button3)
        back_button.setOnClickListener(View.OnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container,parent_frag).commit()
        })

    }
    fun setCurrent(add: String, loc: String){
        currentAddress = add
        currentLocation = loc
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}