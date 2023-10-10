package datn.fpoly.myapplication.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityPickPossitionInMapBinding
import datn.fpoly.myapplication.utils.Common
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PickPossitionInMapActivity : BaseActivity<ActivityPickPossitionInMapBinding>(), OnMapReadyCallback {
    private lateinit var fusedLoaction: FusedLocationProviderClient
    private lateinit var mapFragment: SupportMapFragment
    private var ggMap: GoogleMap? = null
    private var myLocation: LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(views.root)
        fusedLoaction = LocationServices.getFusedLocationProviderClient(this)
        if(Common.isNetwork(this)&& Common.checkPermission(this)&& Common.isGpsEnabled(this)){
            getCurrentLocation()
        }else{
            // xử lý phần chưa đủ điều kiện
            if(!Common.isNetwork(this)){
                startActivityForResult(
                    Intent(Settings.ACTION_WIFI_SETTINGS),
                    Common.REQUEST_ACTIVITY_RESULT
                )
            }else if(!Common.checkPermission(this)){
                    Common.repuestPermission(this)
            }else if(!Common.isGpsEnabled(this)){
                Common.turnOnLocationService(this)
            }

        }
    }

    fun loadFragmentMap() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.fr_map) as SupportMapFragment
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLoaction.getCurrentLocation(
                LocationRequest.QUALITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {

                        return CancellationTokenSource().token
                    }

                    override fun isCancellationRequested(): Boolean {
                        return false
                    }

                }).addOnSuccessListener() {
                if (it != null) {
                    myLocation = LatLng(it.latitude, it.longitude)
                    loadFragmentMap()
                    mapFragment.getMapAsync(this)
//                    Common.setCurrentLocation(LatLng(it.latitude, it.longitude))
                    runOnUiThread {
                        views.tvCurrentAdress.text = Common.getAddress(myLocation!!, this@PickPossitionInMapActivity)
                    }
                }
            }.addOnCompleteListener() {
                if (it.isSuccessful) {

                }
            }
        }

    }
    fun setPossition(){
        views.btnChossePossition.setOnClickListener {

        }
    }


    override fun getBinding(): ActivityPickPossitionInMapBinding =
        ActivityPickPossitionInMapBinding.inflate(layoutInflater)

    override fun onMapReady(p0: GoogleMap) {
        ggMap = p0
        ggMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        ggMap?.setMaxZoomPreference(10000F);
        ggMap?.setOnCameraChangeListener {
            val posCamera = it.target
            Log.e("AAAAAAAAAAAA", "onMapReady: latitude: "+posCamera.latitude+" longtitude: "+posCamera.longitude)
            CoroutineScope(Dispatchers.IO).launch {
                val adreess = Common.getAddress(posCamera, this@PickPossitionInMapActivity)
              runOnUiThread {
                  views.tvCurrentAdress.text = adreess
              }
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            ggMap?.isMyLocationEnabled = false
            ggMap?.uiSettings?.isMyLocationButtonEnabled = false
        }
        val cameraUpdate = myLocation?.let { CameraUpdateFactory.newLatLngZoom(it, 500f) }
        cameraUpdate?.let {
            ggMap?.animateCamera(cameraUpdate)
        }

    }

}