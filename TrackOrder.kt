package com.example.blingo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.blingo.model.LocationData
import com.google.android.gms.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class TrackOrder : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myLocationMarker: Marker
    private lateinit var locationReference: DatabaseReference
    private var markerHashMap = HashMap<String, Marker>()
    private var flag = 0

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_order)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationReference = FirebaseDatabase.getInstance().getReference("delivery")

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            startLocationUpdates()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val myLocation = LatLng(0.0, 0.0)
        myLocationMarker = mMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))!!

        locationReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val locationData = snapshot.getValue(LocationData::class.java)
                    locationData?.let {
                        val location = LatLng(locationData.latitude, locationData.longitude)
                        val buggyName = locationData.buggyName

                        if (markerHashMap.containsKey(buggyName)) {
                            val marker = markerHashMap[buggyName]
                            animateMarker(marker!!, location)
                        } else {
                            val markerOptions = MarkerOptions()
                                .position(location)
                                .title(buggyName)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.motor))
                            val marker : Marker = mMap.addMarker(markerOptions)!!
                            markerHashMap[buggyName] = marker
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle cancelled event
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val myLocation = LatLng(location.latitude, location.longitude)
                    if (flag == 0) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))
                        flag++
                    }
                    myLocationMarker.position = myLocation
                }
            }
        }, null)
    }

    private fun animateMarker(marker: Marker, toPosition: LatLng) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val duration: Long = 3000

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = Math.min(1.0f, elapsed.toFloat() / duration)
                val lng = t * toPosition.longitude + (1 - t) * marker.position.longitude
                val lat = t * toPosition.latitude + (1 - t) * marker.position.latitude
                marker.position = LatLng(lat, lng)

                if (t < 1.0) {
                    handler.postDelayed(this, 16)
                }
            }
        })
    }
}
