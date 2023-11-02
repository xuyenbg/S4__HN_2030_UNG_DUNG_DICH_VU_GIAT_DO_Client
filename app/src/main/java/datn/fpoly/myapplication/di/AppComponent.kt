package datn.fpoly.myapplication.di

import datn.fpoly.myapplication.AppApplication
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import datn.fpoly.myapplication.ui.detailstore.DetailStoreActivity
import datn.fpoly.myapplication.ui.home.HomeActivity
import datn.fpoly.myapplication.ui.homeStore.HomeStoreActivity
import datn.fpoly.myapplication.ui.listService.ListServiceActivity
import datn.fpoly.myapplication.ui.login.OTPLoginActivity
import datn.fpoly.myapplication.ui.poststore.AddPostActivity
import datn.fpoly.myapplication.ui.service.DetailServiceActivity
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
    fun inject(otpLoginActivity: OTPLoginActivity)
    fun inject(signUpActivity: SignUpActivity)
    fun inject(registerInforActivity: RegisterInforActivity)
    fun inject(homeUser: HomeActivity)
    fun inject(detailStore: DetailStoreActivity)
    fun inject(addPostActivity: AddPostActivity)
    fun inject(homeStore: HomeStoreActivity)
    fun inject(detailService: DetailServiceActivity)
    fun inject(listService: ListServiceActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}