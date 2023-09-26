package datn.fpoly.myapplication

import android.app.Application
import androidx.databinding.ktx.BuildConfig
import datn.fpoly.myapplication.utils.LocalHelper
import datn.fpoly.myapplication.di.AppComponent
import datn.fpoly.myapplication.di.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

open class AppApplication: Application() {
    private val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    @Inject
    lateinit var localHelper: LocalHelper

    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}