package com.example.bottomnavigation.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bottomnavigation.PermissionsHelper
import com.example.bottomnavigation.R
import com.example.bottomnavigation.fragments.models.PlacesResponse
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val rev_opt = ReviewOptionsFragment()
    private lateinit var mapFrag: SupportMapFragment

    private val LOCATION_REQUEST_CODE = 101

    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var email: EditText
    lateinit var list_ittem_selected: String
    lateinit var locationManager: LocationManager
    var permissionsHelper = PermissionsHelper()
    var lastKnownLocation: Location? = null
    lateinit var list: ArrayList<String>
    var place_review_list = mutableMapOf<String, ArrayList<String>>()
    lateinit var adapter: ArrayAdapter<*>
    var myMap: GoogleMap? = null
    lateinit var marker: Marker
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var markerObject: MarkerOptions
    lateinit var mAdView : AdView



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                lastKnownLocation = location
            }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var v = inflater.inflate(R.layout.fragment_home, container, false)
        mapFrag = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFrag.getMapAsync(this)
        permissionsHelper.requestLocation(this)
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //do the query earlier to mayble load it in earlier.
        searchView = view.findViewById(R.id.search)


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                if (list.contains(query)) {
//                    adapter.filter.filter(query)
                //Added by tyler
                showOnMap(query)
//                }
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

        mAdView = view.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


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

    private fun pointToPosition(position: LatLng, googleMap: GoogleMap) {
        //Build camera position
        val cameraPosition = CameraPosition.Builder()
            .target(position)
            .zoom(15f).build()
        //Zoom in and animate the camera.
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun markLocation(latlng: LatLng,  name: String, address: String) {
        myMap?.addMarker(
            MarkerOptions()
                .position(latlng)
                .title(name)
                .snippet(address)
        )

        pointToPosition(latlng, myMap!!)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap

        if(myMap != null) {
            val permission = ContextCompat.checkSelfPermission(
                requireView().context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            if (permission == PackageManager.PERMISSION_GRANTED) {
                myMap?.isMyLocationEnabled = true
            } else {
                requestPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_REQUEST_CODE
                )
            }
        }

        var lastLoc = LatLng(51.0447, -114.0719)
        /*if(lastKnownLocation != null){
            lastLoc = LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
        }*/
        googleMap.setOnInfoWindowClickListener {
            var title = it.title
            var pos = it.position
            var MapsId = it.id
            var address = it.snippet
            Log.i("testingMaps", title.toString())
            Log.i("testingMaps", pos.toString())
            Log.i("testingMaps", MapsId)
            Log.i("testingMaps", address.toString())

            rev_opt.getPlaceInfo(address.toString(),title.toString(),pos.toString())
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, rev_opt)
                .commit()

        }

        markLocation(lastLoc, "Current Location", "")

    }


    fun showOnMap(query: String){
        myMap?.clear()
        val placesResponse = getPlace(query) ?: return
        val lat = placesResponse.candidates[0].geometry.location.lat
        val lng = placesResponse.candidates[0].geometry.location.lng
        val latlng = LatLng(lat, lng)
        markLocation(latlng,placesResponse.candidates[0].name, placesResponse.candidates[0].formatted_address)



    }

    private fun getPlace(query: String): PlacesResponse? {
        var placesResponse: PlacesResponse?
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=${query}&inputtype=textquery&locationbias=point:${lastKnownLocation?.latitude},${lastKnownLocation?.longitude}&fields=geometry%2Cname%2Cformatted_address&key=AIzaSyDfEkqVY4To9F1q9zmwm-BArnL-Pf49D-g"
            )
            .build()

        runBlocking {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                val response = client.newCall(request).execute()
                val gson = Gson()
                val body = response.body?.string()
                val result = gson.fromJson(body, PlacesResponse::class.java)
                placesResponse = result.copy()
            }
        }
        return placesResponse
    }

    private fun requestPermission(permissionType: String,
                                   requestCode: Int) {

        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(permissionType), requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                             permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            LOCATION_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] !=
                    PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireView().context,
                        "Unable to show location - permission required",
                        Toast.LENGTH_LONG).show()
                } else {

                    val mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
                    mapFragment.getMapAsync(this)
                }
            }
        }
    }
}
