package datn.fpoly.myapplication.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.WindowManager.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.databinding.DialogGpsBinding
import datn.fpoly.myapplication.databinding.DialogInternetBinding
import java.util.*

object Common {
    val KEY_LOCATION ="Current_location";
    val KEY_CURRENT_ADRESS ="current_adress"
    val REQUEST_CODE_LOCATION =100
    val REQUEST_ACTIVITY_RESULT = 101
    fun setCurrentLocation(pos: LatLng){
        Hawk.put(KEY_LOCATION, pos)
    }
    fun setCurrentAdress(adress: String){
        Hawk.put(KEY_LOCATION, adress)
    }
    fun getCurrentAdress(): String = Hawk.get(KEY_CURRENT_ADRESS)
    fun getCurrentLocation(): LatLng = Hawk.get(KEY_LOCATION)
    fun checkPermission(context: Context): Boolean = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context,  android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun repuestPermission(activity: Activity){
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)){

        }else{

        }
        ActivityCompat.requestPermissions(activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            , REQUEST_CODE_LOCATION)
    }
    fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    fun turnOnLocationService(activity: Activity){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivity(intent)
    }

    fun getAddress(location: LatLng, context: Context): String {
        return try {
            val address = Geocoder(
                context,
                Locale.getDefault()
            ).getFromLocation(location.latitude, location.longitude, 1)!![0]
            val addressLine = address.getAddressLine(0)
            addressLine
        } catch (e: Exception) {
            "Unknown"
        }
    }
    fun isNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
    @SuppressLint("ResourceType")
    fun dialogDisconnectWifi(context: Context){
        val dialog = Dialog(context)
        val binding = DialogInternetBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(Color.TRANSPARENT)
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        binding.btnConnectNow.setOnClickListener {
            (context as Activity).startActivityForResult(
                Intent(Settings.ACTION_WIFI_SETTINGS),
                REQUEST_ACTIVITY_RESULT
            )
            dialog.dismiss()
        }
        if(!dialog.isShowing){
            dialog.show()
        }
    }
    @SuppressLint("ResourceType")
    fun dialogLocationService(context: Context){
        val dialog = Dialog(context)
        val binding = DialogGpsBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawableResource(Color.TRANSPARENT)
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        binding.btnTurnOn.setOnClickListener {
            turnOnLocationService(context as Activity)
            dialog.dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        if(!dialog.isShowing){
            dialog.show()
        }
    }

}