package datn.fpoly.myapplication

import android.app.Application
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.di.AppComponent
import datn.fpoly.myapplication.di.DaggerAppComponent
import datn.fpoly.myapplication.utils.LocalHelper
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
        Hawk.init(this)
    }

}