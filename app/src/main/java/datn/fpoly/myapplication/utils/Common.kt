package datn.fpoly.myapplication.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.databinding.DialogGpsBinding
import datn.fpoly.myapplication.databinding.DialogInternetBinding
import java.text.SimpleDateFormat
import java.util.*

object Common {
    val KEY_LOCATION ="Current_location";
    val KEY_CURRENT_ADRESS ="current_adress"
    val REQUEST_CODE_LOCATION =100
    val REQUEST_ACTIVITY_RESULT = 101
    val KEY_STORE_DETAIL="store_detail"
    val KEY_SERVICE_DETAIL="service_detail"
    const val baseUrl = "https://s4-hn-2030-ung-dung-dich-vu-giat-do.onrender.com"

    fun checkPermission(context: Context): Boolean = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context,  android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun repuestPermission(activity: Activity){
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                , REQUEST_CODE_LOCATION)
        }else{
            dialogGotoSetting(activity)
        }
    }
    fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    fun turnOnLocationService(activity: Activity){
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
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
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
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
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
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
    fun dialogGotoSetting(context: Context){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.title_permission)
        builder.setMessage(R.string.content_permission)
        builder.setPositiveButton(R.string.go_to_setting, DialogInterface.OnClickListener { dialog, which ->
            val packageName = context.packageName
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", packageName, null)
            context.startActivity(intent)
            dialog.dismiss()
        })
        builder.setNegativeButton(R.string.cancel,
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        builder.setCancelable(false)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.window?.setDimAmount(1f)
        alertDialog.show()
    }
    @SuppressLint("SimpleDateFormat")
    fun convertISO8601ToCustomFormat(iso8601String: String): String {
        val customFormat = "yyyy-MM-dd"
        val iso8601Format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val customDateFormat = SimpleDateFormat(customFormat)

        // Set the time zone to UTC to match the input string
        iso8601Format.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val date: Date = iso8601Format.parse(iso8601String) as Date
            customDateFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

}