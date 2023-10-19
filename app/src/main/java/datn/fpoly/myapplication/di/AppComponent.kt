package datn.fpoly.myapplication.di

import datn.fpoly.myapplication.AppApplication
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import datn.fpoly.myapplication.ui.dashboard.DashboardActivity
import datn.fpoly.myapplication.ui.fragment.homeUser.HomeUserFragment
import datn.fpoly.myapplication.ui.home.HomeActivity
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.login.SignInActivity
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        FragmentModule::class,
        ViewModelModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(application: AppApplication)
    fun inject(signInActivity: SignInActivity)
    fun inject(dashboardActivity: DashboardActivity)
    fun inject(homeActivity: HomeActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}