package datn.fpoly.myapplication.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityPickPossitionInMapBinding
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.DialogLoading
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class PickPossitionInMapActivity :AppCompatActivity(),
    OnMapReadyCallback {
    private lateinit var fusedLoaction: FusedLocationProviderClient
    private lateinit var mapFragment: SupportMapFragment
    private var ggMap: GoogleMap? = null
    private var myLocation: LatLng? = null
    private lateinit var binging : ActivityPickPossitionInMapBinding
    object dataAdress {
        var pick_address: String = ""
        var longitude: Double = 0.0
        var latitude: Double = 0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binging = ActivityPickPossitionInMapBinding.inflate(layoutInflater)
        setContentView(binging.root)
        fusedLoaction = LocationServices.getFusedLocationProviderClient(this)
        binging.btnChossePossition.setOnClickListener {
            myLocation?.let{
                dataAdress.longitude= it.longitude
                dataAdress.latitude= it.latitude
                dataAdress.pick_address = binging.tvCurrentAdress.text.toString()
            }
            myLocation?.let { it1 -> Common.setMyLocation(this, it1) }
            onBackPressedDispatcher.onBackPressed()
        }
        binging.icBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        requestGetCurrentLocation()
    }



    override fun onResume() {
        super.onResume()

    }

    override fun onRestart() {
        super.onRestart()
//        if(Common.checkPermission(this)){
//            getCurrentLocation()
//        }
        requestGetCurrentLocation()
    }

    fun requestGetCurrentLocation() {
        if (Common.isNetwork(this) && Common.checkPermission(this) && Common.isGpsEnabled(this)) {
            getCurrentLocation()
        } else {
            if (!Common.isNetwork(this)) {
                Common.dialogDisconnectWifi(this)
            } else {
                if (!Common.checkPermission(this)) {
                    Common.requestPermission(this)
                } else {
                    if (!Common.isGpsEnabled(this)) {
                        Common.dialogLocationService(this)
                    }

                }
            }
            // xử lý phần chưa đủ điều kiện
        }
    }

    fun loadFragmentMap() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.fr_map) as SupportMapFragment
    }

    private fun getCurrentLocation() {
      DialogLoading.showDialog(this)
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
                   DialogLoading.hideDialog()
                    myLocation = LatLng(it.latitude, it.longitude)
                    loadFragmentMap()
                    mapFragment.getMapAsync(this)
                    CoroutineScope(Dispatchers.IO).launch {
                        val address =
                            Common.getAddress(myLocation!!, this@PickPossitionInMapActivity)

                        runOnUiThread {
                            binging.tvCurrentAdress.text = address
                        }
                    }

                }
            }
        }

    }




    override fun onMapReady(p0: GoogleMap) {
        ggMap = p0
        ggMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        ggMap?.setMaxZoomPreference(10000F);
        ggMap?.setOnCameraChangeListener {
            val posCamera = it.target
            Timber.tag("AAAAAAAAAAAA")
                .e("onMapReady: latitude: " + posCamera.latitude + " longtitude: " + posCamera.longitude)
            myLocation = it.target
//            Hawk.put(Common.KEY_LOCATION,it.target)
            CoroutineScope(Dispatchers.IO).launch {
                val adreess = Common.getAddress(posCamera, this@PickPossitionInMapActivity)
                runOnUiThread {
                    binging.tvCurrentAdress.text = adreess
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            ggMap?.isMyLocationEnabled = true
            ggMap?.uiSettings?.isMyLocationButtonEnabled = false
            ggMap?.uiSettings?.isCompassEnabled = false
        }
        val cameraUpdate = myLocation?.let { CameraUpdateFactory.newLatLngZoom(it, 400f) }
        cameraUpdate?.let {
            ggMap?.animateCamera(cameraUpdate)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RESULT_OK) {
            requestGetCurrentLocation()
        }
    }

}