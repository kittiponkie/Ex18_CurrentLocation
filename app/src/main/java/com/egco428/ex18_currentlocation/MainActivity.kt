package com.egco428.ex18_currentlocation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Double.parseDouble
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var locationManager: LocationManager?  = null
    private var locationListener: LocationListener? = null
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener{
            override fun onLocationChanged(location: Location?) {
                //textView.append("\n"+location!!.longitude+"\n"+location.latitude)

                editText.setText(location!!.longitude.toString())
                editText2.setText(location!!.latitude.toString())
                locationManager!!.removeUpdates(locationListener)
            }

            override fun onProviderDisabled(provider: String?) {
                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(i)
            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }
        }
        requestLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            10 -> requestLocation()
            else -> {  }
        }
    }
    private fun requestLocation(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.INTERNET),10)

            }
            return
        }

        gpsBtn.setOnClickListener {
            locationManager!!.requestLocationUpdates("gps",3000,0f,locationListener)

        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        Log.d("test","ok")
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val mu = LatLng(13.793406,100.322514)
        mMap.addMarker(MarkerOptions().position(mu).title("Marker in Mahidol"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mu,6f))

        markBtn.setOnClickListener {
            var latitude = editText.text
            var longtitude = editText2.text
            Log.d("test","latitude : "+latitude)
            Log.d("test","longtitude : "+longtitude)
            var newlatlng = LatLng(latitude.toString().toDouble(),longtitude.toString().toDouble())
            mMap.addMarker(MarkerOptions().position(newlatlng))
            mMap.animateCamera(CameraUpdateFactory.newLatLng(newlatlng))
        }
    }
}
