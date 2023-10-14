package datn.fpoly.myapplication.di

import datn.fpoly.myapplication.AppApplication
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import datn.fpoly.myapplication.ui.dashboard.DashboardActivity
import datn.fpoly.myapplication.ui.demo.DemoActivity
import datn.fpoly.myapplication.ui.login.SignInActivity
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        FragmentModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(application: AppApplication)
    fun inject(signInActivity: SignInActivity)
    fun inject(demoActivity: DemoActivity)
    fun inject(dashboardActivity: DashboardActivity)
    @Component.Factory
    interface Factory {
            fun create(@BindsInstance context: Context): AppComponent
    }
}