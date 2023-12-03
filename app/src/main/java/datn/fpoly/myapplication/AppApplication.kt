package datn.fpoly.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.di.AppComponent
import datn.fpoly.myapplication.di.DaggerAppComponent
import datn.fpoly.myapplication.utils.LocalHelper
import datn.fpoly.myapplication.utils.ServiceMessageFirebase
import timber.log.Timber
import timber.log.Timber.Forest.plant
import javax.inject.Inject


open class AppApplication: Application() {
    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    @Inject
    lateinit var localHelper: LocalHelper

    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
        super.onCreate()
        appComponent.inject(this)
        Hawk.init(this).build()
        createNotificationChannel()
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = this.packageName
            val description = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(ServiceMessageFirebase.CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

}