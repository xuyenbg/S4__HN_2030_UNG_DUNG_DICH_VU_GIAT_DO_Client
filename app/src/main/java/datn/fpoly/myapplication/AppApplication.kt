package datn.fpoly.myapplication

import android.app.Application
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
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
        appComponent.inject(this)
    }

}