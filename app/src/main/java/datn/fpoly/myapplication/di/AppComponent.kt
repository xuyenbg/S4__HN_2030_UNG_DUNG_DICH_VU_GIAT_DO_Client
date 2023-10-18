package datn.fpoly.myapplication.di

import datn.fpoly.myapplication.AppApplication
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import datn.fpoly.myapplication.ui.dashboard.DashboardActivity
import datn.fpoly.myapplication.ui.login.SignInActivity
import datn.fpoly.myapplication.ui.signup.RegisterInforActivity
import datn.fpoly.myapplication.ui.signup.SignUpActivity
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
    fun inject(signUpActivity: SignUpActivity)
    fun inject(registerInforActivity: RegisterInforActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}