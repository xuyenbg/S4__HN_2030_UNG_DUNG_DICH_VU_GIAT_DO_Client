package datn.fpoly.myapplication.ui.splashScreen


import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivitySplashBinding
import datn.fpoly.myapplication.ui.map.PickPossitionInMapActivity
import datn.fpoly.myapplication.utils.Common
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.location.LocationServices
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.ui.login.SignInActivity

class SplashsActivity : BaseActivity<ActivitySplashBinding>() {
private lateinit var fusedLoaction: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            view.onApplyWindowInsets(windowInsets)
        }
        fusedLoaction = LocationServices.getFusedLocationProviderClient(this)

        if (Common.checkPermission(this)) {
            CoroutineScope(Dispatchers.IO).launch {
                getCurrentLocation()
            }
        }
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
                val location = LatLng(it.latitude, it.longitude)
                Common.setMyLocation(this@SplashsActivity, location)

            }.addOnCompleteListener() {

            }
        }

    }

    override fun getBinding(): ActivitySplashBinding {
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this@SplashsActivity, SignInActivity::class.java))
            finish()
        }, 3000)
        return ActivitySplashBinding.inflate(layoutInflater)

    }
}