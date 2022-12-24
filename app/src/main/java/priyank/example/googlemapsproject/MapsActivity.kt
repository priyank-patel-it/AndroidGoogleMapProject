package priyank.example.googlemapsproject

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import priyank.example.googlemapsproject.databinding.ActivityMapsBinding
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.*


    class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

        private lateinit var mMap: GoogleMap
        private lateinit var binding: ActivityMapsBinding
        var lat:Double = 43.012440;
        var log:Double = -81.200180;
        var fanshawe = LatLng(lat,log)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityMapsBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        override fun onMapReady(googleMap: GoogleMap) {
            mMap = googleMap

            // Add a marker in Sydney and move the camera
            val sydney = LatLng(-34.0, 151.0)
            mMap.addMarker(MarkerOptions().position(fanshawe).title("Fanshawe college"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fanshawe,15f))

            setMapLongClick(mMap)
            // set a blue dot to the current location

            // shows the zoom controls
            mMap.uiSettings.setZoomControlsEnabled(true)
            // shows the traffic conditions
            mMap.setTrafficEnabled(true)
        }

        // creating a menu to change map type
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            val inflater = menuInflater
            inflater.inflate(R.menu.menu_options, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
            // Change the map type based on the user's selection.
            R.id.map -> {
//                val intent = Intent(this, MapsActivity::class.java).apply {
//                }
//                startActivity(intent)
                true
            }
            R.id.location -> {
//            mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                val intent = Intent(this, addressActivitynew::class.java).apply {
                    putExtra("passingLat", lat)
                    putExtra("passingLong", log)
                }
                resultLauncher.launch(intent)
                true
            }
            R.id.send_sms -> {
//                val intent = Intent(this, emailActivity::class.java).apply {
//                }
//                startActivity(intent)
                true
            }
            R.id.about -> {
////
                val intent = Intent(this, AboutActivity::class.java).apply {
                }
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
           // val setAndroidphone = result.data?.getIntExtra("setAndroid",0)
             lat = result.data?.getDoubleExtra("passingLat",0.0)!!
            lat = result.data?.getDoubleExtra("passingLat",0.0)!!

//            iosText.text=setIosphone.toString()
//            countAndroid = Integer.parseInt(setAndroidphone.toString())
//            androidText.text = setAndroidphone.toString()
//            countIos = Integer.parseInt(setIosphone.toString())

            when (result.resultCode) {

                Activity.RESULT_OK -> {
                    Toast.makeText(this, getString(R.string.okay), Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, getString(R.string.not_okay), Toast.LENGTH_LONG).show()
                }
            }
        }
        private fun setMapLongClick(map: GoogleMap) {
            map.setOnMapLongClickListener { latLng ->
                // A Snippet is Additional text that's displayed below the title.
                val snippet = String.format(
                    Locale.getDefault(),
                    "Lat: %1$.5f, Long: %2$.5f",
                    latLng.latitude,
                    latLng.longitude
                )
                map.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("New Location")
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                )
            }
        }

        private fun getAddress(loc:LatLng): String? {
            val geocoder = Geocoder(this, Locale.getDefault())
            var addresses: List<Address>? = null
            try {
                addresses = geocoder.getFromLocation(loc!!.latitude, loc!!.longitude, 1)
            } catch (e1: IOException) {
                Log.e("Geocoding", getString(R.string.problem), e1)
            } catch (e2: IllegalArgumentException) {
                Log.e("Geocoding", getString(R.string.invalid)+
                        "Latitude = " + loc!!.latitude +
                        ", Longitude = " +
                        loc!!.longitude, e2)
            }
            // If the reverse geocode returned an address
            if (addresses != null) {
                // Get the first address
                val address = addresses[0]
                val addressText = String.format(
                    "%s, %s, %s",
                    address.getAddressLine(0), // If there's a street address, add it
                    address.locality,                 // Locality is usually a city
                    address.countryName)              // The country of the address
                return addressText
            }
            else
            {
                Log.e("Geocoding", getString(R.string.noaddress))
                return ""
            }
        }

    }