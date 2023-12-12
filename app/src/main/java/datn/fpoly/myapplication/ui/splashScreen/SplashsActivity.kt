package datn.fpoly.myapplication.ui.splashScreen


import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivitySplashBinding
import datn.fpoly.myapplication.ui.map.PickPossitionInMapActivity
import datn.fpoly.myapplication.utils.Common
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.google.android.gms.location.LocationServices
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.ui.login.SignInActivity
import javax.inject.Inject
import com.airbnb.mvrx.viewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.data.repository.AuthRepo
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class SplashsActivity : BaseActivity<ActivitySplashBinding>(), SplashViewModel.Factory {
    @Inject
    lateinit var factory: SplashViewModel.Factory
    private val viewModel: SplashViewModel by viewModel()
    @Inject
    lateinit var user : AuthRepo

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
        val account  = user.getUser()
        val manege : Int = Hawk.get("Manage",0)
        val isLogin= Hawk.get<Boolean>("CheckLogin")
        viewModel.subscribe(this){
            if(account!=null&&isLogin){
                if(manege==0){
                    updateStateGetAccount(it)
                }else if(manege==1){
                    updateStateGetStore(it)
                }
            }
        }

        if(account!=null&&isLogin){
            if(manege==0){
                account.id?.let { SplashViewAction.getUser(it) }?.let { viewModel.handle(it) }
            }else if(manege==1){
                account.id?.let { SplashViewAction.getStore(it) }?.let { viewModel.handle(it) }
            }
        }else{
            Handler(Looper.myLooper()!!).postDelayed(Runnable {
                startActivity(Intent(this@SplashsActivity, SignInActivity::class.java))
                finish()
            }, 3000)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("AAAAAAAAAAAA", "token: "+token)

        })
    }
    private fun updateStateGetAccount(state: SplashState){
        when(state.stateUser){
            is Loading-> {
                Timber.tag("AAAAAAAAAAAA").e("updateStateGetAccount: loading account")
            }
            is Success->{
                runBlocking {
                    launch {
                        state.stateUser.invoke()?.let {
                            val listIdStore = mutableListOf<String>()
                            if(it.favoriteStores!!.size!=0){
                                for (index in 0 until it.favoriteStores!!.size){
                                    val id = it.favoriteStores!![index].id
                                    if (id != null) {
                                        listIdStore.add(id)
                                    }
                                }
                            }
                            val user = AccountModel(
                                it.id,
                                it.phone,
                                it.passwd,
                                it.fullname,
                                it.username,
                                it.idRole?.id,
                                listIdStore,
                                it.avatar
                            )
                            Hawk.put("Account", user)
                            startActivity(Intent(this@SplashsActivity, SignInActivity::class.java))
                            finish()
                        }

                    }
                }
                Timber.tag("AAAAAAAAAAAA").e("updateStateGetAccount: Success account")
            }
            is Fail->{
                startActivity(Intent(this@SplashsActivity, SignInActivity::class.java))
                finish()
                Timber.tag("AAAAAAAAAAAA").e("updateStateGetAccount: Fail account")
            }
            else->{

            }
        }
    }
    private fun updateStateGetStore(state: SplashState){
        when(state.stateStore){
            is Loading-> {
                Timber.tag("AAAAAAAAAAAA").e("updateStateGetAccount: loading account")
            }
            is Success->{
                runBlocking {
                    launch {
                        state.stateStore.invoke()?.let {
                            Hawk.put(Common.KEY_STORE, it)
                            startActivity(Intent(this@SplashsActivity, SignInActivity::class.java))
                            finish()
                        }

                    }
                }
                Timber.tag("AAAAAAAAAAAA").e("updateStateGetAccount: Success account")
            }
            is Fail->{
                startActivity(Intent(this@SplashsActivity, SignInActivity::class.java))
                finish()
                Timber.tag("AAAAAAAAAAAA").e("updateStateGetAccount: Fail account")
            }
            else->{

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
                if(it!=null){
                    val location = LatLng(it.latitude, it.longitude)
                    Common.setMyLocation(this@SplashsActivity, location)
                }

            }.addOnCompleteListener() {

            }
        }

    }

    override fun getBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)

    }

    override fun create(initialState: SplashState): SplashViewModel {
      return factory.create(initialState)
    }
}